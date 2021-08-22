package com.example.materialdesignvoipcall.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CallViewModel : ViewModel() {
    private val _videoCallView: MutableLiveData<Boolean> = MutableLiveData(false)
    val videoCallView: LiveData<Boolean> get() = _videoCallView

    private val _callLayout: MutableLiveData<Boolean> = MutableLiveData(true)
    val callLayout: LiveData<Boolean> get() = _callLayout

    private val _videoCallState: MutableLiveData<Boolean> = MutableLiveData(false)
    val videoCallState: LiveData<Boolean> get() = _videoCallState

    private val _microphoneState: MutableLiveData<Boolean> = MutableLiveData(false)
    val microphoneState: LiveData<Boolean> get() = _microphoneState

    private val _speakerState: MutableLiveData<Boolean> = MutableLiveData(false)
    val speakerState: LiveData<Boolean> get() = _speakerState


    fun setVideoCallView(state: Boolean) {
        val state: Boolean = state
        _videoCallView.value = state
    }

    fun setCallLayout(state: Boolean) {
        val state: Boolean = state
        _callLayout.value = state
    }

    fun setVideoCallState(state: Boolean) {
        val state: Boolean = state
        _videoCallState.value = state
    }

    fun setMicrophoneState(state: Boolean) {
        val state: Boolean = state
        _microphoneState.value = state
    }

    fun setSpeakerState(state: Boolean) {
        val state: Boolean = state
        _speakerState.value = state
    }
}