package com.shubhamgupta16.realtimekeyloggeradmin.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shubhamgupta16.realtimekeyloggeradmin.databinding.ItemDeviceCardBinding
import com.shubhamgupta16.realtimekeyloggeradmin.helpers.displayDate
import com.shubhamgupta16.realtimekeyloggeradmin.models.DeviceModel

class DeviceAdapter(private val context: Context, private val list: List<DeviceModel>, private val listener: (DeviceModel)->Unit) :
    RecyclerView.Adapter<DeviceAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemDeviceCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val model = list[position]
        holder.binding.status.text = model.lastSeen.displayDate()

        holder.binding.deviceName.text = model.name + if (model.status == 1) " - Online" else " - Offline"
        holder.itemView.setOnClickListener {
            listener(model)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ItemViewHolder(val binding: ItemDeviceCardBinding) :
        RecyclerView.ViewHolder(binding.root)
}