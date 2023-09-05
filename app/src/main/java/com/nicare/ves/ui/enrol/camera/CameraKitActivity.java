package com.nicare.ves.ui.enrol.camera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.nicare.ves.R;
import com.nicare.ves.common.Util;
import com.nicare.ves.interfaces.ImageResultCallBack;
import com.nicare.ves.persistence.ImageProcessResult;
import com.nicare.ves.vision.facedetection.GraphicOverlay;
import com.nicare.ves.vision.facedetection.facedetector.FaceGraphic;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListenerAdapter;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

//import com.google.firebase.ml.vision.FirebaseVision;
//import com.google.firebase.ml.vision.common.FirebaseVisionImage;
//import com.google.firebase.ml.vision.face.FirebaseVisionFace;
//import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
//import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

public class CameraKitActivity extends AppCompatActivity implements ImageResultCallBack {
    public static final int PASSPORT_WIDTH = 120;
    public static final int PASSPORT_HEIGHT = 120;
    //    static {
////        System.loadLibrary("jpge");
////        System.loadLibrary("jpgd");
////        System.loadLibrary("JniYuvOperator");
//        System.loadLibrary("JniBitmapOperator");
//    }

    private Integer mImageMaxWidth;
    // Max height (portrait mode)
    private Integer mImageMaxHeight;

    /**
     * Number of results to show in the UI.
     */
    private static final int RESULTS_TO_SHOW = 3;


    public static final String IS_FINGER = "FINGER";
    @BindView(R.id.camera_preview)
    CameraView cameraPreview;
    @BindView(R.id.graphic_overlay)
    GraphicOverlay mGraphicOverlay;
    @BindView(R.id.button_reset)
    ImageView buttonReset;
    @BindView(R.id.button_capture)
    ImageView buttonCapture;
    @BindView(R.id.button_save)
    ImageView buttonSave;
    AlertDialog mAlertDialog;
    boolean isFinger = false;
    private Bitmap mBitmap;
    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.leftEye)
    ImageView leftEye;
    @BindView(R.id.rightEye)
    ImageView rightEye;
    @BindView(R.id.leftEar)
    ImageView leftEar;
    @BindView(R.id.rightEar)
    ImageView rightEar;
    @BindView(R.id.smiling)
    ImageView smiling;

    @BindView(R.id.rightCheek)
    ImageView rightCheek;
    @BindView(R.id.leftCheek)
    ImageView leftCheek;

    @BindView(R.id.resultTable)
    TableLayout resultTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_kit);
        ButterKnife.bind(this);

        mAlertDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setCancelable(false)
                .setMessage("Please wait, Processing...")
                .build();

        readintentStateValue();

        initCamera();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    private void readintentStateValue() {
        isFinger = getIntent().getBooleanExtra(IS_FINGER, false);
    }

    private void initCamera() {
        cameraPreview.addCameraKitListener(new CameraKitEventListenerAdapter() {
            @Override
            public void onEvent(CameraKitEvent event) {
//                String hhh = event.getType();
                super.onEvent(event);


            }

            @Override
            public void onError(CameraKitError error) {
                super.onError(error);
            }

            @Override
            public void onImage(CameraKitImage image) {

                mBitmap = Bitmap.createScaledBitmap(image.getBitmap(), cameraPreview.getWidth(), cameraPreview.getHeight(), false);

//                if (event.getType() == CameraKitEvent.TYPE_IMAGE_CAPTURED) {
                cameraPreview.stop();
//                }

                Log.e("SIZE", mBitmap.getWidth() + " " + mBitmap.getHeight());
                if (isFinger) {
                    enableSave(true);
                    enableReset(true);
                } else {
//                    mAlertDialog.show();
                    runFaceContourDetection();
                }

            }

            @Override
            public void onVideo(CameraKitVideo video) {
                super.onVideo(video);
            }
        });
    }

