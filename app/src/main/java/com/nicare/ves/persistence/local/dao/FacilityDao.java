package com.nicare.ves.persistence.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nicare.ves.persistence.local.databasemodels.utilmodels.Facility;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Dao

public interface FacilityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<Facility> facilities);

    @Query("SELECT * FROM facilities_table WHERE hcplga = :lga AND hcpward= :ward AND hcpcount<hcpcap")
    LiveData<List<Facility>> getFacilityByLgaAndWard(String lga, String ward);

    @Query("SELECT * FROM facilities_table WHERE hcplga = :lgaId AND hcpward = :wardId OR (hcpcode = :hcpCode)")
    LiveData<List<Facility>> getFacilityByLgaAndWardEdit(String lgaId, String wardId, String hcpCode);

    @Query("SELECT * FROM facilities_table WHERE hcpcode = :code ")
    Maybe<Facility> getFacilityCode(String code);

    @Update
    Completable updateCapCount(Facility facility);

    @Query("SELECT * FROM facilities_table")
    LiveData<List<Facility>> getAllFacilities();

}
