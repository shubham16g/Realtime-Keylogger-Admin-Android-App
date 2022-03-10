package com.shubhamgupta16.realtimekeyloggeradmin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhamgupta16.realtimekeyloggeradmin.adapters.DeviceAdapter
import com.shubhamgupta16.realtimekeyloggeradmin.databinding.ActivityMainBinding
import com.shubhamgupta16.realtimekeyloggeradmin.helpers.DeviceConnection
import com.shubhamgupta16.realtimekeyloggeradmin.helpers.DeviceListener
import com.shubhamgupta16.realtimekeyloggeradmin.helpers.getPositionByDeviceKey
import com.shubhamgupta16.realtimekeyloggeradmin.models.DeviceModel

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<DeviceModel>()
    private lateinit var adapter: DeviceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = DeviceAdapter(this, list){
            /*val intent = Intent(this, CommandActivity::class.java)
            intent.putExtra("device", it)
            startActivity(intent)*/
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        DeviceConnection(object : DeviceListener {
            override fun onDeviceAdded(model: DeviceModel) {
                list.add(model)
                Log.d(TAG, "onDeviceAdded: $model")
                adapter.notifyItemInserted(list.size)
            }

            override fun onDeviceUpdated(model: DeviceModel) {
                val position = list.getPositionByDeviceKey(model.deviceKey)
                position?.let {
                    list[it] = model
                    adapter.notifyItemChanged(it)
                }

            }
        })
    }
}