package com.nicare.ves.ui.enrol.vulnerable;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nicare.ves.persistence.HUWEDatabase;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Vulnerable;
import com.nicare.ves.persistence.local.localdatasources.LocalVulnerableDataSource;

import java.util.List;

public class VulnerableListViewModel extends AndroidViewModel {

    private LocalVulnerableDataSource mVulnerableDataSource;

    public VulnerableListViewModel(@NonNull Application application) {
        super(application);
        mVulnerableDataSource = new LocalVulnerableDataSource(application);
    }


    public LiveData<List<Vulnerable>> getAll() {
        return mVulnerableDataSource.principals();
    }
}
