package com.nicare.ves.persistence.local.localdatasources;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.nicare.ves.persistence.HUWEDatabase;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Facility;
import com.nicare.ves.persistence.local.dao.FacilityDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class LocalFacilityDatasource {

    FacilityDao mProviderDao;
    HUWEDatabase mDatabase;

    public LocalFacilityDatasource(Context application) {
        mDatabase = HUWEDatabase.getInstance(application);
        mProviderDao = mDatabase.getProviderDao();
    }

    public Completable insert(List<Facility> facilities) {
        return mProviderDao.insert(facilities);
    }

    public Completable update(Facility facilities) {
        return mProviderDao.updateCapCount(facilities);
    }

    public LiveData<List<Facility>> getFacilityByLgaAndWard(String lga, String wardId) {
        return mProviderDao.getFacilityByLgaAndWard(lga,wardId);
    }

    public LiveData<List<Facility>> getFacilityByLgaAndWardEdit(String lgaId, String wardId, String hcpCode) {
        return mProviderDao.getFacilityByLgaAndWardEdit(lgaId, wardId, hcpCode);
    }

    public Maybe<Facility> getFacilityByCode(String lgaId) {
        return mProviderDao.getFacilityCode(lgaId);
    }


    public LiveData<List<Facility>> getAllFacilities() {
        return mProviderDao.getAllFacilities();

    }
}
