package com.nicare.ves.persistence.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Vulnerable;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Reproductive;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.ReCapture;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface VulnerableDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<Long> insert(Vulnerable vulnerable);

    @Update
    void update(Vulnerable vulnerable);

    @Query("SELECT * FROM vulnerable_table ")
    LiveData<List<Vulnerable>> getVulnerable();

    @Query("Select * from recapture_table order by recaptured asc")
    LiveData<List<ReCapture>> getRecaptures();

//    @Query("SELECT * FROM vulnerable_table")
//    Maybe<List<Vulnerable>> getVulnerablesList();

    @Query("SELECT * FROM vulnerable_table limit 50")
    List<Vulnerable> getVulnerablesList();

    @Query("SELECT * FROM nin_table where nin=:nin OR nicareId=:nin")
    Maybe<Reproductive> getVulnerableByNIN(String nin);

    @Query("SELECT * FROM recapture_table where recaptured = 1 limit 50")
    List<ReCapture> getRecapturedList();

    @Query("SELECT * FROM vulnerable_table limit 20")
    Single<List<Vulnerable>> getVulnerablesData();

    @Query("SELECT * FROM vulnerable_table where id =:id")
    LiveData<Vulnerable> getVulnerable(long id);

    @Query("DELETE FROM vulnerable_table WHERE id =:id")
    Completable delete(long id);

    @Query("SELECT count() FROM vulnerable_table")
    LiveData<Integer> allDataCount();

    @Query("SELECT * FROM recapture_table where nicareId =:id")
    LiveData<ReCapture> getRecaptureUser(String id);

    @Update
    Completable updateRecapture(ReCapture reCapture);

    @Query("DELETE from recapture_table where id =:id")
    Completable deleteRecapture(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertRecaptures(List<ReCapture> recaptures);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<Long> insertNIN(Reproductive reproductive);
}
