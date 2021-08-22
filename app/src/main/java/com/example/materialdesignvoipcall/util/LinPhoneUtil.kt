package com.example.materialdesignvoipcall.util

import com.example.materialdesignvoipcall.service.LinphoneService
import com.example.materialdesignvoipcall.util.AppConstants.Companion.SIP_DOMAIN
import org.linphone.core.*
import org.linphone.core.tools.Log

object LinPhoneUtil {

    fun acceptCall(call: Call?): Boolean {
        if (call == null) return false
        val core: Core = LinphoneService.getCore()
        val params = core.createCallParams(call)
        call.acceptWithParams(params)
        return true
    }

    fun switchCamera() {
        val core = LinphoneService.getCore()
        val currentDevice = core.videoDevice
        Log.i("[Context] Current camera device is $currentDevice")

        for (camera in core.videoDevicesList) {
            if (camera != currentDevice && camera != "StaticImage: Static picture") {
                Log.i("[Context] New camera device will be $camera")
                core.videoDevice = camera
                break
            }
        }
        val conference = core.conference
        if (conference == null || !conference.isIn) {
            val call = core.currentCall
            if (call == null) {
                Log.w("[Context] Switching camera while not in call")
                return
            }
            call.update(null)
        }
    }

    fun outgoingCall(sipAddress: String) {
        val core = LinphoneService.getCore()
        val domain =
            SharedPreferenceUtil.getStringValue(SIP_DOMAIN)
        val remoteAddress = Factory.instance().createAddress("sip:$sipAddress@$domain")
        remoteAddress
            ?: return
        val params = core.createCallParams(null)
        params ?: return
        params.mediaEncryption = MediaEncryption.None
        core.inviteAddressWithParams(remoteAddress, params)
    }

    fun terminateCurrentCall() {
        val core = LinphoneService.getCore()
        if (core.callsNb == 0) return
        val call = if (core.currentCall != null) core.currentCall else core.calls[0]
        call ?: return
        call.terminate()
    }

    private fun toggleSpeaker() {
        /*if (mAudioManager.isAudioRoutedToSpeaker()) {
            mAudioManager.routeAudioToEarPiece()
        } else {
            mAudioManager.routeAudioToSpeaker()
        }
        mSpeaker.setSelected(mAudioManager.isAudioRoutedToSpeaker())*/
    }

    fun toggleMicrophone() {
        val core = LinphoneService.getCore()
        core.enableMic(!core.micEnabled())
    }

    fun addVideo() {
        val core = LinphoneService.getCore()
        val currentCall = core.currentCall
        val conference = core.conference

        if (conference != null && conference.isIn) {
            val params = core.createConferenceParams()
            val videoEnabled = conference.currentParams.isVideoEnabled
            params.isVideoEnabled = !videoEnabled
            Log.i("[Controls VM] Conference current param for video is $videoEnabled")
            conference.updateParams(params)
        } else if (currentCall != null) {
            val state = currentCall.state
            if (state == Call.State.End || state == Call.State.Released || state == Call.State.Error)
                return
            val params = core.createCallParams(currentCall)
            params?.enableVideo(!currentCall.currentParams.videoEnabled())
            currentCall.update(params)
        }
    }

    fun removeVideo() {
        val core: Core = LinphoneService.getCore()
        val call = core.currentCall
        val params = core.createCallParams(call)
        params!!.enableVideo(false)
        call!!.update(params)
    }

    fun acceptCallUpdate(accept: Boolean) {
        val core: Core = LinphoneService.getCore()
        val call = core.currentCall ?: return
        val params = core.createCallParams(call)
        if (accept) {
            params!!.enableVideo(true)
            core.enableVideoCapture(true)
            core.enableVideoDisplay(true)
        }
        call.acceptUpdate(params)
    }

    fun pauseOrResume() {
        val core = LinphoneService.getCore()
        if (core.callsNb == 0) return
        val call = if (core.currentCall != null) core.currentCall else core.calls[0]
        call ?: return

        if (call.state != Call.State.Paused && call.state != Call.State.Pausing) {
            // If our call isn't paused, let's pause it
            call.pause()
        } else if (call.state != Call.State.Resuming) {
            // Otherwise let's resume it
            call.resume()
        }
    }
}