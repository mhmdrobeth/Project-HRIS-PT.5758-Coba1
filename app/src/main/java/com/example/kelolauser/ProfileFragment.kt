package com.example.kelolauser

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ProfileFragment : Fragment() {

    private lateinit var tvProfileInitial: TextView
    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileEmail: TextView
    private lateinit var tvProfilePosisi: TextView
    private lateinit var ivProfilePicture: ImageView
    
    private lateinit var menuMyProfile: LinearLayout
    private lateinit var menuLogout: LinearLayout
    private lateinit var btnChangePhoto: MaterialCardView
    private lateinit var btnEditTop: TextView

    private var currentUser: User? = null

    // Register Activity Result Launchers
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { saveImageToInternalStorage(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize Views
        tvProfileInitial = view.findViewById(R.id.tvProfileInitial)
        tvProfileName = view.findViewById(R.id.tvProfileName)
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail)
        tvProfilePosisi = view.findViewById(R.id.tvProfilePosisi)
        ivProfilePicture = view.findViewById(R.id.ivProfilePicture)
        
        menuMyProfile = view.findViewById(R.id.menuMyProfile)
        menuLogout = view.findViewById(R.id.menuLogout)
        btnChangePhoto = view.findViewById(R.id.btnChangePhoto)
        btnEditTop = view.findViewById(R.id.btnEditTop)

        loadUserData()

        // Edit Profile Click (Top Edit button or My Profile menu)
        btnEditTop.setOnClickListener {
            showEditProfileDialog()
        }

        menuMyProfile.setOnClickListener {
            showEditProfileDialog()
        }

        // Profile Picture Click (To Change)
        btnChangePhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Logout Button
        menuLogout.setOnClickListener {
            handleLogout()
        }

        return view
    }

    private fun loadUserData() {
        val sharedPref = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", -1)

        if (userId != -1) {
            Thread {
                val db = AppDatabase.getDatabase(requireContext())
                currentUser = db.userDao().getUserById(userId)
                
                activity?.runOnUiThread {
                    currentUser?.let { user ->
                        tvProfileInitial.text = user.initial
                        tvProfileName.text = user.name
                        tvProfileEmail.text = user.email
                        tvProfilePosisi.text = user.position
                        
                        if (!user.profileImage.isNullOrEmpty()) {
                            val imgFile = File(user.profileImage)
                            if (imgFile.exists()) {
                                val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                                ivProfilePicture.setImageBitmap(bitmap)
                                ivProfilePicture.visibility = View.VISIBLE
                                tvProfileInitial.visibility = View.GONE
                            } else {
                                ivProfilePicture.visibility = View.GONE
                                tvProfileInitial.visibility = View.VISIBLE
                                tvProfileInitial.text = user.initial
                            }
                        } else {
                            ivProfilePicture.visibility = View.GONE
                            tvProfileInitial.visibility = View.VISIBLE
                            tvProfileInitial.text = user.initial
                        }
                    }
                }
            }.start()
        }
    }

    private fun showEditProfileDialog() {
        val user = currentUser ?: return
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null)
        
        val etNama = dialogView.findViewById<EditText>(R.id.etEditNama)
        val etTelepon = dialogView.findViewById<EditText>(R.id.etEditTelepon)
        val etPosisi = dialogView.findViewById<EditText>(R.id.etEditPosisi)
        val etDepartemen = dialogView.findViewById<EditText>(R.id.etEditDepartemen)

        etNama.setText(user.name)
        etTelepon.setText(user.phone)
        etPosisi.setText(user.position)
        etDepartemen.setText(user.department)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Profil")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val newName = etNama.text.toString()
                val newPhone = etTelepon.text.toString()
                val newPosisi = etPosisi.text.toString()
                val newDept = etDepartemen.text.toString()

                updateUser(user.copy(
                    name = newName,
                    phone = newPhone,
                    position = newPosisi,
                    department = newDept,
                    initial = if (newName.isNotEmpty()) newName[0].uppercase() else "U"
                ))
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun updateUser(user: User) {
        Thread {
            val db = AppDatabase.getDatabase(requireContext())
            db.userDao().update(user)
            activity?.runOnUiThread {
                currentUser = user
                loadUserData()
                Toast.makeText(requireContext(), "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun handleLogout() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Apakah Anda yakin ingin keluar?")
            .setPositiveButton("Ya") { _, _ ->
                val sharedPref = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    clear()
                    apply()
                }
                
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    private fun saveImageToInternalStorage(uri: Uri) {
        try {
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
            val file = File(requireContext().filesDir, "profile_${currentUser?.id}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            
            currentUser?.let {
                updateUser(it.copy(profileImage = file.absolutePath))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveBitmapToInternalStorage(bitmap: Bitmap) {
        try {
            val file = File(requireContext().filesDir, "profile_${currentUser?.id}.jpg")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()

            currentUser?.let {
                updateUser(it.copy(profileImage = file.absolutePath))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show()
        }
    }
}
