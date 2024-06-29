package com.a360play.a360nautica.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a360play.a360nautica.data.booking.GameAccessoriesItem
import com.a360play.a360nautica.databinding.TitleLayoutBinding
import com.a360play.a360nautica.utils.OnAccessoryClickListener
import com.a360play.a360nautica.utils.OnlyAccessoryClickListener

class OnlyAccessoryAdapter(
    val gameAccessories: List<GameAccessoriesItem>,
    val context: Context,
    val clickListener: OnlyAccessoryClickListener
) : RecyclerView.Adapter<OnlyAccessoryAdapter.ViewHolder>() {

    var selectedPosition = -1
    var value = 0
    var isClickable = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TitleLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val accessory = gameAccessories[position]
        holder.bind(accessory,context,clickListener)
    }

    override fun getItemCount(): Int {
        return gameAccessories.size
    }

    class ViewHolder(private val binding: TitleLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            gameAccessory: GameAccessoriesItem,
            context: Context,
            clickListener: OnlyAccessoryClickListener
        ) {
            binding.tvaccessory.text = gameAccessory.accessory

            val childAdapter = OnlyChildAdapter(gameAccessory.listGameAccessory,context, clickListener,adapterPosition)
            binding.childRecyclerView.adapter = childAdapter
            val layoutManager = GridLayoutManager(binding.root.context, 3)
            binding.childRecyclerView.layoutManager = layoutManager
        }
    }

}