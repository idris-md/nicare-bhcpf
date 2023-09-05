package com.nicare.ves.di.main;

import androidx.lifecycle.ViewModel;

import com.nicare.ves.ui.main.MainActivityViewModel;
import com.nicare.ves.di.viewmodels.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
@Module
public abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel.class)
    public abstract ViewModel bindMainViewModel(MainActivityViewModel viewModel);

}
