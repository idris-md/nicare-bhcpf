package com.nicare.ves.persistence.local.localdatasources;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.nicare.ves.persistence.HUWEDatabase;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Ward;
import com.nicare.ves.persistence.local.dao.WardDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class LocalWardDataSource {

    WardDao mWardDao;
    HUWEDatabase mDatabase;

    public LocalWardDataSource(Application application) {
        mDatabase = HUWEDatabase.getInstance(application);
        mWardDao = mDatabase.getWardDao();
    }

    public Completable insert(List<Ward> wardList) {
        return mWardDao.insert(wardList);
    }

    public Completable updateWardCount(int wardId) {
        return mWardDao.updateTotalEnrolled(wardId);
    }

    public LiveData<List<Ward>> getWardByLga(String lgaId)  {
        return mWardDao.wardByLga(lgaId);
    }

    public LiveData<List<Ward>> getEnroleableWardByLga(int lgaId)  {
        return mWardDao.enroleableWardByLGA(lgaId);
    }

    public Maybe<Ward> getWardById(String wardId) {
        return mWardDao.wardById(wardId);
    }


    public LiveData<List<Ward>> getAllWards() {
        return mWardDao.getAllWards();    }
}
