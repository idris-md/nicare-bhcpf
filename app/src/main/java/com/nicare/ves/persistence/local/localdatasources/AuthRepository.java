package com.nicare.ves.persistence.local.localdatasources;

import android.content.Context;
import androidx.lifecycle.LiveData;

import com.nicare.ves.persistence.HUWEDatabase;
import com.nicare.ves.persistence.local.dao.AuthDao;
import com.nicare.ves.persistence.local.databasemodels.EOAuthModel;
import io.reactivex.Maybe;

public class AuthRepository {
    HUWEDatabase mDatabase;
    AuthDao mAuthDao;

    public AuthRepository(Context application) {
        mDatabase = HUWEDatabase.getInstance(application);
        mAuthDao = mDatabase.getAuthDao();
    }

    public Maybe<Long> insertAuth(EOAuthModel activePrincipals) {
        return mAuthDao.insertOrUpdate(activePrincipals);
    }

    public LiveData<EOAuthModel> getAuth() {
        return mAuthDao.getAuthModel();
    }

}
