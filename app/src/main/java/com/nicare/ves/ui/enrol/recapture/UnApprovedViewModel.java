package com.nicare.ves.ui.enrol.recapture;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nicare.ves.persistence.remote.apimodels.ApiResponse.ReCapture;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Ward;
import com.nicare.ves.persistence.local.localdatasources.LocalVulnerableDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalWardDataSource;

import java.util.List;

public class UnApprovedViewModel extends AndroidViewModel {
    private LocalVulnerableDataSource mVulnerableDataSource;
    private LocalWardDataSource mLocalWardDataSource;

    public UnApprovedViewModel(@NonNull Application application) {
        super(application);
        mVulnerableDataSource = new LocalVulnerableDataSource(application);
        mLocalWardDataSource = new LocalWardDataSource(application);
    }

    public LiveData<List<ReCapture>> getAll() {
        return mVulnerableDataSource.recaptures();
    }

    public LiveData<List<Ward>> getAllWards() {
        return mLocalWardDataSource.getAllWards();
    }

}
