package com.nicare.ves.persistence.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nicare.ves.persistence.remote.apimodels.Report;
import com.nicare.ves.persistence.remote.apimodels.Transaction;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface TransactionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<Long> insert(Transaction transaction);


    @Query("SELECT * FROM activity_log_table WHERE date =:today")
    LiveData<Transaction> todayLiveActivity(String today);

    @Query("SELECT * FROM activity_log_table WHERE date =:today")
    Maybe<Transaction> todayActivity(String today);

    @Query("SELECT * FROM activity_log_table")
    LiveData<List<Transaction>> allActivity();

    @Query("SELECT count() FROM vulnerable_table")
    LiveData<Integer> allReports();
//
//    @Query("SELECT *," +
//            "(SELECT count() FROM vulnerable_table) as spouseCount," +
//            "(SELECT count() FROM child_dependent_table) as childCount," +
//            "(SELECT count() FROM uncaptured_beneficiaries_table where isCaptured = 1) as capturedCount" +
//            " FROM  (SELECT count() as principalCount FROM principal_table) as a ")
//    LiveData<Report> allReports();


}
