package com.vsi.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData


/**
 * Created by Paresh Devatval
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val reportIssueToSupport: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val apiErrorMessage: MutableLiveData<String> = MutableLiveData()
    val loadingVisibility: MutableLiveData<Boolean> = MutableLiveData()
    val onSSLExpire: MutableLiveData<Boolean> = MutableLiveData()
    val sessionExpired: MutableLiveData<String> = MutableLiveData()
    val onLogoutSuccess: MutableLiveData<Boolean> = MutableLiveData()
    /* val applicationUpdateResponse: MutableLiveData<BaseResponse> = MutableLiveData()
     val vpnThrowable: MutableLiveData<Throwable> by lazy {
         MutableLiveData<Throwable>()
     }

     protected lateinit var prefsObj: Prefs
     protected var subscription: Disposable? = null
     protected lateinit var apiLogZeroService: ApiLogZeroService
     var lastCalledApiParams: HashMap<String, String?>? = null
     var lastCalledApi = ""

     fun onApiStart() {
         loadingVisibility.value = true
         errorMessage.value = null
     }

     fun onInternetError() {
         errorMessage.value = R.string.msg_no_internet
     }

     fun onApiFinish() {
         loadingVisibility.value = false
     }

     fun injectPrefs(prefs: Prefs) {
         this.prefsObj = prefs
     }

     fun getVpnThrowable(): LiveData<Throwable> {
         return vpnThrowable
     }

     fun injectApiServices(apiLogZeroService: ApiLogZeroService) {
         this.apiLogZeroService = apiLogZeroService
     }

     fun callRestApi(
         params: HashMap<String, String?>,
         apiName: String
     ) {
         if (AppUtils.hasInternet(getApplication())) {
             lastCalledApi = apiName
             lastCalledApiParams = params

             var observable: Observable<Response<VerificationResponse>>? = null

             when (lastCalledApi) {
                 ApiParams.API_LOGIN ->
                     observable = apiLogZeroService.apiLogin(params)
                 ApiParams.API_EMAIL_VERIFICATION ->
                     observable = apiLogZeroService.apiEmailVerification(params)
                 ApiParams.API_CANCEL_SUBSCRIPTION ->
                     observable = apiLogZeroService.apiCancelSubscription(params)
                 ApiParams.API_CHANGE_PASSWORD ->
                     observable = apiLogZeroService.apiChangePassword(params)
                 ApiParams.API_SET_PASSWORD ->
                     observable = apiLogZeroService.apiSetPassword(params)
                 ApiParams.API_SET_NEW_PASSWORD ->
                     observable = apiLogZeroService.apiSetNewPassword(params)
                 ApiParams.API_FORGOT_PASSWORD ->
                     observable = apiLogZeroService.apiForgotPassword(params)
                 ApiParams.API_GET_PLAN_DETAIL ->
                     observable = apiLogZeroService.apiGetPlanDetail(params)
                 ApiParams.API_SELECT_PLAN ->
                     observable = apiLogZeroService.apiSelectPlan(params)
                 ApiParams.API_PURCHASE_SUCCESS ->
                     observable = apiLogZeroService.apiPurchaseSuccess(params)
                 ApiParams.API_VERIFICATION_CODE ->
                     observable = apiLogZeroService.apiVerifyCode(params)
                 ApiParams.API_LOGOUT ->
                     observable = apiLogZeroService.apiLogout(params)

             }

             subscription = observable!!
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribeOn(Schedulers.io())
                 .doOnSubscribe { onApiStart() }
                 .subscribe(
                     { handleResponse(it) },
                     this::handleError
                 )

         } else {
             onInternetError()
         }
     }

     open fun <T : BaseResponse> handleResponse(it: Response<T>?) {
         lastCalledApiParams = null

         if (lastCalledApi == ApiParams.API_LOGOUT) {
             if (it!!.body()!!.success) {
                 logoutFromVpn()
             } else {
                 apiErrorMessage.value = it.body()!!.message
             }
         }
     }

     @SuppressLint("CheckResult")
     fun callApiForLogoutUser() {

         val verificationResponse = prefsObj.verificationResponseData

         val params = HashMap<String, String?>()
         params[ApiParams.EMAIL] = verificationResponse?.email

         callRestApi(params, ApiParams.API_LOGOUT)

     }

     internal fun <T : Any> isValidResponse(response: Response<T>): Boolean {

         when (response.code()) {
             AppConstants.RESPONSE_200 -> {
                 val baseResponse = response.body() as BaseResponse

                 return when (baseResponse.appUpdate?.appUpdateStatus) {
                     AppConstants.OPTIONAL_UPDATE,
                     AppConstants.FORCE_UPDATE -> {
                         applicationUpdateResponse.value = baseResponse
                         false
                     }
                     else -> {
                         if (lastCalledApi != ApiParams.API_LOGIN) {
                             onApiFinish()
                         }
                         true
                     }
                 }
             }
             AppConstants.RESPONSE_401 -> {
                 val jsonError = JSONObject(response.errorBody()!!.string())
                 sessionExpired.value = jsonError.optString("message").toString()
                 return false
             }
             else -> {
                 reportIssueToSupport.value = true
                 return false
             }

         }
     }

     fun logoutFromVpn() {
         onApiStart()
         BaseApplication.vpnSdk!!.disconnect()
             .subscribe({
                 BaseApplication.vpnSdk!!.logout().subscribe({ unit ->
                     prefsObj.clearAll(getApplication())
                     onLogoutSuccess.value = true
                     onApiFinish()
                     Unit
                 }, { throwable ->
                     throwable.printStackTrace()
                     apiErrorMessage.value = throwable.toString()
                     onApiFinish()
                     Unit
                 })
                 onApiFinish()
                 Unit
             }, { throwable ->
                 apiErrorMessage.value = throwable.toString()
                 onApiFinish()
                 Unit
             })

     }

     protected fun handleError(error: Throwable) {
         onApiFinish()
         if (error is SSLHandshakeException || error is NullPointerException) {
             onSSLExpire.value = true
             //  apiErrorMessage.value = "SSLHandshakeException"
             return
         } else if (error is RuntimeException) {
             lastCalledApiParams = null
             vpnThrowable.value = error
         } else if (error is UnknownHostException) {
             lastCalledApiParams = null
             apiErrorMessage.value =
                 getApplication<BaseApplication>().getString(R.string.msg_no_internet)
             return
         } else if (lastCalledApi == ApiParams.API_PURCHASE_SUCCESS) {
             val contactSupportDialog = ContactSupportDialog(
                 getApplication<BaseApplication>().applicationContext as Activity,
                 getApplication<BaseApplication>().getString(R.string.msg_payment_report_issue)
             )
             contactSupportDialog.show()
         }
         apiErrorMessage.value = error.message
     }

     override fun onCleared() {
         super.onCleared()
         subscription?.dispose()
     }*/


}