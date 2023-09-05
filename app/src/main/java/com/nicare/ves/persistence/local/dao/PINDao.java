package com.nicare.ves.persistence.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nicare.ves.persistence.local.databasemodels.utilmodels.Premium;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Reproductive;
import com.nicare.ves.persistence.remote.apimodels.pojo.SoldPIN;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Dao
public interface PINDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<Reproductive> pins);

    @Query("SELECT * FROM nin_table WHERE nicareId = :pin LIMIT 1")
    Maybe<Reproductive> validate(String pin);

    @Query("SELECT * FROM premium_table WHERE sales_code = :code LIMIT 1")
    Maybe<Premium> pinbyCode(String code);

    @Query("SELECT count() FROM premium_table WHERE sales_status = 1 AND is_used = 0")
    LiveData<Integer> unUsedPins();

    @Query("SELECT count() FROM premium_table WHERE sales_status = 1 AND is_used = 1")
    LiveData<Integer> usedPins();

    @Query("SELECT pin, sales_status FROM premium_table WHERE sales_status =1")
    Maybe<List<SoldPIN>> getSoldPins();

    @Query("UPDATE premium_table set is_used=1 WHERE pin=:pin")
    Completable setPinAsUsed(String pin);

    @Query("UPDATE premium_table set sales_status=1, soldAt = :date WHERE sales_code =:salesCode")
    Completable setPinAsSold(String salesCode, String date);

    @Update
    Completable update(Premium arg);

}
