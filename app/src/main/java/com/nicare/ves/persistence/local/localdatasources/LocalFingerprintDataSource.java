package com.nicare.ves.persistence.local.localdatasources;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.nicare.ves.persistence.HUWEDatabase;
import com.nicare.ves.persistence.local.dao.FingerprintDao;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Fingerprint;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Vulnerable;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class LocalFingerprintDataSource {

    FingerprintDao mFingerprintDAO;
    HUWEDatabase mDatabase;

    public LocalFingerprintDataSource(Context application) {
        mDatabase = HUWEDatabase.getInstance(application);
        mFingerprintDAO = mDatabase.getFingerprintDao();
    }

    public Completable delete(long id, int type) {
        return mFingerprintDAO.delete(id, type);
    }

    public Maybe<Long> insertFingerprint(Fingerprint fingerprint) {
        return mFingerprintDAO.insert(fingerprint);
    }

    //    public Maybe<Fingerprint> getFingerprint(long userId, int type) {
//        return mFingerprintDAO.fingerprintBySpouse(userId, type);
//    }
    public LiveData<List<Fingerprint>> getAllFingerprints() {
     return  mFingerprintDAO.getAllFingerprints();
    }

    public Fingerprint getFingerprint(long userId, int type) throws ExecutionException, InterruptedException {
        return new GetFingerprintAsync(mFingerprintDAO, type).execute(userId).get();
    }

    private static final class GetFingerprintAsync extends AsyncTask<Long, Void, Fingerprint> {

        FingerprintDao mFingerprintDAO;
        int type;

        public GetFingerprintAsync(FingerprintDao principalEnrolledDAO, int userType) {
            mFingerprintDAO = principalEnrolledDAO;
            type = userType;
        }

        @Override
        protected Fingerprint doInBackground(Long... args) {
            return mFingerprintDAO.fingerprintBySpouse(args[0], type);
        }
    }


}
