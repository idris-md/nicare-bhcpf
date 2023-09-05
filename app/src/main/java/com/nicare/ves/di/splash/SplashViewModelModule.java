package com.nicare.ves.di.splash;

import androidx.lifecycle.ViewModel;

import com.nicare.ves.di.viewmodels.ViewModelKey;
import com.nicare.ves.ui.splash.SplashViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
@Module
public abstract class SplashViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel.class)
    public abstract ViewModel bindViewModel(SplashViewModel viewModel);

}
