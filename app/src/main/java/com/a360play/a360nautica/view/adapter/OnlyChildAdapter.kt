package com.a360play.a360nautica.view.adapter

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.a360play.a360nautica.data.booking.ListGameAccessoryItem
import com.a360play.a360nautica.databinding.LayoutAccessoryBinding
import com.a360play.a360nautica.utils.OnAccessoryClickListener
import com.a360play.a360nautica.utils.OnlyAccessoryClickListener

class OnlyChildAdapter(
    private val gameDetails: List<ListGameAccessoryItem>,
    val context: Context,
    val clickListener: OnlyAccessoryClickListener,
    val adapterPosition: Int
) :
    RecyclerView.Adapter<OnlyChildAdapter.ViewHolder>() {

    var value = 0
    var isClickable = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAccessoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gameDetail = gameDetails[position]
        holder.bind(gameDetail,isClickable,context,clickListener, adapterPosition )

    }

    override fun getItemCount(): Int {
        return gameDetails.size
    }

    class ViewHolder(private val binding: LayoutAccessoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            gameDetail: ListGameAccessoryItem,
            isClickable: Boolean,
            context: Context,
            clickListener: OnlyAccessoryClickListener,
            adapterPosition : Int
        ) {
            binding.tvaccessory.text = gameDetail.optionDetails
            // Set the background color based on colorCode
            if (gameDetail.colorCode!=null)
            binding.llbg.setBackgroundColor(Color.parseColor(gameDetail.colorCode))
            else
            binding.llbg.setBackgroundColor(Color.parseColor("#ADD8E6"))

            binding.ivminus.setOnClickListener {
                if (!isClickable) {

                    Log.d("12June:", isClickable.toString())

                } else {
                    if (gameDetail.selectedQuantity != 0)
                        gameDetail.selectedQuantity--
                    val editable = Editable.Factory.getInstance()
                        .newEditable(gameDetail.selectedQuantity.toString())

                    binding.edquantity.text = editable
                    clickListener.onlyAccessorySelected(adapterPosition,getAdapterPosition())

//                    notifyDataSetChanged()
                }

            }

            val editable = Editable.Factory.getInstance()
                .newEditable(gameDetail.selectedQuantity.toString())

            binding.edquantity.text = editable

            binding.tvaccessory.setOnClickListener {
                if (!isClickable) {

                    Log.d("12June:", isClickable.toString())

                } else {

                    if (gameDetail.quantity == 0) {
                        // show alert here as item is out of stock..

                        Toast.makeText(context, "Out of stock!", Toast.LENGTH_SHORT).show()

                    } else {

                        Log.d("26July:", gameDetail.selectedQuantity.toString())

                        if (gameDetail.selectedQuantity < gameDetail.quantity
                        ) {
                            gameDetail.selectedQuantity++
                            val editable = Editable.Factory.getInstance()
                                .newEditable(gameDetail.selectedQuantity.toString())

                            binding.edquantity.text = editable
                            clickListener.onlyAccessorySelected(adapterPosition,getAdapterPosition())

//                            notifyDataSetChanged()
                        } else {
                            // Handle the case when the maximum quantity is reached (e.g., show a toast)
                            Toast.makeText(context, "Maximum quantity reached!", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }

                }
            }

        }
    }

}
