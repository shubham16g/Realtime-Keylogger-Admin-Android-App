package com.shubhamgupta16.realtimekeyloggeradmin.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shubhamgupta16.realtimekeyloggeradmin.R
import com.shubhamgupta16.realtimekeyloggeradmin.databinding.ItemEventDataBinding
import com.shubhamgupta16.realtimekeyloggeradmin.helpers.displayDate
import com.shubhamgupta16.realtimekeyloggeradmin.models.EventModel

class EventAdapter(
    private val context: Context, private val list: List<EventModel>,
    private val selectionListener: (isSelectionOn: Boolean) -> Unit
) :
    RecyclerView.Adapter<EventAdapter.ItemViewHolder>() {

    private var selection = false
    fun clearSelection() {
        selection = false
        selectionListener(false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemEventDataBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val model = list[position]
        holder.binding.status.text = "${model.timestamp?.displayDate()}"
//        holder.binding.status.text = "${model.timestamp}"
        val text = model.text?.let { "[${model.text}]" } ?: ""
        val desc =
            model.desc?.let { if (model.text == null) model.desc else " -> ${model.desc}" } ?: ""
        holder.binding.deviceName.text = "$text$desc"

        holder.itemView.setOnLongClickListener {
            if (!selection) {
                list[position].isSelected = !list[position].isSelected
                selection = true
                selectionListener(true)
                notifyItemChanged(position)
            }
            false
        }
        holder.itemView.setOnClickListener {
            if (selection) {
                list[position].isSelected = !list[position].isSelected
                selection = list.any { it.isSelected }
                if (!selection) selectionListener(false)
                notifyItemChanged(position)
            }
        }

        if (model.isSelected) {
            holder.itemView.setBackgroundColor(Color.BLACK)
            holder.itemView.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
        } else {
            holder.itemView.setBackgroundResource(R.color.translucent_card)
            holder.itemView.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    context, when (model.event) {
                        1 -> R.color.g
                        8 -> R.color.r
                        else -> R.color.b
                    }
                )
            )
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ItemViewHolder(val binding: ItemEventDataBinding) :
        RecyclerView.ViewHolder(binding.root)
}