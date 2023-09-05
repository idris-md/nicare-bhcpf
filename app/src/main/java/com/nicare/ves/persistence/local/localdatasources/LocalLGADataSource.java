package com.nicare.ves.persistence.local.localdatasources;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.nicare.ves.persistence.remote.apimodels.Address;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.LGA;
import com.nicare.ves.persistence.HUWEDatabase;
import com.nicare.ves.persistence.local.dao.LGADao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class LocalLGADataSource {

    LGADao mLGADao;
    HUWEDatabase mDatabase;

    public LocalLGADataSource(Application application) {
        mDatabase = HUWEDatabase.getInstance(application);
        mLGADao = mDatabase.getLGADao();
    }

    public Completable insert(List<LGA> lgaList) {
        return mLGADao.insert(lgaList);
    }

    public LiveData<List<LGA>> getAllLga() {
        return mLGADao.getLGAs();
    }

    public Maybe<LGA> getLGA(String lgaId) {
        return  mLGADao.getLGAById(lgaId);
    }
    public Maybe<Address> getAddress(String lgaId, String wardId) {
        return  mLGADao.getAddress(lgaId,wardId);
    }

}
