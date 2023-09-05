package com.nicare.ves.persistence.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nicare.ves.persistence.local.databasemodels.utilmodels.Ward;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Dao
public interface WardDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(List<Ward> wardList);

    @Query("SELECT * FROM wards_table WHERE lga_id = :lgaId")
    LiveData<List<Ward>> wardByLga(String lgaId);
    @Query("SELECT * FROM wards_table WHERE lga_id = :lgaId AND totalEnrolled>0")
    LiveData<List<Ward>> enroleableWardByLGA(int lgaId);

    @Query("UPDATE wards_table SET totalEnrolled = totalEnrolled+1 WHERE ward_id = :ward_id AND totalEnrolled>0")
    Completable updateTotalEnrolled(int ward_id);

    @Query("SELECT * FROM wards_table WHERE ward_id = :wardId")
    Maybe<Ward> wardById(String wardId);

    @Query("SELECT * FROM wards_table")
    LiveData<List<Ward>> getAllWards();

}
