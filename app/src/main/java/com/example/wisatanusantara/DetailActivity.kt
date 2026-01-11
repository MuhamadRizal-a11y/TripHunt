package com.example.wisatanusantara

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.wisatanusantara.databinding.ActivityDetailBinding
import com.example.wisatanusantara.model.Wisata

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val dataWisata = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_WISATA", Wisata::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_WISATA")
        }

        // PERBAIKAN: Ganti 'it' jadi 'wisata' agar tidak bentrok dengan 'it' milik Button
        dataWisata?.let { wisata ->
            supportActionBar?.title = "Detail Wisata"
            binding.tvDetailName.text = wisata.name
            binding.tvDetailLocation.text = wisata.location
            binding.tvDetailDescription.text = wisata.description
            binding.tvDetailRating.text = wisata.rating
            binding.tvDetailPrice.text = wisata.price
            binding.tvDetailHours.text = wisata.hours

            Glide.with(this)
                .load(wisata.photo)
                .into(binding.imgDetailPhoto)

            // --- LOGIKA GOOGLE MAPS SEARCH ---
            binding.btnOpenMaps.setOnClickListener {
                // Sekarang kita pakai 'wisata.name', bukan 'it.name'
                val query = "${wisata.name}, ${wisata.location}"
                val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(query)}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

                mapIntent.setPackage("com.google.android.apps.maps")

                if (mapIntent.resolveActivity(packageManager) != null) {
                    startActivity(mapIntent)
                } else {
                    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(query)}"))
                    startActivity(webIntent)
                }
            }

            // --- LOGIKA SHARE ---
            binding.btnShareWisata.setOnClickListener {
                // Sekarang aman, tidak merah lagi karena merujuk ke 'wisata'
                val shareText = "Cek tempat wisata keren ini: ${wisata.name}\nLokasi: ${wisata.location}\n\n${wisata.description}"
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(sendIntent, "Bagikan via"))
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}