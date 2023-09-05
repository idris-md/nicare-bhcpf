package com.nicare.ves.persistence.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Fingerprint;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Dao
public interface FingerprintDao {

    @Insert
    Maybe<Long> insert(Fingerprint spouseFingerprint);

    @Update
    void update(Fingerprint spouseFingerprint);

    // @Query("SELECT * FROM fingerprints_table WHERE beneficiary_id =:userId AND beneficiary_type =:type")
    @Query("SELECT * FROM fingerprints_table WHERE beneficiary_id =:userId AND beneficiary_type =:type")
    Fingerprint fingerprintBySpouse(long userId, int type);

    @Query("SELECT * FROM fingerprints_table")
    LiveData<List<Fingerprint>> getAllFingerprints();

    @Query("DELETE FROM fingerprints_table WHERE beneficiary_id =:id AND beneficiary_type =:type")
    Completable delete(long id, int type);
}
