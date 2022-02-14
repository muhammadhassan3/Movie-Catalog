package com.dicoding.moviecatalog.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.dicoding.moviecatalog.adapter.viewholder.ItemSmallViewHolder
import com.dicoding.moviecatalog.data.model.FilmListInterface
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.databinding.LayoutItemSmallBinding
import com.dicoding.moviecatalog.utils.Constant
import com.dicoding.moviecatalog.utils.DiffUtils.filmModelDiffUtils
import com.dicoding.moviecatalog.utils.FilmModelDiffUtils
import com.dicoding.moviecatalog.utils.loadImage

class FilmSmallPagingAdapter(private val filmInterface: FilmListInterface): PagingDataAdapter<FilmModel, ItemSmallViewHolder>(filmModelDiffUtils) {

    override fun onBindViewHolder(holder: ItemSmallViewHolder, position: Int) {
        holder.binding.apply {
            val item = getItem(position)
            item?.let{
                imgPoster.loadImage(Constant.ImageBaseUrl+item.poster, item.poster)
                tvRating.text = item.rating.toString()
                tvTitle.text = item.title ?: item.title
                root.setOnClickListener {
                    filmInterface.onItemClicked(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemSmallViewHolder {
        val binding =
            LayoutItemSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemSmallViewHolder(binding)
    }
}