package com.nicare.ves.di.auth.suspend;

import androidx.lifecycle.ViewModel;

import com.nicare.ves.di.viewmodels.ViewModelKey;
import com.nicare.ves.ui.auth.device.SuspendViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class SuspendViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(SuspendViewModel.class)
    public abstract ViewModel bindViewModel(SuspendViewModel viewModel);
}
