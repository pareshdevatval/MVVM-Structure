package com.vsi.base

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.os.Bundle
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import com.vsi.R
import com.vsi.databinding.AppToolbarBinding
import com.vsi.utils.AppUtils
import javax.inject.Inject


@SuppressLint("Registered")


/**
 * Created by Paresh Devatval
 */
abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {

    private lateinit var mViewModel: T

    private var progressDialog: Dialog? = null
    private lateinit var broadcastReceiver: BroadcastReceiver
    private val toolbarBinding: AppToolbarBinding by lazy {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarLayout)
        DataBindingUtil.getBinding<AppToolbarBinding>(toolbar) as AppToolbarBinding
    }
    private var errorSnackbar: Snackbar? = null
    private val errorClickListener = View.OnClickListener {
        //internetErrorRetryClicked()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBaseObservables()

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                injectAllDependencies()
            }
        }

        /*   LocalBroadcastManager.getInstance(this).registerReceiver(
               broadcastReceiver,
               IntentFilter(AppConstants.SSL_EXPIRED_EVENT)
           )*/
    }

    private fun setBaseObservables() {
        mViewModel = getViewModel()
        mViewModel.errorMessage.observe(this, Observer { errorMessage ->
            // if (errorMessage != null) AppUtils.showSnackBar(getRootView(), getString(errorMessage))
        })

        mViewModel.loadingVisibility.observe(this, Observer { loadingVisibility ->
            loadingVisibility?.let { if (loadingVisibility) showProgress() else hideProgress() }
        })

        mViewModel.apiErrorMessage.observe(this, Observer { apiError ->
            apiError?.let {
                //  AppUtils.showSnackBar(getRootView(), it)
            }
        })

        /*  mViewModel.onLogoutSuccess.observe(this, Observer { logoutSuccess ->
              logoutSuccess?.let {
                  startActivity(Intent(this, LoginActivity::class.java))
                  finishAffinity()
                  AppUtils.finishFromLeftToRight(this)
              }
          })

          mViewModel.onSSLExpire.observe(this, Observer { sslExpire ->
              sslExpire?.let {
                  LocalBroadcastManager.getInstance(this)
                      .sendBroadcast(Intent(AppConstants.SSL_EXPIRED_EVENT))
              }
          })*/
    }


    open fun injectAllDependencies() {

    }

    /* fun reCallLastApi() {
         if (getViewModel().lastCalledApiParams != null) {
             getViewModel().callRestApi(
                 getViewModel().lastCalledApiParams!!,
                 getViewModel().lastCalledApi
             )
         }
     }*/

    fun transparentStatusBar(transparent: Boolean) {

        val window = window
        if (transparent) {
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorAccent)
        }

    }

    private fun getRootView(): View {
        val contentViewGroup = findViewById<View>(android.R.id.content) as ViewGroup
        var rootView = contentViewGroup.getChildAt(0)
        if (rootView == null) rootView = window.decorView.rootView
        return rootView!!
    }


    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(getRootView(), errorMessage, Snackbar.LENGTH_LONG)
        //errorSnackbar?.setAction(R.string.action_retry, errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract fun getViewModel(): T

    /*  abstract fun internetErrorRetryClicked()*/

    fun hideToolbar() {
        toolbarBinding.toolbar.visibility = View.GONE
    }

    fun setToolbarBackgroundColor(color: Int) {
        toolbarBinding.toolbar.setBackgroundColor(ContextCompat.getColor(this, color))
    }

    // toolabr title
    fun setToolbarTitle(title: Int) {
        toolbarBinding.tvToolbarTitle.visibility = View.VISIBLE
        toolbarBinding.tvToolbarTitle.text = resources.getString(title)
    }

    fun setToolbarRightText(title: Int, toolbarRightClickListener: ToolbarRightImageClickListener) {
        toolbarBinding.tvToolbarRight.visibility = View.VISIBLE
        toolbarBinding.tvToolbarRight.text = resources.getString(title)
        toolbarBinding.tvToolbarRight.setOnClickListener { toolbarRightClickListener.onRightImageClicked() }
    }

    // set icon of toolbar left icon
    fun setToolbarTitleIcon(resourceId: Int) {
        toolbarBinding.ivToolbarTitle.setImageResource(resourceId)
        toolbarBinding.ivToolbarTitle.visibility = View.VISIBLE
    }

    // set icon of toolbar left icon
    fun setToolbarLeftIcon(
        resourceId: Int,
        toolbarLeftImageClickListener: ToolbarLeftImageClickListener
    ) {
        toolbarBinding.ivToolbarLeft.setImageResource(resourceId)
        toolbarBinding.ivToolbarLeft.visibility = View.VISIBLE

        toolbarBinding.ivToolbarLeft.setOnClickListener { toolbarLeftImageClickListener.onLeftImageClicked() }
    }

    // set toolbar right icon and implement its click
    fun setToolbarRightIcon(
        resourceId: Int,
        toolbarRightClickListener: ToolbarRightImageClickListener
    ) {
        toolbarBinding.ivToolbarRight.setImageResource(resourceId)
        toolbarBinding.ivToolbarRight.visibility = View.VISIBLE

        toolbarBinding.ivToolbarRight.setOnClickListener { toolbarRightClickListener.onRightImageClicked() }
    }

    // hide toolbar right icon when not needed
    fun hideToolbarRightIcon() {
        toolbarBinding.ivToolbarRight.visibility = View.GONE
    }

    // hide toolbar left icon when not needed
    fun hideToolbarLeftIcon() {
        toolbarBinding.ivToolbarLeft.visibility = View.GONE
    }

    @SuppressLint("InflateParams")
    fun showProgress() {
        if (progressDialog == null) {
            progressDialog = Dialog(this)
            progressDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        // inflating and setting view of custom dialog
        val view = LayoutInflater.from(this).inflate(R.layout.app_loading_dialog, null, false)
        val imageView = view.findViewById<ImageView>(R.id.imageView1)
        /*Glide.with(this)
            .load(R.drawable.loading)
            .into(imageView)*/

        ObjectAnimator.ofFloat(imageView, View.ROTATION, 0f, 360f).apply {
            repeatCount = ObjectAnimator.INFINITE
            duration = 1500
            interpolator = LinearInterpolator()
            start()
        }
        progressDialog!!.setContentView(view)

        // setting background of dialog as transparent
        val window = progressDialog!!.window
        window?.setBackgroundDrawable(ContextCompat.getDrawable(this, android.R.color.transparent))
        // preventing outside touch and setting cancelable false
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.show()
    }

    fun hideProgress() {
        progressDialog?.run {
            if (isShowing) {
                dismiss()
            }
        }
    }

    /* [START] Check if an active internet connection is present or not*/
    /* return boolen value true or false */
    fun isInternetAvailable(): Boolean {
        // getting Connectivity service as ConnectivtyManager
        return AppUtils.hasInternet(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
            progressDialog = null
        }

        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(broadcastReceiver)
    }

    // interface class for toolbar right icon click
    interface ToolbarRightImageClickListener {
        fun onRightImageClicked()
    }

    interface ToolbarLeftImageClickListener {
        fun onLeftImageClicked()
    }

    private fun getApp() = (application as BaseApplication)
}