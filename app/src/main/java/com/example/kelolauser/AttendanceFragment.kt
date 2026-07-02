package com.example.kelolauser

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AttendanceFragment : Fragment() {

    private lateinit var tvClock: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvWelcome: TextView
    private lateinit var btnAttendance: MaterialButton
    private lateinit var previewView: PreviewView
    
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    
    private var loggedInUserId: Int = -1
    private var isFaceDetected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_attendance, container, false)

        val sharedPref = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        loggedInUserId = sharedPref.getInt("USER_ID", -1)
        val userName = sharedPref.getString("USER_NAME", "User")

        tvClock = view.findViewById(R.id.tvRealTimeClock)
        tvDate = view.findViewById(R.id.tvAttendanceDate)
        tvStatus = view.findViewById(R.id.tvStatusMessage)
        tvWelcome = view.findViewById(R.id.tvWelcome)
        btnAttendance = view.findViewById(R.id.btnVerifyFace)
        previewView = view.findViewById(R.id.previewView)
        
        tvWelcome.text = "Halo, $userName!"
        
        // Hide back button in fragment as it is part of bottom navigation
        view.findViewById<ImageView>(R.id.btnBack).visibility = View.GONE

        cameraExecutor = Executors.newSingleThreadExecutor()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        startClock()
        setCurrentDate()

        btnAttendance.setOnClickListener {
            if (loggedInUserId != -1) {
                checkFaceRegistration(loggedInUserId)
            } else {
                Toast.makeText(requireContext(), "Sesi habis, silakan login kembali", Toast.LENGTH_SHORT).show()
            }
        }
        
        return view
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        processImageProxy(imageProxy)
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageAnalysis
                )
            } catch (exc: Exception) {
                Log.e("AttendanceFragment", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            
            val options = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .build()
            
            val detector = FaceDetection.getClient(options)
            
            detector.process(image)
                .addOnSuccessListener { faces ->
                    isFaceDetected = faces.isNotEmpty()
                    activity?.runOnUiThread {
                        if (isFaceDetected) {
                            tvStatus.text = "Wajah terdeteksi"
                        } else {
                            tvStatus.text = "Arahkan wajah ke kamera"
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("AttendanceFragment", "Face detection failed", e)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "Izin tidak diberikan.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startClock() {
        handler.post(object : Runnable {
            override fun run() {
                val calendar = Calendar.getInstance()
                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                tvClock.text = timeFormat.format(calendar.time)
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun setCurrentDate() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        tvDate.text = dateFormat.format(calendar.time)
    }

    private fun checkFaceRegistration(userId: Int) {
        Thread {
            val db = AppDatabase.getDatabase(requireContext())
            val user = db.userDao().getAll().find { it.id == userId }

            activity?.runOnUiThread {
                if (user != null) {
                    if (user.isFaceRegistered) {
                        performFaceVerification()
                    } else {
                        Toast.makeText(requireContext(), "Wajah Anda belum terdaftar.", Toast.LENGTH_LONG).show()
                        val intent = Intent(requireContext(), RegisterFaceActivity::class.java)
                        intent.putExtra("USER_ID", user.id)
                        startActivity(intent)
                    }
                }
            }
        }.start()
    }

    private fun performFaceVerification() {
        if (!isFaceDetected) {
            Toast.makeText(requireContext(), "Wajah tidak terdeteksi!", Toast.LENGTH_SHORT).show()
            return
        }

        tvStatus.text = "Memverifikasi lokasi..."
        btnAttendance.isEnabled = false

        checkLocationAndSave()
    }

    @SuppressLint("MissingPermission")
    private fun checkLocationAndSave() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val sharedPref = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
                val officeLat = sharedPref.getString("office_latitude", "-6.977116260995142")?.toDoubleOrNull() ?: -6.977116260995142
                val officeLng = sharedPref.getString("office_longitude", "110.45034999660835")?.toDoubleOrNull() ?: 110.45034999660835
                val officeRadius = sharedPref.getString("office_radius", "100")?.toFloatOrNull() ?: 100f

                val results = FloatArray(1)
                Location.distanceBetween(location.latitude, location.longitude, officeLat, officeLng, results)
                val distanceInMeters = results[0]

                if (distanceInMeters <= officeRadius) {
                    saveAttendanceToDatabase()
                } else {
                    tvStatus.text = "Gagal: Di luar jangkauan"
                    Toast.makeText(requireContext(), "Anda berada di luar jangkauan kantor (${distanceInMeters.toInt()}m)", Toast.LENGTH_LONG).show()
                    btnAttendance.isEnabled = true
                }
            } else {
                tvStatus.text = "Gagal mendapatkan lokasi"
                Toast.makeText(requireContext(), "Pastikan GPS aktif", Toast.LENGTH_SHORT).show()
                btnAttendance.isEnabled = true
            }
        }.addOnFailureListener {
            tvStatus.text = "Gagal verifikasi lokasi"
            btnAttendance.isEnabled = true
        }
    }

    private fun saveAttendanceToDatabase() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        
        val date = dateFormat.format(calendar.time)
        val time = timeFormat.format(calendar.time)
        
        val sharedPrefSettings = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val jamMasuk = sharedPrefSettings.getString("jam_masuk", "08:00") ?: "08:00"
        
        val status = try {
            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            val limitDate = format.parse(jamMasuk)
            val currentDate = format.parse(time)
            if (currentDate != null && limitDate != null && currentDate.before(limitDate)) "Hadir" else "Terlambat"
        } catch (e: Exception) {
            "Hadir"
        }

        Thread {
            val db = AppDatabase.getDatabase(requireContext())
            val sharedPref = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val userName = sharedPref.getString("USER_NAME", "User") ?: "User"

            val newRecord = Attendance(
                userId = loggedInUserId,
                userName = userName,
                date = date,
                time = time,
                status = status
            )
            db.attendanceDao().insert(newRecord)

            activity?.runOnUiThread {
                tvStatus.text = "Verifikasi Berhasil!"
                Toast.makeText(requireContext(), "Absensi Berhasil Dicatat: $status!", Toast.LENGTH_SHORT).show()
                btnAttendance.isEnabled = true
                // In a fragment, we might want to stay here or navigate home
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        cameraExecutor.shutdown()
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}
