package com.dicoding.moviecatalog.adapter.loadstate

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.dicoding.moviecatalog.adapter.viewholder.ShimmerItemSmallViewHolder
import com.dicoding.moviecatalog.databinding.ShimmerLayoutSmallPlaceholderBinding

class ItemSmallLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<ShimmerItemSmallViewHolder>() {
    override fun onBindViewHolder(holder: ShimmerItemSmallViewHolder, loadState: LoadState) {
        holder.binding.apply {
            if (loadState is LoadState.Loading) {
                if (!loadState.endOfPaginationReached) {
                    error.root.visibility = View.GONE
                    noData.root.visibility = View.GONE

                    shimmer.apply {
                        visibility = View.VISIBLE
                        startShimmer()
                    }
                }
            } else {
                error.root.visibility = View.VISIBLE
                error.btnRetry.setOnClickListener {
                    retry.invoke()
                }
                noData.root.visibility = View.GONE

                shimmer.apply {
                    stopShimmer()
                    visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ShimmerItemSmallViewHolder {
        val binding =
            ShimmerLayoutSmallPlaceholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShimmerItemSmallViewHolder(binding)
    }


}