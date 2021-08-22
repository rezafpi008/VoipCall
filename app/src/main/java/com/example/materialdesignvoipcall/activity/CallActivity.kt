package com.example.materialdesignvoipcall.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.materialdesignvoipcall.R
import com.example.materialdesignvoipcall.databinding.ActivityCallBinding
import com.example.materialdesignvoipcall.service.LinphoneService
import com.example.materialdesignvoipcall.service.CallCallback
import com.example.materialdesignvoipcall.util.LinPhoneUtil
import com.example.materialdesignvoipcall.util.LinPhoneUtil.addVideo
import com.example.materialdesignvoipcall.util.LinPhoneUtil.pauseOrResume
import com.example.materialdesignvoipcall.util.LinPhoneUtil.removeVideo
import com.example.materialdesignvoipcall.util.LinPhoneUtil.switchCamera
import com.example.materialdesignvoipcall.util.LinPhoneUtil.terminateCurrentCall
import com.example.materialdesignvoipcall.util.LinPhoneUtil.toggleMicrophone
import com.example.materialdesignvoipcall.viewModel.CallViewModel
import org.linphone.core.Call
import org.linphone.core.Core
import org.linphone.core.tools.Log


class CallActivity : AppCompatActivity(), CallCallback {
    lateinit var viewModel: CallViewModel
    lateinit var binding: ActivityCallBinding
    lateinit var core: Core

    companion object {
        private const val MIC_TO_DISABLE_MUTE = 1
        private const val CAMERA_TO_TOGGLE_VIDEO = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_call)
        viewModel = CallViewModel()
        binding.callViewModel = viewModel
        binding.lifecycleOwner = this
        initialData()
        LinphoneService.setCallCallback(this)
        binding.username.text = intent.extras?.get("username").toString()
    }

    private fun initialData() {
        binding.hangUp.setOnClickListener {
            terminateCurrentCall()
            finish()
        }

        binding.pause.setOnClickListener {
            pauseOrResume()
        }

        binding.toggleVideo.setOnClickListener {
            if (checkAndRequestPermission(
                    Manifest.permission.CAMERA, CAMERA_TO_TOGGLE_VIDEO
                )
            ) {
                toggleVideo()
                setVideoLayout()
            }
        }

        binding.muteMic.setOnClickListener {
            viewModel.setMicrophoneState(!viewModel.microphoneState.value!!)
            if (checkAndRequestPermission(
                    Manifest.permission.RECORD_AUDIO, MIC_TO_DISABLE_MUTE
                )
            ) {
                toggleMicrophone()
            }
        }

        binding.toggleSpeaker.setOnClickListener {
            viewModel.setSpeakerState(!viewModel.speakerState.value!!)

        }

        binding.switchCamera.setOnClickListener {
            switchCamera()
        }
    }

    override fun connected() {
        binding.muteMic.isEnabled = true
        binding.toggleSpeaker.isEnabled = true
    }

    override fun streamsRunning() {
        binding.pause.isEnabled = true
        binding.toggleVideo.isEnabled = true
    }

    override fun paused() {
        binding.toggleVideo.isEnabled = false
    }

    override fun pausedByRemote() {

    }

    override fun updating() {

    }

    override fun updatedByRemote() {
        LinPhoneUtil.acceptCallUpdate(true)
        setVideoLayout()
    }

    override fun released() {
        finish()
    }

    override fun end() {
        finish()
    }

    override fun error() {
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        terminateCurrentCall()
    }

    private fun toggleVideo() {
        val call: Call = LinphoneService.getCore().currentCall ?: return
        binding.toggleVideo.isEnabled = false
        if (call.currentParams.videoEnabled()) {
            removeVideo()
        } else {
            addVideo()
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

    fun setVideoLayout() {
        viewModel.setVideoCallState(!viewModel.videoCallState.value!!)
        if (viewModel.videoCallState.value!!)
            viewModel.setVideoCallView(true)
        else
            viewModel.setVideoCallView(false)
    }

    override fun onStart() {
        super.onStart()
        core = LinphoneService.getCore()

        if (core != null) {
            core.nativeVideoWindowId = binding.remoteVideoSurface
            core.nativePreviewWindowId = binding.localPreviewVideoSurface
            core.enableVideoCapture(true)
            core.enableVideoDisplay(true)
            core.videoActivationPolicy.automaticallyAccept = true
        }
    }
}