package com.shubhamgupta16.realtimekeyloggeradmin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhamgupta16.realtimekeyloggeradmin.adapters.EventAdapter
import com.shubhamgupta16.realtimekeyloggeradmin.databinding.FragmentEventsBinding
import com.shubhamgupta16.realtimekeyloggeradmin.helpers.DatabaseListener
import com.shubhamgupta16.realtimekeyloggeradmin.helpers.EventCase
import com.shubhamgupta16.realtimekeyloggeradmin.helpers.getDbReference
import com.shubhamgupta16.realtimekeyloggeradmin.models.EventModel

class EventsFragment : Fragment() {
    private var deviceKey: String? = null
    private lateinit var binding: FragmentEventsBinding
    private var databaseListener: DatabaseListener<EventModel>? = null
    private var adapter: EventAdapter? = null
    private val list = ArrayList<EventModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            deviceKey = it.getString(DEVICE_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEventListening()
        val linearLayoutManager =
            LinearLayoutManager(requireContext()).apply { stackFromEnd = true }
        binding.deleteFab.hide()
        binding.recycler.layoutManager = linearLayoutManager
        databaseListener =
            DatabaseListener("actions_v2/${deviceKey}", getDbReference()) {
                EventModel.fromSnapshot(it)
            }
        databaseListener?.enableEventListener()
        databaseListener?.childListener = { case, model, prevKey ->
            when (case) {
                EventCase.ADDED -> {
                    list.add(model)
                    adapter?.notifyItemInserted(list.lastIndex)
                    linearLayoutManager.let {
                        val last = it.findLastVisibleItemPosition()
                        if (last >= it.itemCount - 2 && last > 0)
                            binding.recycler.smoothScrollToPosition(list.lastIndex)
                    }
                }
                EventCase.CHANGED -> {
                    val position = list.indexOfFirst { it.timestamp == model.timestamp }
                    list[position] = model
                    adapter?.notifyItemChanged(position)
                }
                EventCase.REMOVED -> {
                    val position = list.indexOfFirst { it.timestamp == model.timestamp }
                    if (position >= 0) {
                        list.removeAt(position)
                        adapter?.notifyItemRemoved(position)
                        adapter?.notifyItemRangeChanged(position, list.size)

                    }
                }
            }
        }
    }

    private fun setupEventListening() {
        adapter = EventAdapter(requireContext(), list) {
            if (it)
                binding.deleteFab.show()
            else
                binding.deleteFab.hide()
//            databaseListener?.removeChild(model.timestamp.toString())
//            list.removeAt(position)
//            adapter?.notifyItemRemoved(position)
        }
        binding.recycler.adapter = adapter

        binding.deleteFab.setOnClickListener {
            val selectedList = list.filter { it.isSelected }
            selectedList.forEach {
                databaseListener?.removeChild("${it.timestamp}")
            }
            adapter?.clearSelection()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        databaseListener?.disableEventListener()
    }

    companion object {
        private const val DEVICE_KEY = "device_key"
        fun newInstance(deviceKey: String) =
            EventsFragment().apply {
                arguments = Bundle().apply {
                    putString(DEVICE_KEY, deviceKey)
                }
            }
    }
}