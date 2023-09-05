package com.nicare.ves.persistence;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.nicare.ves.common.Cryptography;
import com.nicare.ves.persistence.local.dao.AuthDao;
import com.nicare.ves.persistence.local.dao.BenefactorDao;
import com.nicare.ves.persistence.local.dao.FacilityDao;
import com.nicare.ves.persistence.local.dao.FingerprintDao;
import com.nicare.ves.persistence.local.dao.LGADao;
import com.nicare.ves.persistence.local.dao.PINDao;
import com.nicare.ves.persistence.local.dao.TransactionsDao;
import com.nicare.ves.persistence.local.dao.VulnerableDAO;
import com.nicare.ves.persistence.local.dao.WardDao;
import com.nicare.ves.persistence.local.databasemodels.Benefactor;
import com.nicare.ves.persistence.local.databasemodels.EOAuthModel;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Fingerprint;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Vulnerable;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Facility;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.LGA;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Premium;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Reproductive;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Ward;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.ReCapture;
import com.nicare.ves.persistence.remote.apimodels.Transaction;

@Database(entities = {Reproductive.class, EOAuthModel.class, Vulnerable.class, Benefactor.class,ReCapture.class, Fingerprint.class, Premium.class,
        LGA.class, Ward.class, Facility.class, Transaction.class},
        exportSchema = false,
        version = 3)

public abstract class HUWEDatabase extends RoomDatabase {
    //    static {
    //        System.loadLibrary("native-lib");
    //    }
    //    private static native String invokeNativeDBKey();
    private static HUWEDatabase instance;

    public abstract PINDao getPINDao();

    public abstract FacilityDao getProviderDao();

    public abstract BenefactorDao getBenefactorDAO();

    public abstract LGADao getLGADao();

    public abstract WardDao getWardDao();

    public abstract TransactionsDao getTransactionsDao();

    public abstract VulnerableDAO getVulnerableDAO();

    public abstract FingerprintDao getFingerprintDao();

    public abstract AuthDao getAuthDao();

    public static synchronized HUWEDatabase getInstance(Context context) {
        if (instance == null) {

            // SafeHelperFactory helperFactory = new SafeHelperFactory(invokeNativeDBKey().toCharArray());
            Cryptography cryptography = new Cryptography(context);
//            try {
//                char[] key = PrefUtils.getInstance(context).getXCVFRN().toCharArray();
//                SafeHelperFactory helperFactory = new SafeHelperFactory(cryptography.decryptData();

//            SafeHelperFactory helperFactory = new SafeHelperFactory("xcgvbnhbjh".toCharArray());
            instance = Room.databaseBuilder(context, HUWEDatabase.class, "HUWEDatabase")
//                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
//                        .openHelperFactory(helperFactory)
                    .build();
//            } catch (NoSuchPaddingException | NoSuchAlgorithmException | UnrecoverableEntryException | KeyStoreException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchProviderException | BadPaddingException | IllegalBlockSizeException | IOException | CertificateException e) {
//                e.printStackTrace();
//            }
        }

        return instance;

    }

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'reproductive' ( id INTEGER PRIMARY KEY NOT NULL , nicareId TEXT  , nin TEXT , name TEXT ,UNIQUE(nin) );");
            database.execSQL("ALTER TABLE 'vulnerable_table' ADD COLUMN 'guardianID' TEXT DEFAULT('');");
            database.execSQL("ALTER TABLE 'vulnerable_table' ADD COLUMN 'guardianNIN' TEXT DEFAULT('');");
        }
    };

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE 'recapture_table' ADD COLUMN 'issue_code' INTEGER NOT NULL default 0;");
            database.execSQL("ALTER TABLE 'vulnerable_table' ADD COLUMN 'community' TEXT ;");
//            database.execSQL("ALTER TABLE 'vulnerable_table' ADD COLUMN 'guardianId' TEXT ;");
//            database.execSQL("ALTER TABLE 'vulnerable_table' ADD COLUMN 'guardianNIN' TEXT ;");
        }
    };
}
