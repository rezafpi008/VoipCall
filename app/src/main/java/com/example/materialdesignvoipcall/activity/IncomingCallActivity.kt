package com.example.materialdesignvoipcall.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.materialdesignvoipcall.R
import com.example.materialdesignvoipcall.databinding.ActivityIncomingCallBinding
import com.example.materialdesignvoipcall.service.IncomingCallCallback
import com.example.materialdesignvoipcall.service.LinphoneService
import com.example.materialdesignvoipcall.util.LinPhoneUtil
import com.example.materialdesignvoipcall.viewModel.CallViewModel
import org.linphone.core.Call

class IncomingCallActivity : AppCompatActivity(), IncomingCallCallback {
    lateinit var binding: ActivityIncomingCallBinding
    lateinit var call: Call
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incoming_call)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_incoming_call)
        binding.lifecycleOwner = this
        initialData()
        LinphoneService.setIncomingCallCallback(this)
        call = LinphoneService.getCore().currentCall!!
        binding.username.text = call.remoteAddress.username
    }

    private fun initialData() {
        binding.answer.setOnClickListener {
            LinPhoneUtil.acceptCall(call)
        }

        binding.decline.setOnClickListener {
            LinPhoneUtil.terminateCurrentCall()
            finish()
        }
    }

    override fun connected() {
        finish()
    }

    override fun end() {
        finish()
    }

    override fun error() {
        LinPhoneUtil.terminateCurrentCall()
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        LinPhoneUtil.terminateCurrentCall()
    }
}