package com.nicare.ves.persistence.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nicare.ves.persistence.local.databasemodels.Benefactor;
import java.util.List;
import io.reactivex.Completable;

@Dao
public interface BenefactorDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(List<Benefactor> lgaList);

    @Query("SELECT * FROM benefactor")
    LiveData<List<Benefactor>> getBenefactor();


}
