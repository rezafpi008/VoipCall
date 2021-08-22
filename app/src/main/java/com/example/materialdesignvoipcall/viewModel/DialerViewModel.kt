package com.example.materialdesignvoipcall.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.linphone.core.RegistrationState

class DialerViewModel: ViewModel() {

    private val _connectionStatus: MutableLiveData<RegistrationState> = MutableLiveData(RegistrationState.Progress)
    val connectionStatus: LiveData<RegistrationState> get() = _connectionStatus
    fun setConnectionStatus(state: RegistrationState) {
        _connectionStatus.value = state
    }

    private val _connectionStatusTitle: MutableLiveData<String> = MutableLiveData()
    val connectionStatusTitle: LiveData<String> get() = _connectionStatusTitle
    fun setConnectionStatusTitle(state: String) {
        _connectionStatusTitle.value = state
    }

}