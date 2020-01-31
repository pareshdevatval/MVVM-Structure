package com.vsi.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.logzero.utils.constants.PrefsConstants

/**
 * Local data related methods for storing the local data
 */
class Prefs(context: Context) {

    private val TAG = "LogZero_Prefs"

    private var preferences: SharedPreferences

    private var editor: SharedPreferences.Editor

    private val BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----\n"
    private val END_CERTIFICATE = "\n-----END CERTIFICATE-----"

    var certificateString: String = ""
        get() = BEGIN_CERTIFICATE + preferences.getString(
            PrefsConstants.SSL_KEY,
            ""
        ) + END_CERTIFICATE

    init {
        preferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    companion object {
        var prefs: Prefs? = null

        fun getInstance(context: Context): Prefs? {
            prefs = if (prefs != null) prefs else Prefs(context)
            return prefs
        }
    }

    /*  var verificationResponseData: VerificationResponse.Data?
          get() = Gson().fromJson<VerificationResponse.Data>(
              preferences.getString(PrefsConstants.VERIFICATION_DATA, ""),
              VerificationResponse.Data::class.java
          )
          set(verificationResponseData) = preferences.edit().putString(
              PrefsConstants.VERIFICATION_DATA,
              Gson().toJson(verificationResponseData)
          ).apply()
  */
    fun save(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    fun save(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    fun save(key: String, value: Int) {
        editor.putInt(key, value).apply()
    }

    fun save(key: String, value: Float) {
        editor.putFloat(key, value).apply()
    }

    fun save(key: String, value: Long) {
        editor.putLong(key, value).apply()
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun save(key: String, value: Set<String>) {
        editor.putStringSet(key, value).apply()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return preferences.getBoolean(key, defValue)
    }

    fun getString(key: String, defValue: String): String? {
        return preferences.getString(key, defValue)
    }

    fun getInt(key: String, defValue: Int): Int {
        return preferences.getInt(key, defValue)
    }

    fun getFloat(key: String, defValue: Float): Float {
        return preferences.getFloat(key, defValue)
    }

    fun getLong(key: String, defValue: Long): Long {
        return preferences.getLong(key, defValue)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun getStringSet(key: String, defValue: Set<String>): Set<String>? {
        return preferences.getStringSet(key, defValue)
    }

    fun getAll(): Map<String, *> {
        return preferences.all
    }

    fun remove(key: String) {
        editor.remove(key).apply()
    }

    fun clearAll(context: Context) {
        val sslCertificate = getString(PrefsConstants.SSL_KEY, "")
        editor.clear()
        editor.apply()
        save(PrefsConstants.SSL_KEY, sslCertificate!!)
    }

    private class Builder(context: Context?) {

        private val context: Context

        init {
            if (context == null) {
                throw IllegalArgumentException("Context must not be null.")
            }
            this.context = context.applicationContext
        }

        /**
         * Method that creates an instance of Prefs
         *
         * @return an instance of Prefs
         */
        fun build(): Prefs {
            return Prefs(context)
        }
    }
}