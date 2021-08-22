package com.example.materialdesignvoipcall.util

import java.util.*

open class AppConstants {
    companion object {

        const val SERVER_DOMAIN = "192.168.15.242"

        const val SHARE_FILE_NAME = "VOIP_CALL"

        const val SIP_USERNAME: String = "USERNAME"

        const val SIP_PASSWORD: String = "PASSWORD"

        const val SIP_DOMAIN = "DOMAIN"

        const val SIP_TRANSFER_TYPE = "TRANSFER_TYPE"

        const val SIP_CONNECT = "CONNECT"

        const val UDP = 0
        const val TCP = 1
        const val TLS = 2

        const val CONNECTED = "Connected"
        const val CONNECTION_IN_PROGRESS="Connection in progress"
        const val CONNECTION_FAILED="Connection failed"
        const val CONNECTION_NONE="Not connected"
    }
}