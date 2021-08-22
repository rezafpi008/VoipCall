package com.example.materialdesignvoipcall.util

import android.widget.Toast
import com.example.materialdesignvoipcall.util.MainApplication.Companion.applicationContext

object ToastUtil {

    private var mToast: Toast? = null

    fun showToast(message: String?) {
        if (mToast == null) {
            mToast = Toast.makeText(applicationContext(), message, Toast.LENGTH_SHORT)
        } else {
            mToast!!.cancel()
            mToast = Toast.makeText(applicationContext(), message, Toast.LENGTH_SHORT)
            mToast!!.duration = Toast.LENGTH_SHORT
        }
        mToast!!.show()

    }

    fun showToast(message: Int?) {
        var errorMessage= applicationContext().getString(message!!);
        if (mToast == null) {
            mToast = Toast.makeText(applicationContext(), errorMessage, Toast.LENGTH_SHORT)
        } else {
            mToast!!.cancel()
            mToast = Toast.makeText(applicationContext(), errorMessage, Toast.LENGTH_SHORT)
            mToast!!.duration = Toast.LENGTH_SHORT
        }
        mToast!!.show()

    }
}