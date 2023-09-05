package com.nicare.ves.ui.auth.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nicare.ves.persistence.local.databasemodels.EOAuthModel;
import com.nicare.ves.persistence.local.localdatasources.AuthRepository;

import io.reactivex.Maybe;

public class EnrolAuthViewModel extends AndroidViewModel {

    AuthRepository mRepository;

    public EnrolAuthViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AuthRepository(application);
    }

    public Maybe<Long> insert(EOAuthModel eoAuthModel) {
        return mRepository.insertAuth(eoAuthModel);
    }

    public LiveData<EOAuthModel> getAuth() {
        return mRepository.getAuth();
    }


}
