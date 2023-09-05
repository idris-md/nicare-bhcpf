package com.nicare.ves.persistence.local.localdatasources;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.nicare.ves.persistence.HUWEDatabase;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Reproductive;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.ReCapture;
import com.nicare.ves.persistence.local.dao.VulnerableDAO;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Vulnerable;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class LocalVulnerableDataSource {

    VulnerableDAO mVulnerableDAO;

    HUWEDatabase mDatabase;

    public LocalVulnerableDataSource(Context application) {
        mDatabase = HUWEDatabase.getInstance(application);
        mVulnerableDAO = mDatabase.getVulnerableDAO();
    }

    public Maybe<Reproductive> getVulnerableByNIN(String nin){
        return  mVulnerableDAO.getVulnerableByNIN(nin);
    }
    public Maybe<Long> insertOrUpdate(Vulnerable vulnerable) {
        return mVulnerableDAO.insert(vulnerable);
    }

    public LiveData<Integer> allData() {
        return mVulnerableDAO.allDataCount();
    }

    public LiveData<ReCapture> getRecaptureUser(String id) {
        return mVulnerableDAO.getRecaptureUser(id);
    }


    public LiveData<List<Vulnerable>> principals() {
        return mVulnerableDAO.getVulnerable();
    }

    public LiveData<List<ReCapture>> recaptures() {
        return mVulnerableDAO.getRecaptures();
    }
//    public Maybe<List<Vulnerable>> principalsList()  {
//        return mVulnerableDAO.getVulnerablesList();
//    }

    public LiveData<Vulnerable> principal(long id) {
        return mVulnerableDAO.getVulnerable(id);
    }

    public List<Vulnerable> principalsList() {
        return mVulnerableDAO.getVulnerablesList();
    }

    public List<ReCapture> getReCaptureList() {
        return mVulnerableDAO.getRecapturedList();
    }

    public Single<List<Vulnerable>> getAllVulnerableData() {
        return mVulnerableDAO.getVulnerablesData();
    }

    public Completable delete(long id) {

        return mVulnerableDAO.delete(id);

    }

    public Completable updateRecapture(ReCapture reCapture) {
       return mVulnerableDAO.updateRecapture(reCapture);
    }

    public Completable deleteRecapture(long id) {
        return mVulnerableDAO.deleteRecapture(id);
    }

    public Completable insertRecaptures(List<ReCapture> recaptures) {
        return mVulnerableDAO.insertRecaptures(recaptures);
    }

    public Maybe<Long> insertNIN(Reproductive reproductive) {
        return mVulnerableDAO.insertNIN(reproductive);

    }

    private static final class PrincipalsListAsync extends AsyncTask<Void, Void, List<Vulnerable>> {

        VulnerableDAO mPrincipalEnrolledDAO;

        public PrincipalsListAsync(VulnerableDAO principalEnrolledDAO) {
            mPrincipalEnrolledDAO = principalEnrolledDAO;
        }

        @Override
        protected List<Vulnerable> doInBackground(Void... args) {
            return mPrincipalEnrolledDAO.getVulnerablesList();
        }
    }
}
