package com.vsi.base

import android.app.Application
import com.vsi.BuildConfig
import com.vsi.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject


class BaseApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AppInjector.init(this)

        /* if (isEmpty(
                 Prefs.getInstance(this)!!
                     .getString(PrefsConstants.DEVICE_TOKEN, "")
             )
         ) {
             getDeviceToken()
         }*/

    }

    override fun androidInjector() = dispatchingAndroidInjector


    /*   private fun getDeviceToken() {

           FirebaseInstanceId.getInstance().instanceId
               .addOnCompleteListener(OnCompleteListener { task ->
                   if (!task.isSuccessful) {
                       task.exception!!.stackTrace
                       return@OnCompleteListener
                   }

                   // Get new Instance ID token
                   val token = task.result?.token

                   Prefs.getInstance(this)!!.save(PrefsConstants.DEVICE_TOKEN, token!!)
                   // Log and toast
                   Log.e(BaseApplication::class.simpleName, token)
               })
       }

       fun getAppComponent(): AppComponent {
           return DaggerAppComponent.builder().appModule(AppModule(this)).build()
       }

       fun getNetworkComponent(): NetworkComponent {
           return DaggerNetworkComponent.builder().appComponent(getAppComponent())
               .networkModule(NetworkModule()).build()
       }*/
}