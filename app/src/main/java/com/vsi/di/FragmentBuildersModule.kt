package com.vsi.di

import com.vsi.ui.TestFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeTestFragment(): TestFragment

    /* @ContributesAndroidInjector
     abstract fun contributeUserFragment(): UserFragment

     @ContributesAndroidInjector
     abstract fun contributeSearchFragment(): SearchFragment*/
}
