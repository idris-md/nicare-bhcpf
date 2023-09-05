package com.nicare.ves.ui.enrol.vulnerable;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Vulnerable;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Facility;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.LGA;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Ward;
import com.nicare.ves.persistence.local.localdatasources.LocalFacilityDatasource;
import com.nicare.ves.persistence.local.localdatasources.LocalFingerprintDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalLGADataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalTransactionDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalVulnerableDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalWardDataSource;
import com.nicare.ves.persistence.remote.apimodels.Transaction;

import java.io.LineNumberReader;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class EditPrincipalViewModel extends AndroidViewModel {

    LocalLGADataSource mLocalLGADataSource;
    LocalWardDataSource mLocalWardDataSource;
    LocalTransactionDataSource mLocalTransactionDataSource;
    LocalFacilityDatasource mLocalFacilityDatasource;
    LocalVulnerableDataSource mLocalVulnerableDataSource;

    LocalFingerprintDataSource mLocalFingerprintDataSource;

    Application mApplication;

    public EditPrincipalViewModel(@NonNull Application application) {
        super(application);

        mApplication = application;
        mLocalTransactionDataSource = new LocalTransactionDataSource(application);
        mLocalWardDataSource = new LocalWardDataSource(application);
        mLocalLGADataSource = new LocalLGADataSource(application);
        mLocalFacilityDatasource = new LocalFacilityDatasource(application);
        mLocalVulnerableDataSource = new LocalVulnerableDataSource(application);
        mLocalFingerprintDataSource = new LocalFingerprintDataSource(application);

    }

    public Completable updateCount(Facility facility) {
     return  mLocalFacilityDatasource.update(facility);
    }

    public Maybe<Facility> getFaciltyByCode(String facilityCode) {
        return mLocalFacilityDatasource.getFacilityByCode(facilityCode);

    }

    public LiveData<List<Facility>> getFacility(String lga, String ward) {
        return mLocalFacilityDatasource.getFacilityByLgaAndWard(lga, ward);
    }

    public LiveData<Transaction> getTransaction() {
        return mLocalTransactionDataSource.getLiveTodayLog();

    }

    public Maybe<Long> updatePrincipal(Vulnerable enrollment) {
        return mLocalVulnerableDataSource.insertOrUpdate(enrollment);
    }

    public Maybe<Facility> getFacilityByCode(String code) {
        return mLocalFacilityDatasource.getFacilityByCode(code);
    }

    public LiveData<Vulnerable> getPrincipal(long id) {
        return mLocalVulnerableDataSource.principal(id);
    }

    public LiveData<List<Ward>> getWardsByLG(String lgaId) {
        return mLocalWardDataSource.getWardByLga(lgaId);
    }

    public LiveData<List<LGA>> getLGAs() {
        return mLocalLGADataSource.getAllLga();
    }
}
