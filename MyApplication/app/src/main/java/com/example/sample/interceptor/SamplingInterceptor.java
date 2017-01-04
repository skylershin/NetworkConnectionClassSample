package com.example.sample.interceptor;

import android.util.Log;

import com.example.sample.MainActivity;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by MunkyuShin on 1/4/17.
 */

public class SamplingInterceptor implements Interceptor{
    private MainActivity.SamplingConnectionListener mListener;

    public SamplingInterceptor(MainActivity.SamplingConnectionListener listener) {
        mListener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        ConnectionClassManager.getInstance().register(mListener);
        Request request = chain.request();
        DeviceBandwidthSampler.getInstance().startSampling();
        Response response = chain.proceed(request);
        DeviceBandwidthSampler.getInstance().stopSampling();
        ConnectionQuality cq = ConnectionClassManager.getInstance().getCurrentBandwidthQuality();
        Log.d("cq", cq.name());
        return response;
    }
}
