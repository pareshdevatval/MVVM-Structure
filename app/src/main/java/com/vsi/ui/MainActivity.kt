package com.vsi.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.vsi.R
import com.vsi.base.BaseActivity
import com.vsi.databinding.ActivityMainBinding
import com.vsi.di.Injectable
import com.vsi.utils.AppUtils
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel>(), HasAndroidInjector, Injectable {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: ActivityMainBinding

    val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel.printLog()

        if (AppUtils.hasInternet(this)) {
            AppUtils.showSnackBar(binding.root, "Has Internet")
        } else {
            AppUtils.showSnackBar(binding.root, "No Internet")
        }
    }

    override fun getViewModel(): MainViewModel {
        return mainViewModel
    }

    override fun androidInjector() = dispatchingAndroidInjector
}
