package com.a360play.a360nautica.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.a360play.a360nautica.R
import com.a360play.a360nautica.data.booking.GameTypeResponse
import com.a360play.a360nautica.databinding.ItemTimeslotsBinding
import com.a360play.a360nautica.view.fragment.book.BookStationsFragment

class TypeOfGameAdapter(
    val mGameslotsList: List<GameTypeResponse>,
    val context: Context,var selectedPosition:Int,
    val clickListener: BookStationsFragment
    ) : RecyclerView.Adapter<TypeOfGameAdapter.ViewHolder>() {

//    var selectedPosition = 0

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
                binding.tvSlotstype.text = this.type
            }
        }
        holder.binding.llRoot.setOnClickListener(View.OnClickListener {
            Log.d("12June:",isClickable.toString())

            if (!isClickable) {

                Log.d("12June:",isClickable.toString())

            } else{

                selectedPosition = position
                notifyDataSetChanged()
                clickListener.onTypeOfGameClicked(position,0)
            }

        })

    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return mGameslotsList.size
    }


    interface OnItemClickListener {
        fun onTypeOfGameClicked(position: Int, passId: Int)
    }
}