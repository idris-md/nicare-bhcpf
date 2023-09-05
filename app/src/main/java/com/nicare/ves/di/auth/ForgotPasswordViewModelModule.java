package com.nicare.ves.di.auth;

import androidx.lifecycle.ViewModel;

import com.nicare.ves.di.viewmodels.ViewModelKey;
import com.nicare.ves.ui.auth.user.FirstFragmentViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ForgotPasswordViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(FirstFragmentViewModel.class)
    public abstract ViewModel bindViewModel(FirstFragmentViewModel viewModel);

}
