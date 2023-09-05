package com.nicare.ves.ui.enrol.recapture;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nicare.ves.persistence.remote.apimodels.ApiResponse.ReCapture;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Fingerprint;
import com.nicare.ves.persistence.local.localdatasources.LocalFingerprintDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalVulnerableDataSource;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class ReCaptureViewModel extends AndroidViewModel {

    private final LocalVulnerableDataSource mLocalEnrolDataSource;
    private final LocalFingerprintDataSource mLocalFingerprintDataSource;

    public ReCaptureViewModel(@NonNull Application application) {
        super(application);
        mLocalEnrolDataSource = new LocalVulnerableDataSource(application);
        mLocalFingerprintDataSource = new LocalFingerprintDataSource(application);
    }

    public LiveData<ReCapture> getUser(String id) {
        return mLocalEnrolDataSource.getRecaptureUser(id);
    }

    public Completable save(ReCapture reCapture) {
       return mLocalEnrolDataSource.updateRecapture(reCapture);
    }

    public Maybe<Long> saveFingerprint(Fingerprint fingerprint) {
        return mLocalFingerprintDataSource.insertFingerprint(fingerprint);
    }
}
