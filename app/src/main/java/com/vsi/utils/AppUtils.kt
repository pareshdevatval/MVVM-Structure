package com.vsi.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils.isEmpty
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.vsi.R
import java.text.SimpleDateFormat


object AppUtils {

    /**
     * A method which returns the state of internet connectivity of user's phone.
     */
    fun hasInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    /**
     * A common method used in whole application to show a snack bar
     */
    fun showSnackBar(v: View, msg: String) {
        val mSnackBar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG)
        val view = mSnackBar.view
        // val typeface = ResourcesCompat.getFont(v.context, R.font.montserrat_regular)
        val params = view.layoutParams as FrameLayout.LayoutParams
        //  params.gravity = Gravity.TOP
        view.layoutParams = params
        view.setBackgroundColor(ContextCompat.getColor(v.context, R.color.white_1))
        val mainTextView = view.findViewById<TextView>(R.id.snackbar_text)
        // val mainTextView: TextView = view.findViewById(android.support.design.R.id.snackbar_text)
        mainTextView.setTextColor(ContextCompat.getColor(v.context, R.color.black_1))
        /*mainTextView.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            v.context.resources.getDimension(R.dimen.regular)
        )*/
        mainTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        mainTextView.maxLines = 3
        // mainTextView.typeface = typeface
        mainTextView.gravity = Gravity.CENTER_HORIZONTAL
        mSnackBar.show()
    }

    fun getScreenWidth(activity: Activity?): Int {
        if (activity != null) {
            val display = activity.windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.x
        }
        return 0
    }

    fun getScreenHeight(activity: Activity?): Int {
        if (activity != null) {
            val display = activity.windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.y
        }
        return 0
    }

    fun getText(textView: TextView): String {
        return textView.text.toString().trim()
    }

    /**
     * A method to show device keyboard for user input
     */
    fun showKeyboard(activity: Activity?, view: EditText) {
        try {
            val inputManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        } catch (e: Exception) {
            Log.e("Exception showKeyboard", e.toString())
        }
    }

    /**
     * A method to hide the device's keyboard
     */
    fun hideKeyboard(activity: Activity) {
        if (activity.currentFocus != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }


    /*Alert Dialog*/
    fun showAlertDialog(
        ctx: Context?, title: String, msg: String,
        btn1: String, btn2: String?,
        listener1: DialogInterface.OnClickListener,
        listener2: DialogInterface.OnClickListener?
    ): AlertDialog? {
        if (ctx != null) {
            val builder = AlertDialog.Builder(ctx)
            builder.setTitle(title)
            builder.setMessage(msg).setCancelable(true)
                .setPositiveButton(btn1, listener1)
            if (btn2 != null) {
                builder.setNegativeButton(btn2, listener2)
            }
            val alert = builder.create()
            alert.show()
            return alert
        }
        return null
    }

    /*Setting dialog for permission*/
    fun showDialogForRedirectToSettings(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.title_error))
            .setMessage(context.getString(R.string.msg_grant_permission))
            .setPositiveButton(context.getString(R.string.title_go_to_settings)) { dialog, which ->
                (context as Activity).finish()
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }
            .setNegativeButton(context.getText(R.string.btn_cancel)) { dialog, _ ->
                (context as Activity).finish()
                dialog.dismiss()

            }
            .setCancelable(false)
            .create()
            .show()
    }

    /*Check Device Type*/
    fun isTablet(context: Context): Boolean {
        return (context.resources.configuration.screenLayout
                and
                Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }


    @JvmStatic
    fun getDateFromFormattedDate(strDate: String?): String {
        return try {
            if (!isEmpty(strDate)) {
                val simpleFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")//yyyy-MM-dd'T'HH:mm:ssZ
                val simpleFormat2 = SimpleDateFormat("MM-dd-yyyy")
                val date = simpleFormat.parse(strDate)
                simpleFormat2.format(date)
            } else {
                ""
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }
    }
}