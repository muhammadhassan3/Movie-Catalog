package com.dicoding.moviecatalog.adapter.loadstate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.dicoding.moviecatalog.adapter.viewholder.ShimmerItemMediumViewHolder
import com.dicoding.moviecatalog.databinding.ShimmerLayoutMediumPlaceholderBinding
import com.dicoding.moviecatalog.utils.gone
import com.dicoding.moviecatalog.utils.visible

class ItemMediumLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<ShimmerItemMediumViewHolder>() {
    override fun onBindViewHolder(holder: ShimmerItemMediumViewHolder, loadState: LoadState) {
        holder.binding.apply {
            if(loadState is LoadState.Loading){
                if(!loadState.endOfPaginationReached){
                    error.root.gone()
                    noData.root.gone()
                    shimmer.apply {
                        startShimmer()
                        visible()
                    }
                }
            }else{
                error.root.visible()
                error.btnRetry.setOnClickListener {
                    retry.invoke()
                }
                noData.root.gone()
                shimmer.apply {
                    stopShimmer()
                    gone()
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ShimmerItemMediumViewHolder {
        val binding = ShimmerLayoutMediumPlaceholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShimmerItemMediumViewHolder(binding)
    }

}