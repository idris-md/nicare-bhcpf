package com.nicare.ves.ui.enrol.premium;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


import com.nicare.ves.persistence.local.databasemodels.utilmodels.Premium;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Reproductive;
import com.nicare.ves.persistence.local.localdatasources.LocalPINDataSource;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class PINActivityViewModel extends AndroidViewModel {
    LocalPINDataSource mLocalPINDataSource;
    Application mApplication;

    public PINActivityViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        mLocalPINDataSource = new LocalPINDataSource(application);
    }

    public Maybe<Reproductive> getReproductive(String pin) {
        return mLocalPINDataSource.getReproductive(pin);
    }

}
