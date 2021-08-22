package com.example.materialdesignvoipcall.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OutgoingCallViewModel:ViewModel() {
    private val _microphoneState: MutableLiveData<Boolean> = MutableLiveData(false)
    val microphoneState: LiveData<Boolean> get() = _microphoneState

    private val _speakerState: MutableLiveData<Boolean> = MutableLiveData(false)
    val speakerState: LiveData<Boolean> get() = _speakerState

    fun setMicrophoneState(state: Boolean) {
        val state: Boolean = state
        _microphoneState.value = state
    }

    fun setSpeakerState(state: Boolean) {
        val state: Boolean = state
        _speakerState.value = state
    }
}