package com.a360play.a360nautica.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.a360play.a360nautica.R
import com.a360play.a360nautica.data.booking.GamingListData
import com.a360play.a360nautica.databinding.ItemTimeslotsBinding

class GameAdapter(
    val mGameslotsList: List<GamingListData>,
    val context: Context,
    val clickListener: OnItemClickListener
    ) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {
    var selectedPosition = -1
    var isClickable = true

    inner class ViewHolder(val binding: ItemTimeslotsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTimeslotsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (selectedPosition == position)
            holder.binding.llRoot.setBackground(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.corner_curve_yellow_selector
                )
            )
        else
            holder.binding.llRoot.setBackground(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.corner_curve_normal_selector
                )
            )
        with(holder) {
            with(mGameslotsList[position]) {
                binding.tvSlotstype.text = this.game
            }
        }
        holder.binding.llRoot.setOnClickListener(View.OnClickListener {
            if (!isClickable) {

            } else {
                // do your click stuff
                selectedPosition = position
                notifyDataSetChanged()
                clickListener.onGameSelected(position)
            }

        })

    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return mGameslotsList.size
    }

    interface OnItemClickListener {
        fun onGameSelected(position: Int)
    }
}