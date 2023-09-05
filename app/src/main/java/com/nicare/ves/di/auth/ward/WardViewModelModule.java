package com.nicare.ves.di.auth.ward;

import androidx.lifecycle.ViewModel;

import com.nicare.ves.di.viewmodels.ViewModelKey;
import com.nicare.ves.ui.auth.user.WardViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class WardViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(WardViewModel.class)
    public abstract ViewModel bindViewModel(WardViewModel viewModel);
}
