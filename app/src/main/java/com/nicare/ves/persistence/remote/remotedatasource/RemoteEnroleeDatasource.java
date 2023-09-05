package com.nicare.ves.persistence.remote.remotedatasource;

import android.content.Context;

import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.persistence.remote.NiCareClient;
import com.nicare.ves.persistence.remote.api.NiCareAPI;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.UploadResponse;
import com.nicare.ves.persistence.remote.apimodels.requestmodel.ReCaptureRequest;
import com.nicare.ves.persistence.remote.apimodels.requestmodel.VulnerableRequest;

import retrofit2.Call;

public class RemoteEnroleeDatasource {

    private Context mContext;
    private NiCareAPI mAPI;

    public RemoteEnroleeDatasource(Context context) {
        mContext = context;
        mAPI = NiCareClient.NiCareClient(mContext).create(NiCareAPI.class);
    }


    public Call<UploadResponse> uploadVulnerables(VulnerableRequest vulnerables) {
        String token = PrefUtils.getInstance(mContext).getToken();
        return mAPI.uploadVulnerable(vulnerables, "Bearer " + token);
    }

    public Call<UploadResponse> uploadRecaptures(ReCaptureRequest vulnerables) {
        String token = PrefUtils.getInstance(mContext).getToken();
        return mAPI.uploadRecapture(vulnerables, "Bearer " + token);
    }


}
