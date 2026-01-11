package com.example.wisatanusantara

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.wisatanusantara.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            try {
                contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                binding.imgAboutPhoto.setImageURI(it)
                saveImageUri(it.toString())
            } catch (e: Exception) {
                binding.imgAboutPhoto.setImageURI(it)
                saveImageUri(it.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Profil Pengguna"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        binding.tvAboutName.text = sharedPref.getString("SAVED_NAME", "Nama User")
        binding.tvAboutEmail.text = sharedPref.getString("SAVED_EMAIL", "Email User")

        val savedImage = sharedPref.getString("SAVED_IMAGE", null)
        if (savedImage != null) {
            try {
                binding.imgAboutPhoto.setImageURI(Uri.parse(savedImage))
            } catch (e: Exception) {
                binding.imgAboutPhoto.setImageResource(R.drawable.ic_profile)
            }
        } else {
            binding.imgAboutPhoto.setImageResource(R.drawable.ic_profile)
        }

        binding.btnEditPhoto.setOnClickListener { getContent.launch("image/*") }

        binding.btnDeletePhoto.setOnClickListener { deleteProfilePhoto() }

        // LOGIKA KLIK LOGOUT
        binding.btnLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        // Jika lu mau menghapus data login saat logout (biar login lagi dari nol):
        // val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        // sharedPref.edit().clear().apply()
        // Note: Gue saranin jangan .clear() semua biar akunnya gak ilang,
        // cukup pindah halaman saja sebagai tanda 'keluar sesi'.

        val intent = Intent(this, LoginActivity::class.java)

        // PENTING: Clear all previous activities (biar gak bisa klik back balik ke profile)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
        Toast.makeText(this, "Berhasil Logout", Toast.LENGTH_SHORT).show()
    }

    private fun saveImageUri(uriString: String) {
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPref.edit().putString("SAVED_IMAGE", uriString).apply()
        Toast.makeText(this, "Foto profil diperbarui!", Toast.LENGTH_SHORT).show()
    }

    private fun deleteProfilePhoto() {
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPref.edit().remove("SAVED_IMAGE").apply()
        binding.imgAboutPhoto.setImageResource(R.drawable.ic_profile)
        Toast.makeText(this, "Foto profil dihapus!", Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}