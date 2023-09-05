package com.nicare.ves.di;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.RequestManager;
//import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.nicare.ves.BuildConfig;
import com.nicare.ves.R;
import com.nicare.ves.common.Constant;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.common.Util;
import com.nicare.ves.persistence.remote.apimodels.requestmodel.LoginRequest;
import com.nicare.ves.persistence.remote.apimodels.DeviceInfo;
import com.nicare.ves.persistence.remote.api.AuthApi;
import com.nicare.ves.persistence.remote.api.NiCareAPI;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
@Module
public class AppModule {

    @Singleton
    @Provides
    static PrefUtils providePrefUtils(Application application) {
        PrefUtils instance = PrefUtils.getInstance(application);
        return instance;
    }

    @Singleton
    @Provides
    static LoginRequest provideLoginRequest(Application application, DeviceInfo deviceInfo, PrefUtils prefUtils) {
        return new LoginRequest(PrefUtils.getInstance(application).getUser(), prefUtils.getPwd(), deviceInfo.getDeviceModel(), deviceInfo.getDeviceId(), deviceInfo.getDeviceIMEI(), BuildConfig.VERSION_CODE);
    }

//    @Singleton
//    @Provides
//    static LogoutRequest provideLogoutRequest(Application application, DeviceInfo deviceInfo, PrefUtils prefUtils) {
//        return new LogoutRequest(PrefUtils.getInstance(application).getUser(), prefUtils.getPwd(), deviceInfo.getDeviceModel(), deviceInfo.getDeviceId(), deviceInfo.getDeviceIMEI());
//    }

    @Singleton
    @Provides
    static AuthApi provideAuthApi(Retrofit retrofit) {
        return retrofit.create(AuthApi.class);
    }
    @Singleton
    @Provides
    static NiCareAPI provideNicareApi(Retrofit retrofit) {
        return retrofit.create(NiCareAPI.class);
    }

    @Singleton
    @Provides
    static Gson provideGsonFactory() {
        return new GsonBuilder()
                .serializeNulls()
                .setLenient()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @Singleton
    @Provides
    static HttpLoggingInterceptor providesHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

    }

    @Singleton
    @Provides
    static OkHttpClient providesOkhttpClient(Application application, HttpLoggingInterceptor httpLoggingInterceptor) {
        int REQUEST_TIMEOUT = 60;

        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);


        httpClient.addInterceptor(httpLoggingInterceptor);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json");

                // Adding Authorization token (API Key)
                // Requests will be denied without API key
                if (!TextUtils.isEmpty(PrefUtils.getInstance(application).getToken())) {
                    requestBuilder.addHeader("Authorization", PrefUtils.getInstance(application).getToken());
                }

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        if (Build.VERSION.SDK_INT <= 25) {
            try {

                TrustManagerFactory  tmf = Util.getTrustManagerFactory();
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, tmf.getTrustManagers(), null);
                httpClient.sslSocketFactory(context.getSocketFactory(), (X509TrustManager) tmf.getTrustManagers()[0]);

            } catch (NoSuchAlgorithmException | KeyStoreException | IOException | CertificateException | KeyManagementException e) {
                e.printStackTrace();
            }
        }
        return httpClient.build();

    }

    @Singleton
    @Provides
    static Retrofit providesRetrofit(Gson gson, OkHttpClient client) {

        return new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .baseUrl(Constant.BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }


//    @Provides
//    @Singleton
//    static RequestOptions provideRequestOptions() {
//        return RequestOptions.
//                placeholderOf(R.drawable.ic_launcher_background)
//                .error(R.drawable.ic_launcher_background);
//    }
//
//    @Provides
//    @Singleton
//    static RequestManager providesRequestManager(Application application, RequestOptions requestOptions) {
//        return Glide.with(application)
//                .setDefaultRequestOptions(requestOptions);
//    }

    @Provides
    @Singleton
    static Drawable providesDrawable(Application application) {
        return ContextCompat.getDrawable(application, R.drawable.logo);
    }

    @Provides
    static DeviceInfo providesDeviceInfo(Application application) {

        String mDeviceModel;
        String mDeviceManufacture;
        String mDeviceId;
        String mDeviceIMEI;

        TelephonyManager manager = (TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);

        mDeviceModel = Build.MODEL;
        mDeviceManufacture = Build.MANUFACTURER;
        mDeviceId = Settings.Secure.getString(application.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (ActivityCompat.checkSelfPermission(application, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
        }
        mDeviceIMEI = "manager.getDeviceId()";

        DeviceInfo device_verify = new DeviceInfo( mDeviceManufacture + " " + mDeviceModel, mDeviceId, mDeviceIMEI);

        return device_verify;
    }


}
