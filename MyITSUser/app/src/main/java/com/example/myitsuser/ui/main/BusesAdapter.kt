package com.example.myitsuser.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myitsuser.R
import com.example.myitsuser.models.BusInfo

class BusesAdapter(private val listener: BusClickListener) : RecyclerView.Adapter<BusesAdapter.BusesViewHolder>() {

    private var data = mutableListOf<BusInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bus_list_item, parent, false)
        return BusesViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BusesViewHolder, position: Int) {
        with(data[position]) {
            holder.busNameTextView.text = name
            val path = "From $from to $to"
            holder.busPathTextView.text = path
            val priceWithDa = "$price DA"
            holder.busPriceTextView.text = priceWithDa
        }
        listener.onFinishLoading()
    }

    fun setData(data: MutableList<BusInfo>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class BusesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val busNameTextView = view.findViewById<TextView>(R.id.busName)
        val busPathTextView = view.findViewById<TextView>(R.id.busPath)
        val busPriceTextView = view.findViewById<TextView>(R.id.busPrice)

        init {
            view.setOnClickListener {
                listener.onBusClick(data[adapterPosition])
            }
        }
    }
}