package com.a360play.a360nautica.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.a360play.a360nautica.R
import com.a360play.a360nautica.data.booking.GameTypePasses
import com.a360play.a360nautica.databinding.ItemTimeslotsBinding

class MultiSelPassesAdapter(
    val mGameslotsList: List<GameTypePasses>,
    var selectedPosition: Int,
    val context: Context,
    val clickListener: OnItemClickListener
) : RecyclerView.Adapter<MultiSelPassesAdapter.ViewHolder>() {

    //    var selectedPosition = -1
    private val selectedPositions = mutableListOf<Int>()

    var isClickable = true
    var removed = false

    inner class ViewHolder(val binding: ItemTimeslotsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTimeslotsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val isSelected = selectedPositions.contains(position)

        if (isSelected) {
            holder.binding.llRoot.setBackground(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.corner_curve_yellow_selector
                )
            )
        } else {

            if (removed) {
                holder.binding.llRoot.setBackground(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.corner_curve_normal_selector
                    )
                )

            }
        }

        with(holder) {
            with(mGameslotsList[position]) {
                binding.tvSlotstype.text = this.pass
            }
        }

        holder.binding.llRoot.setOnClickListener {

            if (!isClickable) {

                Log.d("12June:", isClickable.toString())

            } else {
                if (isSelected) {
                    selectedPositions.remove(position)

                } else {
                    selectedPositions.add(position)
                }
                notifyDataSetChanged()

                clickListener.onPassClicked(position, selectedPositions)
            }

        }

    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return mGameslotsList.size
    }


    interface OnItemClickListener {
        fun onPassClicked(position: Int, selectedPositions: MutableList<Int>)
    }
}