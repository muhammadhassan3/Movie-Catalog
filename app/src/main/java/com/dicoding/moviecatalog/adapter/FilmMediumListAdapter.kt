package com.dicoding.moviecatalog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.dicoding.moviecatalog.R
import com.dicoding.moviecatalog.adapter.viewholder.ItemMediumViewHolder
import com.dicoding.moviecatalog.data.model.FilmListInterface
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.databinding.LayoutItemMediumBinding
import com.dicoding.moviecatalog.utils.Constant
import com.dicoding.moviecatalog.utils.DiffUtils.filmModelDiffUtils
import com.dicoding.moviecatalog.utils.loadImage

class FilmMediumListAdapter(private val context: Context, private val filmListInterface: FilmListInterface): PagingDataAdapter<FilmModel, ItemMediumViewHolder>(filmModelDiffUtils) {
    override fun onBindViewHolder(holder: ItemMediumViewHolder, position: Int) {
        val item = getItem(position)
        with(holder){
            item?.let { item ->
                binding.apply {
                    imgPoster.loadImage(Constant.ImageBaseUrl+item.poster, item.poster)
                    tvOverview.text = if(item.synopsis != "") item.synopsis else  context.resources.getText(
                        R.string.no_data)
                    tvTitle.text = item.title
                }
                itemView.setOnClickListener{
                    filmListInterface.onItemClicked(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMediumViewHolder {
        val binding = LayoutItemMediumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemMediumViewHolder(binding)
    }


}