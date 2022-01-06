package com.dicoding.moviecatalog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.moviecatalog.adapter.viewholder.ItemSmallViewHolder
import com.dicoding.moviecatalog.data.model.FilmListInterface
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.databinding.LayoutItemSmallBinding
import com.dicoding.moviecatalog.utils.FilmModelDiffUtils
import com.dicoding.moviecatalog.utils.loadImage

class FilmSmallListAdapter(private val filmInterface: FilmListInterface) :
    RecyclerView.Adapter<ItemSmallViewHolder>() {
    private val list = arrayListOf<FilmModel>()

    fun setData(data: List<FilmModel>) {
        val diffCallback = FilmModelDiffUtils(this.list, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemSmallViewHolder {
        val binding =
            LayoutItemSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemSmallViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemSmallViewHolder, position: Int) {
        holder.apply {
            val item = list[position]
            binding.apply {
                imgPoster.loadImage(item.poster, true, item.poster)
                tvRating.text = item.rating.toString()
                tvTitle.text = item.title
                root.setOnClickListener {
                    filmInterface.onItemClicked(item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}