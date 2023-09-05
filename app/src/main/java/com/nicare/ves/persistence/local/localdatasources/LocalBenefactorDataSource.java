package com.nicare.ves.persistence.local.localdatasources;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.nicare.ves.persistence.HUWEDatabase;
import com.nicare.ves.persistence.local.dao.BenefactorDao;
import com.nicare.ves.persistence.local.databasemodels.Benefactor;

import java.util.List;

import io.reactivex.Completable;

public class LocalBenefactorDataSource {

    BenefactorDao mLGADao;
    HUWEDatabase mDatabase;

    public LocalBenefactorDataSource(Application application) {
        mDatabase = HUWEDatabase.getInstance(application);
        mLGADao = mDatabase.getBenefactorDAO();
    }

    public Completable insert(List<Benefactor> lgaList) {
        return mLGADao.insert(lgaList);
    }

    public LiveData<List<Benefactor>> getAllLga() {
        return mLGADao.getBenefactor();
    }


}
