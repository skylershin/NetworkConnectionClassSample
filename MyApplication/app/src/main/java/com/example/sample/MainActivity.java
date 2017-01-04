package com.example.sample;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sample.dto.Repository;
import com.example.sample.interceptor.SamplingInterceptor;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private final OkHttpClient mClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(new StethoInterceptor())
            .addNetworkInterceptor(new SamplingInterceptor(new SamplingConnectionListener()))
            .build();

    private final Gson mGson = new Gson();

    public class SamplingConnectionListener implements ConnectionClassManager.ConnectionClassStateChangeListener {
        @Override
        public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
            switch (bandwidthState) {
                case POOR: {
                    Log.d("Connection Quality", "POOR");
                }
                case MODERATE: {
                    Log.d("Connection Quality", "MODERATE");
                }
                case GOOD: {
                    Log.d("Connection Quality", "GOOD");
                }
                case EXCELLENT: {
                    Log.d("Connection Quality", "EXCELLENT");
                }
                case UNKNOWN: {
                    Log.d("Connection Quality", "UNKNOWN");
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button requestButton = (Button) findViewById(R.id.button);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestGithubRepositories();
            }
        });
    }

    private void requestGithubRepositories() {
        Request request = new Request.Builder()
                .url("https://api.github.com/users/skylershin/repos")
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                Type listType = new TypeToken<List<Repository>>() {
                }.getType();

                final List<Repository> repositories = mGson.fromJson(response.body().charStream(), listType);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("Repositories", repositories.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
