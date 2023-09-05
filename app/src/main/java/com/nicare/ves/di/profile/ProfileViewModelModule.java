package com.nicare.ves.di.profile;

import androidx.lifecycle.ViewModel;

import com.nicare.ves.di.viewmodels.ViewModelKey;
import com.nicare.ves.ui.profile.ProfileViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ProfileViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    public abstract ViewModel bindViewModel(ProfileViewModel viewModel);

}
