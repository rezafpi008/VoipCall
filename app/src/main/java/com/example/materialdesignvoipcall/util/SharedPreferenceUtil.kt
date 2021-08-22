package com.example.materialdesignvoipcall.util

import android.content.Context
import android.os.Environment
import com.example.materialdesignvoipcall.util.MainApplication.Companion.applicationContext
import java.util.*

class SharedPreferenceUtil {
    companion object {
        fun saveBooleanValue(key: String?, value: Boolean) {
            val settings = applicationContext().getSharedPreferences(
                AppConstants.SHARE_FILE_NAME, Context.MODE_PRIVATE
            )
            val editor = settings.edit()
            editor.putBoolean(key, value)
            editor.commit()
        }

        fun getBooleanValue(key: String?): Boolean {
            val settings = applicationContext().getSharedPreferences(
                AppConstants.SHARE_FILE_NAME, Context.MODE_PRIVATE
            )
            return settings.getBoolean(key, false)
        }

        fun saveIntValue(key: String?, value: Int) {
            val settings = applicationContext().getSharedPreferences(
                AppConstants.SHARE_FILE_NAME, Context.MODE_PRIVATE
            )
            val editor = settings.edit()
            editor.putInt(key, value)
            editor.commit()
        }


        fun getIntValue(key: String?): Int {
            val settings = applicationContext().getSharedPreferences(
                AppConstants.SHARE_FILE_NAME, Context.MODE_PRIVATE
            )
            return settings.getInt(key, -1)
        }

        fun saveStringValue(key: String?, value: String?) {
            val settings = applicationContext().getSharedPreferences(
                AppConstants.SHARE_FILE_NAME, Context.MODE_PRIVATE
            )
            val editor = settings.edit()
            editor.putString(key, value)
            editor.commit()
        }

        fun getStringValue(key: String?): String? {
            val settings = applicationContext().getSharedPreferences(
                AppConstants.SHARE_FILE_NAME, Context.MODE_PRIVATE
            )
            return settings.getString(key, "")
        }

        fun cleanStringValue(context: Context, vararg keys: String?) {
            for (key in keys) {
                val settings = context.getSharedPreferences(
                    AppConstants.SHARE_FILE_NAME, Context.MODE_PRIVATE
                )
                val editor = settings.edit()
                if (settings.contains(key)) {
                    editor.remove(key).commit()
                }
            }
        }

        fun cleanAll() {
            val settings = applicationContext().getSharedPreferences(
                AppConstants.SHARE_FILE_NAME, Context.MODE_PRIVATE
            )
            val editor = settings.edit()
            editor.clear().apply()
        }

    }
}