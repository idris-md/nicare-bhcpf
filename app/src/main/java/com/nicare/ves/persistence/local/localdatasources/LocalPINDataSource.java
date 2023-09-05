package com.nicare.ves.persistence.local.localdatasources;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.nicare.ves.common.Util;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Premium;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Reproductive;
import com.nicare.ves.persistence.remote.apimodels.pojo.SoldPIN;
import com.nicare.ves.persistence.HUWEDatabase;
import com.nicare.ves.persistence.local.dao.PINDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class LocalPINDataSource {

    PINDao mPINDao;

    HUWEDatabase mDatabase;

    public LocalPINDataSource(Context application) {
        mDatabase = HUWEDatabase.getInstance(application);
        mPINDao = mDatabase.getPINDao();
    }

    public Completable insertOrUpdate(List<Reproductive> pins) {
        return mPINDao.insert(pins);
    }

    public Completable update(String pin) {
        return mPINDao.setPinAsUsed(pin);
    }

    public Completable setPinasSold(String saleCode) {
        return mPINDao.setPinAsSold(saleCode, Util.dateTimeString());
    }

    public Completable setPinasUsed(String pin) {
        return mPINDao.setPinAsUsed(pin);
    }

    public LiveData<Integer> getUnusedPIN() {
        return mPINDao.unUsedPins();
    }

    public LiveData<Integer> getPINUsed() {
        return mPINDao.usedPins();
    }

    public Maybe<Reproductive> getReproductive(String pin)  {
        return mPINDao.validate(pin);
    }

    public Maybe<List<SoldPIN>> getSoldPINs() {
        return mPINDao.getSoldPins();
    }

    public Maybe<Premium> getPINByCode(String pin) {
        return mPINDao.pinbyCode(pin);
    }

    public Completable updatePIN(Premium pin) {
        return mPINDao.update(pin);
    }

}
