package com.nicare.ves.di.auth;

import com.nicare.ves.ui.auth.user.FirstFragment;
import com.nicare.ves.ui.auth.user.SecondFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class PassRecoveryBuilderFragment {
    @ContributesAndroidInjector
    abstract FirstFragment contributeFirstFragment();

    @ContributesAndroidInjector
    abstract SecondFragment contributeSecondFragment();
}
