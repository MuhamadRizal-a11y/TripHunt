package com.example.wisatanusantara.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.wisatanusantara.R
import com.example.wisatanusantara.model.Wisata

class WisataAdapter(private val listWisata: ArrayList<Wisata>) : RecyclerView.Adapter<WisataAdapter.ListViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        // Memanggil layout item_raw_wisata.xml
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_raw_wisata, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        // Ambil data berdasarkan posisi yang benar
        val data = listWisata[position]

        // 1. Tampilkan Nama, Lokasi, dan Rating
        holder.tvName.text = data.name
        holder.tvLocation.text = data.location
        holder.tvRating.text = "⭐ ${data.rating}"

        // 2. Load Gambar pakai Glide (Anti-Ketuker)
        // Di dalam onBindViewHolder
        Glide.with(holder.itemView.context)
            .load(data.photo)
            .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE) // Paksa jangan pakai cache dulu buat ngetes
            .skipMemoryCache(true) // Lewati cache memori juga
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.imgPhoto)

        // 3. Logika Klik Item
        holder.itemView.setOnClickListener {
            // Gunakan bindingAdapterPosition agar posisi yang diklik akurat sesuai data terbaru
            val currentPos = holder.bindingAdapterPosition
            if (currentPos != RecyclerView.NO_POSITION) {
                onItemClickCallback?.onItemClicked(listWisata[currentPos])
            }
        }
    }

    override fun getItemCount(): Int = listWisata.size

    // Menghubungkan variabel dengan ID yang ada di item_raw_wisata.xml
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvLocation: TextView = itemView.findViewById(R.id.tv_item_location)
        val tvRating: TextView = itemView.findViewById(R.id.tv_item_rating)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Wisata)
    }
}