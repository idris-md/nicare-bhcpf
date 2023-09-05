package com.nicare.ves.persistence.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nicare.ves.persistence.local.databasemodels.EOAuthModel;

import io.reactivex.Maybe;

@Dao
public interface AuthDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<Long> insertOrUpdate(EOAuthModel eoAuthModel);

    @Query("SELECT * FROM auth ORDER BY id DESC LIMIT 1 ")
    LiveData<EOAuthModel> getAuthModel();


}
