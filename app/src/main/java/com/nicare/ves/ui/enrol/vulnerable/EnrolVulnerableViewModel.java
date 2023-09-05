package com.nicare.ves.ui.enrol.vulnerable;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.persistence.local.databasemodels.Benefactor;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Fingerprint;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Vulnerable;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Facility;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.LGA;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.NIN;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Reproductive;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Ward;
import com.nicare.ves.persistence.local.localdatasources.LocalBenefactorDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalFacilityDatasource;
import com.nicare.ves.persistence.local.localdatasources.LocalFingerprintDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalLGADataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalPINDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalTransactionDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalVulnerableDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalWardDataSource;
import com.nicare.ves.persistence.remote.apimodels.Transaction;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class EnrolVulnerableViewModel extends AndroidViewModel {

    LocalLGADataSource mLocalLGADataSource;
    LocalBenefactorDataSource mLocalBenefactorDataSource;
    LocalWardDataSource mLocalWardDataSource;
    LocalPINDataSource mLocalPINDataSource;
    LocalTransactionDataSource mLocalTransactionDataSource;
    LocalFacilityDatasource mLocalFacilityDatasource;
    LocalVulnerableDataSource mLocalEnrolDataSource;
    LocalFingerprintDataSource mLocalFingerprintDataSource;

    Application mApplication;

    public EnrolVulnerableViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        mLocalPINDataSource = new LocalPINDataSource(application);
        mLocalTransactionDataSource = new LocalTransactionDataSource(application);
        mLocalWardDataSource = new LocalWardDataSource(application);
        mLocalLGADataSource = new LocalLGADataSource(application);
        mLocalFacilityDatasource = new LocalFacilityDatasource(application);
        mLocalEnrolDataSource = new LocalVulnerableDataSource(application);
        mLocalFingerprintDataSource = new LocalFingerprintDataSource(application);
        mLocalBenefactorDataSource = new LocalBenefactorDataSource(application);
    }

    public LiveData<List<Facility>> getFacility(String lga, String ward) {
        return mLocalFacilityDatasource.getFacilityByLgaAndWard(lga, ward);
    }

    public LiveData<List<Facility>> getAllFacilities() {
        return mLocalFacilityDatasource.getAllFacilities();
    }

    public LiveData<Integer> getPinsUnused() {
        return mLocalPINDataSource.getUnusedPIN();
    }

    public LiveData<Integer> getPinsused() {
        return mLocalPINDataSource.getPINUsed();
    }

    public Maybe<Ward> getWardById() {
        return mLocalWardDataSource.getWardById((PrefUtils.getInstance(mApplication)).getWard());
    }

    public Maybe<LGA> getLGAById() {
        return mLocalLGADataSource.getLGA(PrefUtils.getInstance(mApplication).getLGA());
    }

    public LiveData<List<LGA>> getLGAs() {
        return mLocalLGADataSource.getAllLga();
    }

    public LiveData<List<Benefactor>> getBenefactor() {
        return mLocalBenefactorDataSource.getAllLga();
    }

//    public LiveData<List<Ward>> getWardsByLG(int lgaId) {
//        return mLocalWardDataSource.getWardByLga(lgaId);
//    }

    public Maybe<Reproductive> getVulnerableBYNIN(String nin) {
        return mLocalEnrolDataSource.getVulnerableByNIN(nin);
    }
    public Completable updateWardCount(int wardId) {
        return mLocalWardDataSource.updateWardCount(wardId);
    }
    public Maybe<Long> savePrincipal(Vulnerable enrollment) {
        return mLocalEnrolDataSource.insertOrUpdate(enrollment);
    }

    public LiveData<List<Vulnerable>> getPrincipals() {
        return mLocalEnrolDataSource.principals();
    }

    public Maybe<Long> updatePrincipal(Vulnerable enrollment) {
        return mLocalEnrolDataSource.insertOrUpdate(enrollment);
    }

    public Maybe<Long> saveFingerprint(Fingerprint fingerprint) {
        return mLocalFingerprintDataSource.insertFingerprint(fingerprint);
    }
//    public Transaction todayTransaction() {
//        Transaction todayLog = null;
//            todayLog = mLocalTransactionDataSource.getTodayLog();
//            if (todayLog == null) {
//                Transaction transaction = new Transaction(Util.todayDate());
//                long id = mLocalTransactionDataSource.insert(transaction);
//                if (id > 0) {
//                    todayLog = mLocalTransactionDataSource.getTodayLog();
//                }
//            }
//    }

    public Maybe<Long> updateTransaction(Transaction transaction) {
        return mLocalTransactionDataSource.insertOrUpdate(transaction);
    }

    public Maybe<Transaction> todayTransaction() {
        return mLocalTransactionDataSource.getTodayLog();
    }

    public LiveData<Transaction> getTransaction() {
        return mLocalTransactionDataSource.getLiveTodayLog();
    }

    public Maybe<Long> insertOrUpdateTransaction(Transaction transaction) {
        return mLocalTransactionDataSource.insertOrUpdate(transaction);
    }

    public Completable updateCount(Facility facility) {
        return mLocalFacilityDatasource.update(facility);
    }


    public LiveData<List<Ward>> getWardsByLG(String lgaId) {
        return mLocalWardDataSource.getWardByLga(lgaId);
    }

    public LiveData<List<Ward>> getAllWards() {
        return mLocalWardDataSource.getAllWards();
    }

    public Maybe<Long> saveNIN(Reproductive reproductive) {
        return mLocalEnrolDataSource.insertNIN(reproductive);
    }
}