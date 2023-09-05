package com.nicare.ves.ui.enrol.fingerprint;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Fingerprint;
import com.nicare.ves.persistence.local.localdatasources.LocalFingerprintDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalVulnerableDataSource;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.ReCapture;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class HuafanViewModel extends AndroidViewModel {

    private final LocalFingerprintDataSource mLocalFingerprintDataSource;

    public HuafanViewModel(@NonNull Application application) {
        super(application);
        mLocalFingerprintDataSource = new LocalFingerprintDataSource(application);
    }

    public LiveData<List<Fingerprint>> getAllFingerprints() {
        return mLocalFingerprintDataSource.getAllFingerprints();
    }

}
