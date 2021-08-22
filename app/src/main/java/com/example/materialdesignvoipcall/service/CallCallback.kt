package com.example.materialdesignvoipcall.service

interface CallCallback :ServiceMainCallbacks{
    fun streamsRunning()
    fun paused()
    fun pausedByRemote()
    fun updating()
    fun updatedByRemote()
    fun released()
}