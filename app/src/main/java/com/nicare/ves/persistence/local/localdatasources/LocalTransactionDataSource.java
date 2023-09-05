package com.nicare.ves.persistence.local.localdatasources;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.nicare.ves.common.Util;
import com.nicare.ves.persistence.HUWEDatabase;
import com.nicare.ves.persistence.remote.apimodels.Report;
import com.nicare.ves.persistence.remote.apimodels.Transaction;
import com.nicare.ves.persistence.local.dao.TransactionsDao;

import java.util.List;

import io.reactivex.Maybe;

public class LocalTransactionDataSource {

    TransactionsDao mTransactionsDao;
    HUWEDatabase mDatabase;

    public LocalTransactionDataSource(Context application) {
        mDatabase = HUWEDatabase.getInstance(application);
        mTransactionsDao = mDatabase.getTransactionsDao();
    }

    public Maybe<Long> insertOrUpdate(Transaction transaction) {
        return mTransactionsDao.insert(transaction);
    }

    public Maybe<Transaction> getTodayLog() {
        return mTransactionsDao.todayActivity(Util.todayDate());
    }

    public LiveData<Transaction> getLiveTodayLog() {
        return mTransactionsDao.todayLiveActivity(Util.todayDate());
    }

    public LiveData<List<Transaction>> getTransactions() {
        return mTransactionsDao.allActivity();
    }

    public LiveData<Integer> allReports() {
        return mTransactionsDao.allReports();
    }

}