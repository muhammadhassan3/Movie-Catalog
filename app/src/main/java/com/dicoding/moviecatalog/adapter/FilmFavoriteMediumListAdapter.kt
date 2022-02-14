package com.dicoding.moviecatalog.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.dicoding.moviecatalog.R
import com.dicoding.moviecatalog.adapter.viewholder.ItemMediumViewHolder
import com.dicoding.moviecatalog.data.database.entity.FavoriteEntity
import com.dicoding.moviecatalog.data.model.FilmListInterface
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.databinding.LayoutItemMediumBinding
import com.dicoding.moviecatalog.utils.Constant
import com.dicoding.moviecatalog.utils.DiffUtils.favoriteModelDiffUtils
import com.dicoding.moviecatalog.utils.loadImage

class FilmFavoriteMediumListAdapter(private val context: Context, private val filmListInterface: FilmListInterface) : PagingDataAdapter<FavoriteEntity, ItemMediumViewHolder>(
    favoriteModelDiffUtils
) {

    override fun onBindViewHolder(holder: ItemMediumViewHolder, position: Int) {
        val item = getItem(position)
        with(holder){
            item?.let { item ->
                binding.apply {
                    imgPoster.loadImage(Constant.ImageBaseUrl+item.posterUrl, item.posterUrl)
                    tvOverview.text = if(item.overview != "") item.overview else  context.resources.getText(R.string.no_data)
                    tvTitle.text = item.title
                }
                itemView.setOnClickListener{
                    filmListInterface.onItemClicked(FilmModel(item.title, 0f, "", emptyList(), item.overview, item.mediaType, item.posterUrl, item.itemId))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMediumViewHolder {
        val binding = LayoutItemMediumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemMediumViewHolder(binding)
    }
}