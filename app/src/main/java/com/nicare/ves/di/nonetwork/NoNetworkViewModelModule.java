package com.nicare.ves.di.nonetwork;

import androidx.lifecycle.ViewModel;

import com.nicare.ves.di.viewmodels.ViewModelKey;
import com.nicare.ves.ui.splash.NoNetworkViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class NoNetworkViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NoNetworkViewModel.class)
    public abstract ViewModel bindMainViewModel(NoNetworkViewModel viewModel);

}
