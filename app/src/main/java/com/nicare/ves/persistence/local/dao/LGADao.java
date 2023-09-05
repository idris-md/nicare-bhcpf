package com.nicare.ves.persistence.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nicare.ves.persistence.remote.apimodels.Address;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.LGA;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Dao
public interface LGADao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(List<LGA> lgaList);

    @Query("SELECT * FROM lga_table")
    LiveData<List<LGA>> getLGAs();

    @Query("SELECT * FROM lga_table where lga_table.lgaId =:lgaId")
    Maybe<LGA> getLGAById(String lgaId);

    @Query("SELECT lga_table.name as lga, wards_table.name as ward FROM lga_table, wards_table where lga_table.lgaId =:lgaId AND wards_table.ward_id = :wardId")
    Maybe<Address> getAddress(String lgaId, String wardId);

}
