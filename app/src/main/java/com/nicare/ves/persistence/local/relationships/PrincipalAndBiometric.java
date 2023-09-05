//package com.nicare.ees.persistence.local.relationships;
//
//import androidx.room.Embedded;
//import androidx.room.Relation;
//
//import com.nicare.ees.persistence.local.databasemodels.enrolmentmodels.Fingerprint;
//
//public class PrincipalAndBiometric {
//
//    @Embedded
//    public PrincipalEnrolled principalEnrolled;
//    @Relation(
//            parentColumn = "id",
//            entityColumn = "beneficiary_id"
//    )
//    public Fingerprint fingerprint;
//
//
//
//    public Fingerprint getFingerprint() {
//        return fingerprint;
//    }
//
//    public void setFingerprint(Fingerprint fingerprint) {
//        this.fingerprint = fingerprint;
//    }
//}
