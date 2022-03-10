package com.shubhamgupta16.realtimekeyloggeradmin.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shubhamgupta16.realtimekeyloggeradmin.databinding.ActivityDeviceBinding
import com.shubhamgupta16.realtimekeyloggeradmin.helpers.getDeviceModel
import com.shubhamgupta16.realtimekeyloggeradmin.models.DeviceModel
import com.shubhamgupta16.realtimekeyloggeradmin.ui.fragments.EventsFragment

class DeviceActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDeviceBinding
    private lateinit var deviceModel: DeviceModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getDeviceModel().let {
            if (it == null) {
                Toast.makeText(this, "No Device to connect.", Toast.LENGTH_SHORT).show()
                finish()
            } else
                deviceModel = it
        }

        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, EventsFragment.newInstance(deviceModel.deviceKey))
            .commit()
    }
}