//    private void runFaceDetection(Bitmap bitmap) {
//        FirebaseVisionImage visionImage = FirebaseVisionImage.fromBitmap(bitmap);
//        FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions.Builder()
//                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
//                .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
//                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
//                .build();
//
//        FirebaseVisionFaceDetector faceDetector = FirebaseVision.getInstance().getVisionFaceDetector(options);
//        faceDetector.detectInImage(visionImage)
//                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
//                    @Override
//                    public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
//                        processFaceResult(firebaseVisionFaces);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        e.printStackTrace();
//                        mAlertDialog.dismiss();
//                    }
//                });
//
//
//    }
//
//    private void processFaceResult(List<FirebaseVisionFace> firebaseVisionFaces) {
//
//        int faceCount = 0;
//
//        for (FirebaseVisionFace face : firebaseVisionFaces) {
//            Rect bound = face.getBoundingBox();
//            RectOverlay rectOverlay = new RectOverlay(graphicOverlay, bound);
//            graphicOverlay.add(rectOverlay);
//        }
//
//        faceCount = firebaseVisionFaces.size();
//
//
//        if (faceCount == 1) {
//            ///////enable save button
//            enableSave(buttonSave, true);
//            enableReset(buttonReset, true);
//
//        } else {
//            //////// diable save button
//            enableSave(buttonSave, false);
//            enableReset(buttonReset, true);
//        }
//
//        mAlertDialog.dismiss();
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        cameraPreview.start();
//        ena bleReset(buttonReset, false);
//        enableSave(buttonSave, false);
//        enableSave(buttonCapture, true);
        reset();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraPreview.stop();
    }

    @OnClick(R.id.button_capture)
    void capture() {
        enableCapture(false);
        cameraPreview.captureImage();
    }

    @SuppressLint("StaticFieldLeak")
    @OnClick(R.id.button_save)
    void save() {


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
//                Log.e("SIZE Before", mBitmap.getWidth() + " " + mBitmap.getHeight());
//                mBitmap = Bitmap.createScaledBitmap(mBitmap, (int) (mBitmap.getWidth() * 0.7), (int) (mBitmap.getHeight() * 0.10), true);
//                mBitmap = Bitmap.createScaledBitmap(mBitmap, 68, 105, true);
                mBitmap = Bitmap.createScaledBitmap(mBitmap, PASSPORT_WIDTH, PASSPORT_HEIGHT, true);
                Log.e("SIZE After", mBitmap.getWidth() + " " + mBitmap.getHeight());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                String value = Util.encodeBitmap(mBitmap);
                Log.i("OMK",value);
                intent.putExtra("photo", value);
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        }.execute();

    }

    @OnClick(R.id.button_reset)
    void reset() {
        mGraphicOverlay.clear();
        resultTable.setVisibility(View.INVISIBLE);

        cameraPreview.start();
        enableReset(false);
        enableCapture(true);
        enableSave(false);
        mBitmap = null;
    }

    private void enableSave(boolean b) {
        buttonSave.setEnabled(b);
        if (b) {
            ImageViewCompat.setImageTintList(buttonSave, ColorStateList.valueOf(getResources().getColor(R.color.white)));
        } else {
            ImageViewCompat.setImageTintList(buttonSave, ColorStateList.valueOf(getResources().getColor(R.color.gray)));
        }
    }

    private void enableReset(boolean b) {
        buttonReset.setEnabled(b);
        if (b) {
            ImageViewCompat.setImageTintList(buttonReset, ColorStateList.valueOf(getResources().getColor(R.color.light_red)));
        } else {
            ImageViewCompat.setImageTintList(buttonReset, ColorStateList.valueOf(getResources().getColor(R.color.gray)));
        }
    }

    private void enableCapture(boolean b) {
        buttonCapture.setEnabled(b);
        if (b) {
            ImageViewCompat.setImageTintList(buttonCapture, ColorStateList.valueOf(getResources().getColor(R.color.white)));
        } else {
            ImageViewCompat.setImageTintList(buttonCapture, ColorStateList.valueOf(getResources().getColor(R.color.gray)));
        }
    }


    private void runFaceContourDetection() {
//        imageView.setVisibility(View.VISIBLE);
        InputImage image = InputImage.fromBitmap(mBitmap, 0);
        FaceDetectorOptions options =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .setMinFaceSize(0.8f)
                        .build();

//        mFaceButton.setEnabled(false);
        FaceDetector detector = FaceDetection.getClient(options);
        detector.process(image)
                .addOnSuccessListener(
                        new OnSuccessListener<List<Face>>() {
                            @Override
                            public void onSuccess(List<Face> faces) {
//                                mFaceButton.setEnabled(true);

                                processFaceContourDetectionResult(faces);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
//                                mFaceButton.setEnabled(true);
                                e.printStackTrace();
                                enableReset(true);
                            }
                        });

    }

    private void processFaceContourDetectionResult(List<Face> faces) {
        // Task completed successfully
        if (faces.size() == 0) {
            showToast("No face found");
//            enableSave(true);
            enableReset(true);
            return;
        }
        mGraphicOverlay.clear();
        for (int i = 0; i < faces.size(); ++i) {
            Face face = faces.get(i);
            FaceGraphic graphic = new FaceGraphic(mGraphicOverlay, face);
            graphic.setImageResultCallBack(this);
            mGraphicOverlay.add(graphic);
            float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
            float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees

            Log.e("rotY", "" + rotY);
            Log.e("rotZ", "" + rotZ);
//            FaceLandmark leftEar = face.getLandmark(FaceLandmark.LEFT_EAR);
//            if (leftEar != null) {
//                PointF leftEarPos = leftEar.getPosition();
//                Toast.makeText(this, "Left", Toast.LENGTH_SHORT).show();
//
//            }
//            FaceLandmark rightEar = face.getLandmark(FaceLandmark.RIGHT_EAR);
//            if (rightEar != null) {
//                PointF leftEarPos = rightEar.getPosition();
//                Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
//            }

//            faceGraphic.updateFace(face);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Functions for loading images from app assets.

    // Returns max image width, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
    private Integer getImageMaxWidth() {
        if (mImageMaxWidth == null) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time image
            // rendering time.
            mImageMaxWidth = imageView.getWidth();
        }

        return mImageMaxWidth;
    }

    // Returns max image height, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
    private Integer getImageMaxHeight() {
        if (mImageMaxHeight == null) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time image
            // rendering time.
            mImageMaxHeight =
                    imageView.getHeight();
        }

        return mImageMaxHeight;
    }

    // Gets the targeted width / height.
    private Pair<Integer, Integer> getTargetedWidthHeight() {
        int targetWidth;
        int targetHeight;
        int maxWidthForPortraitMode = getImageMaxWidth();
        int maxHeightForPortraitMode = getImageMaxHeight();
        targetWidth = maxWidthForPortraitMode;
        targetHeight = maxHeightForPortraitMode;
        return new Pair<>(targetWidth, targetHeight);
    }


    @Override
    public void imageResultReady(ImageProcessResult result) {
        resultTable.setVisibility(View.VISIBLE);
        if (result != null) {

            if (result.isLeftEyeOpen()) {
                leftEye.setImageDrawable(getDrawable(R.drawable.ic_baseline_done_24));
            } else {
                leftEye.setImageDrawable(getDrawable(R.drawable.ic_baseline_clear_24));
            }
            if (result.isRightEyeOpen()) {
                rightEye.setImageDrawable(getDrawable(R.drawable.ic_baseline_done_24));
            } else {
                rightEye.setImageDrawable(getDrawable(R.drawable.ic_baseline_clear_24));
            }
            if (result.isLeftEarOpen()) {
                leftEar.setImageDrawable(getDrawable(R.drawable.ic_baseline_done_24));
            } else {
                leftEar.setImageDrawable(getDrawable(R.drawable.ic_baseline_clear_24));
            }
            if (result.isRightEarOpen()) {
                rightEar.setImageDrawable(getDrawable(R.drawable.ic_baseline_done_24));
            } else {
                rightEar.setImageDrawable(getDrawable(R.drawable.ic_baseline_clear_24));
            }
            if (result.isSmiling()) {
                smiling.setImageDrawable(getDrawable(R.drawable.ic_baseline_done_24));
            } else {
                smiling.setImageDrawable(getDrawable(R.drawable.ic_baseline_clear_24));
            }

            if (result.isLeftCheek()) {
                leftCheek.setImageDrawable(getDrawable(R.drawable.ic_baseline_done_24));
            } else {
                leftCheek.setImageDrawable(getDrawable(R.drawable.ic_baseline_clear_24));
            }

            if (result.isRightCheek()) {
                rightCheek.setImageDrawable(getDrawable(R.drawable.ic_baseline_done_24));
            } else {
                rightCheek.setImageDrawable(getDrawable(R.drawable.ic_baseline_clear_24));
            }

//            if (result.isLeftEyeOpen() && result.isRightEyeOpen()){
            enableSave(true);
            enableReset(true);

        } else {
            enableReset(true);
        }
    }

}
