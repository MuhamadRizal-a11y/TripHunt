package com.example.wisatanusantara

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wisatanusantara.adapter.WisataAdapter
import com.example.wisatanusantara.databinding.ActivityMainBinding
import com.example.wisatanusantara.model.Wisata

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val listOriginal = ArrayList<Wisata>()
    private val listFiltered = ArrayList<Wisata>()
    private lateinit var wisataAdapter: WisataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Menghilangkan judul default agar custom layout terlihat rapi

        // Greeting berdasarkan login
        val username = intent.getStringExtra("EXTRA_USER")
        binding.tvGreeting.text = "Mau ke mana hari ini, ${username ?: "Rizal"}?"

        // Init Data
        listOriginal.clear()
        listOriginal.addAll(getListWisata())
        listFiltered.clear()
        listFiltered.addAll(listOriginal)

        showRecyclerList()
        setupAction()
    }

    private fun setupAction() {
        // Logika Search Filter
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterSearch(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Logika Category Filter
        binding.btnCatAll.setOnClickListener {
            updateList(listOriginal)
            updateButtonUI(binding.btnCatAll)
        }

        binding.btnCatPantai.setOnClickListener {
            filterCategory("Pantai")
            updateButtonUI(binding.btnCatPantai)
        }

        binding.btnCatGunung.setOnClickListener {
            filterCategory("Gunung")
            updateButtonUI(binding.btnCatGunung)
        }
    }

    private fun filterSearch(query: String) {
        val filtered = listOriginal.filter {
            it.name.contains(query, ignoreCase = true) || it.location.contains(query, ignoreCase = true)
        }
        updateList(ArrayList(filtered))
    }

    private fun filterCategory(category: String) {
        val filtered = listOriginal.filter {
            it.description.contains(category, ignoreCase = true) || it.name.contains(category, ignoreCase = true)
        }
        updateList(ArrayList(filtered))
    }

    private fun updateList(newList: ArrayList<Wisata>) {
        listFiltered.clear()
        listFiltered.addAll(newList)
        wisataAdapter.notifyDataSetChanged()
    }

    private fun updateButtonUI(selectedButton: com.google.android.material.button.MaterialButton) {
        val buttons = listOf(binding.btnCatAll, binding.btnCatPantai, binding.btnCatGunung)

        for (btn in buttons) {
            if (btn == selectedButton) {
                // Style Aktif: Hitam
                btn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black))
                btn.setTextColor(ContextCompat.getColor(this, R.color.white))
                btn.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black))
            } else {
                // Style Non-Aktif: Transparan Outlined
                btn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.transparent))
                btn.setTextColor(ContextCompat.getColor(this, R.color.black))
                btn.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.line_color))
            }
        }
    }

    private fun getListWisata(): ArrayList<Wisata> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val dataLocation = resources.getStringArray(R.array.data_location)
        val dataRating = resources.getStringArray(R.array.data_rating)
        val dataPrice = resources.getStringArray(R.array.data_price)
        val dataHours = resources.getStringArray(R.array.data_hours)

        val list = ArrayList<Wisata>()
        for (i in dataName.indices) {
            list.add(Wisata(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1),
                dataLocation[i], dataRating[i], dataPrice[i], dataHours[i]))
        }
        dataPhoto.recycle()
        return list
    }

    private fun showRecyclerList() {
        binding.rvWisata.layoutManager = LinearLayoutManager(this)
        wisataAdapter = WisataAdapter(listFiltered)
        binding.rvWisata.adapter = wisataAdapter
        binding.rvWisata.isNestedScrollingEnabled = false

        wisataAdapter.setOnItemClickCallback(object : WisataAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Wisata) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("EXTRA_WISATA", data)
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // --- LOGIKA BARU UNTUK CUSTOM ACTION LAYOUT ---
        val menuAbout = menu?.findItem(R.id.about_page)
        val actionView = menuAbout?.actionView

        // Cari ImageView di dalam custom layout menu
        val imgProfile = actionView?.findViewById<ImageView>(R.id.img_profile_menu)

        // Pasang listener klik pada view tersebut
        actionView?.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        return true
    }

    // Tetap dipertahankan untuk backup klik standar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.about_page) {
            startActivity(Intent(this, AboutActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}