package com.vsi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.vsi.R
import com.vsi.base.BaseFragment
import com.vsi.databinding.TestFragmentBinding
import com.vsi.di.Injectable
import javax.inject.Inject

class TestFragment : BaseFragment<MainViewModel>(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val dataBinding = DataBindingUtil.inflate<TestFragmentBinding>(
            inflater,
            R.layout.test_fragment,
            container,
            false
        )
        mainViewModel.printLog()
        return dataBinding.root
    }

    override fun getViewModel(): MainViewModel {
        return mainViewModel
    }
}