package com.nicare.ves.ui.enrol.vulnerable;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Vulnerable;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Facility;
import com.nicare.ves.persistence.local.localdatasources.LocalFacilityDatasource;
import com.nicare.ves.persistence.local.localdatasources.LocalVulnerableDataSource;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class PrincipalDetailViewModel extends AndroidViewModel {

    LocalVulnerableDataSource mTransactionRepository;
    LocalFacilityDatasource mLocalFacilityDatasource;

    Application mApplication;

    public PrincipalDetailViewModel(@NonNull Application application) {
        super(application);

        mApplication = application;
        mTransactionRepository = new LocalVulnerableDataSource(application);

        mLocalFacilityDatasource = new LocalFacilityDatasource(application);

    }


 

}
