package com.nicare.ves.di;

import com.nicare.ves.di.auth.AuthViewModelModule;
import com.nicare.ves.di.auth.ForgotPasswordViewModelModule;
import com.nicare.ves.di.auth.PassRecoveryBuilderFragment;
import com.nicare.ves.di.auth.suspend.SuspendViewModelModule;
import com.nicare.ves.di.auth.ward.WardViewModelModule;
import com.nicare.ves.di.main.MainViewModelModule;
import com.nicare.ves.di.nonetwork.NoNetworkViewModelModule;
import com.nicare.ves.di.profile.ProfileViewModelModule;
import com.nicare.ves.di.splash.SplashViewModelModule;
import com.nicare.ves.ui.auth.device.DeviceNotVerifyFailActivity;
import com.nicare.ves.ui.auth.device.SuspendActivity;
import com.nicare.ves.ui.auth.user.AuthActivity;
import com.nicare.ves.ui.auth.user.PasswordAuthActivity;
import com.nicare.ves.ui.auth.user.PasswordRecoveryActivity;
import com.nicare.ves.ui.auth.user.SetPasswordActivity;
import com.nicare.ves.ui.auth.user.WardActivity;
import com.nicare.ves.ui.main.MainActivity;
import com.nicare.ves.ui.profile.ProfileActivity;
import com.nicare.ves.ui.splash.NoNetworkActivity;
import com.nicare.ves.ui.splash.SplashActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivitiesBuilderModule {

    @ContributesAndroidInjector(modules = {SplashViewModelModule.class})
    abstract SplashActivity provideSplashActivity();

    @ContributesAndroidInjector(modules = {MainViewModelModule.class})
    abstract MainActivity provideMainActivity();

    @ContributesAndroidInjector(modules = {AuthViewModelModule.class})
    abstract AuthActivity provideAuthActivity();

    @ContributesAndroidInjector(modules = {ProfileViewModelModule.class})
    abstract ProfileActivity provideProfileActivity();

    @ContributesAndroidInjector(modules = {SuspendViewModelModule.class})
    abstract SuspendActivity provideSuspendActivity();

    @ContributesAndroidInjector(modules = {NoNetworkViewModelModule.class})
    abstract NoNetworkActivity provideNoNetworkActivity();

    @ContributesAndroidInjector(modules = {WardViewModelModule.class})
    abstract WardActivity provideWardActivity();

    @ContributesAndroidInjector(modules = {ForgotPasswordViewModelModule.class, PassRecoveryBuilderFragment.class})
    abstract PasswordRecoveryActivity provideRecoveryActivity();

    @ContributesAndroidInjector()
    abstract SetPasswordActivity provideSetPasswordActivity();

    @ContributesAndroidInjector()
    abstract PasswordAuthActivity providePasswordAuthActivity();

    @ContributesAndroidInjector()
    abstract DeviceNotVerifyFailActivity provideDeviceNotVerifyActivity();


}
