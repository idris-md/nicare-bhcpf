package com.nicare.ves.di.auth;

import androidx.lifecycle.ViewModel;

import com.nicare.ves.di.viewmodels.ViewModelKey;
import com.nicare.ves.ui.auth.user.AuthViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
@Module
public abstract class AuthViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(AuthViewModel.class)
    public abstract ViewModel bindViewModel(AuthViewModel viewModel);
}
