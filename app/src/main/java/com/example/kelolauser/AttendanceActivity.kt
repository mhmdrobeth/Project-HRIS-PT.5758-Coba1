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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

class AttendanceActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        loggedInUserId = sharedPref.getInt("USER_ID", -1)
        val userName = sharedPref.getString("USER_NAME", "User")

        tvClock = findViewById(R.id.tvRealTimeClock)
        tvDate = findViewById(R.id.tvAttendanceDate)
        tvStatus = findViewById(R.id.tvStatusMessage)
        tvWelcome = findViewById(R.id.tvWelcome)
        btnAttendance = findViewById(R.id.btnVerifyFace)
        previewView = findViewById(R.id.previewView)
        
        tvWelcome.text = "Halo, $userName!"
        
        val btnBack: ImageView = findViewById(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        cameraExecutor = Executors.newSingleThreadExecutor()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        startClock()
        setCurrentDate()

        btnAttendance.setOnClickListener {
            if (loggedInUserId != -1) {
                checkFaceRegistration(loggedInUserId)
            } else {
                Toast.makeText(this, "Sesi habis, silakan login kembali", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

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
                    this, cameraSelector, preview, imageAnalysis
                )
            } catch (exc: Exception) {
                Toast.makeText(this, "Gagal memulai kamera", Toast.LENGTH_SHORT).show()
            }

        }, ContextCompat.getMainExecutor(this))
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
                    if (isFaceDetected) {
                        runOnUiThread {
                            tvStatus.text = "Wajah terdeteksi"
                        }
                    } else {
                        runOnUiThread {
                            tvStatus.text = "Arahkan wajah ke kamera"
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("AttendanceActivity", "Face detection failed", e)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Izin kamera tidak diberikan.", Toast.LENGTH_SHORT).show()
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
            val db = AppDatabase.getDatabase(this)
            val user = db.userDao().getAll().find { it.id == userId }

            runOnUiThread {
                if (user != null) {
                    if (user.isFaceRegistered) {
                        performFaceVerification()
                    } else {
                        Toast.makeText(this, "Wajah Anda belum terdaftar.", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, RegisterFaceActivity::class.java)
                        intent.putExtra("USER_ID", user.id)
                        startActivity(intent)
                    }
                }
            }
        }.start()
    }

    private fun performFaceVerification() {
        if (!isFaceDetected) {
            Toast.makeText(this, "Wajah tidak terdeteksi!", Toast.LENGTH_SHORT).show()
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
                val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
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
                    Toast.makeText(this, "Anda berada di luar jangkauan kantor (${distanceInMeters.toInt()}m)", Toast.LENGTH_LONG).show()
                    btnAttendance.isEnabled = true
                }
            } else {
                tvStatus.text = "Gagal mendapatkan lokasi"
                Toast.makeText(this, "Pastikan GPS aktif", Toast.LENGTH_SHORT).show()
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
        
        // Cek jam masuk dari settings
        val sharedPrefSettings = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
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
            val db = AppDatabase.getDatabase(this)
            val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val userName = sharedPref.getString("USER_NAME", "User") ?: "User"

            val newRecord = Attendance(
                userId = loggedInUserId,
                userName = userName,
                date = date,
                time = time,
                status = status
            )
            db.attendanceDao().insert(newRecord)

            runOnUiThread {
                tvStatus.text = "Verifikasi Berhasil!"
                Toast.makeText(this, "Absensi Berhasil Dicatat: $status!", Toast.LENGTH_SHORT).show()
                btnAttendance.isEnabled = true
                finish()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
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
