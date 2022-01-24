package com.dicoding.moviecatalog.utils

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.moviecatalog.data.model.FilmModel

class FilmModelDiffUtils(
    private val oldDataList: List<FilmModel>,
    private val newDataList: List<FilmModel>
) : DiffUtil.Callback(){
    override fun getOldListSize(): Int {
        return oldDataList.size
    }

    override fun getNewListSize(): Int {
        return newDataList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDataList[oldItemPosition].id == newDataList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldDataList[oldItemPosition]
        val newData = newDataList[newItemPosition]
        return oldData.id == newData.id
    }

}