//package com.nicare.ees.persistence.local.localdatasources;
//
//import android.content.Context;
//import android.os.AsyncTask;
//
//import androidx.lifecycle.LiveData;
//
//import com.nicare.ees.persistence.EESDatabase;
//
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//
//import io.reactivex.Completable;
//import io.reactivex.Maybe;
//
//public class LocalEnrolDataSource {
//
//    PrincipalEnrolledDAO mPrincipalEnrolledDAO;
//    EESDatabase mDatabase;
//
//    public LocalEnrolDataSource(Context application) {
//        mDatabase = EESDatabase.getInstance(application);
//        mPrincipalEnrolledDAO = mDatabase.getPrincipalEnrolledDAO();
//    }
//    public Completable delete(long id){
//
//         return mPrincipalEnrolledDAO.delete(id);
//
//    }
//    public Maybe<Long> insertOrUpdate(PrincipalEnrolled principalEnrolled) {
//        return mPrincipalEnrolledDAO.insert(principalEnrolled);
//    }
//
//    public LiveData<List<PrincipalEnrolled>> principals() {
//        return mPrincipalEnrolledDAO.getPrincipal();
//    }
//
////    public Maybe<List<PrincipalAndBiometric>> principalsList() {
////        return mPrincipalEnrolledDAO.getPrincipalsList();
////    }
//
//    public LiveData<PrincipalEnrolled> principal(long id) {
//        return mPrincipalEnrolledDAO.getPrincipal(id);
//    }
//
//    public List<PrincipalEnrolled> principalsList() throws ExecutionException, InterruptedException {
//        return new PrincipalsListAsync(mPrincipalEnrolledDAO).execute().get();
//    }
//    private static final class PrincipalsListAsync extends AsyncTask<Void, Void, List<PrincipalEnrolled>> {
//
//        PrincipalEnrolledDAO mPrincipalEnrolledDAO;
//
//        public PrincipalsListAsync(PrincipalEnrolledDAO principalEnrolledDAO) {
//            mPrincipalEnrolledDAO = principalEnrolledDAO;
//        }
//
//        @Override
//        protected List<PrincipalEnrolled> doInBackground(Void... args) {
//            return mPrincipalEnrolledDAO.getPrincipalsList();
//        }
//    }
//
//
//}
