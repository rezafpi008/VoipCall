package com.example.materialdesignvoipcall.activity

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.materialdesignvoipcall.R
import com.example.materialdesignvoipcall.databinding.ActivityCallBinding
import com.example.materialdesignvoipcall.databinding.ActivityOutgoingCallBinding
import com.example.materialdesignvoipcall.service.LinphoneService
import com.example.materialdesignvoipcall.service.OutgoingCallCallback
import com.example.materialdesignvoipcall.util.LinPhoneUtil
import com.example.materialdesignvoipcall.viewModel.CallViewModel
import com.example.materialdesignvoipcall.viewModel.OutgoingCallViewModel
import org.linphone.core.Call
import org.linphone.core.tools.Log

class OutgoingCallActivity : AppCompatActivity(), OutgoingCallCallback {
    lateinit var viewModel: OutgoingCallViewModel
    lateinit var binding: ActivityOutgoingCallBinding
    lateinit var call: Call

    companion object {
        private const val MIC_TO_DISABLE_MUTE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_outgoing_call)
        viewModel = OutgoingCallViewModel()
        binding.callViewModel = viewModel
        binding.lifecycleOwner = this
        initialData()
        LinphoneService.setOutgoingCallCallback(this)
        call = LinphoneService.getCore().currentCall!!
        binding.username.text = call.remoteAddress.username
    }

    private fun initialData() {
        binding.hangUp.setOnClickListener {
            LinPhoneUtil.terminateCurrentCall()
            finish()
        }

        binding.muteMic.setOnClickListener {
            if (checkAndRequestPermission(
                    Manifest.permission.RECORD_AUDIO, MIC_TO_DISABLE_MUTE
                )
            ) {
                LinPhoneUtil.toggleMicrophone()
                viewModel.setMicrophoneState(!viewModel.microphoneState.value!!)
            }
        }

        binding.toggleSpeaker.setOnClickListener {
            viewModel.setSpeakerState(!viewModel.speakerState.value!!)

        }
    }

    private fun checkAndRequestPermission(permission: String, result: Int): Boolean {
        if (!checkPermission(permission)) {
            Log.i("[Permission] Asking for $permission")
            ActivityCompat.requestPermissions(this, arrayOf(permission), result)
            return false
        }
        return true
    }

    private fun checkPermission(permission: String): Boolean {
        val granted = packageManager.checkPermission(permission, packageName)
        Log.i(
            "[Permission] "
                    + permission
                    + " permission is "
                    + if (granted == PackageManager.PERMISSION_GRANTED) "granted" else "denied"
        )
        return granted == PackageManager.PERMISSION_GRANTED
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