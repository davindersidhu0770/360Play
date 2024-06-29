package com.a360play.a360nautica.view.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.a360play.a360nautica.R
import com.a360play.a360nautica.data.booking.GamePassTime
import com.a360play.a360nautica.data.booking.GameTypePasses
import com.a360play.a360nautica.data.booking.GamingListData
import com.a360play.a360nautica.data.booking.PassTimeResponse
import com.a360play.a360nautica.databinding.ItemPasstimeBinding
import com.a360play.a360nautica.databinding.ItemTimeslotsBinding
import com.a360play.a360nautica.view.fragment.book.BookStationsFragment

class PassTimeAdapter(
    val passesList: List<PassTimeResponse>,
    val context: Context,
    val clickListener: OnItemClickListener
) : RecyclerView.Adapter<PassTimeAdapter.ViewHolder>() {

    var isClickable = true

    inner class ViewHolder(val binding: ItemPasstimeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPasstimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(passesList[position]) {
                val selectedPass = passesList[position]

                binding.tvpass.text = this.pass
                binding.tvtime.text = this.timeDetails

/*
                for (timeItem in selectedPass.gamePassTimes) {
                    if (timeItem.id in selectedTimes) {
                        binding.tvtime.text = timeItem.timeDetails
                        break  // Break the loop once a matching time is found
                    }
                }
*/

            }
        }

        holder.binding.llremove.setOnClickListener(View.OnClickListener {

            //here user can remove data .... send callback to class..
            clickListener.removePass(position)

        })


    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return passesList.size

        Log.d("22Aug:", "getItemCount() :" + passesList.size)

    }


    interface OnItemClickListener {
        fun removePass(position: Int)
    }
}