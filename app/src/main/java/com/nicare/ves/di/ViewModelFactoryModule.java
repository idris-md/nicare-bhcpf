package com.nicare.ves.di;

import androidx.lifecycle.ViewModelProvider;

import com.nicare.ves.di.viewmodels.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory  bindViewModelFactory(ViewModelProviderFactory providerFactory);

}
