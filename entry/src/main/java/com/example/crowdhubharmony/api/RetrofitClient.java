package com.example.crowdhubharmony.api;


import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final HiLogLabel HILOG_LABEL = new HiLogLabel(0, 0, "SplashAbilitySlice");
    public static Retrofit getInstance() {

        HttpLoggingInterceptor mHttpLoggingInterceptor = new HttpLoggingInterceptor();
        mHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient mOkHttpClient = new OkHttpClient
                .Builder()
                .addNetworkInterceptor(mHttpLoggingInterceptor)
                .addInterceptor(chain -> {
                    okhttp3.Request request = chain.request();
                    okhttp3.Request.Builder requestBuilder = request.newBuilder()
                            .addHeader("uid", "superadmin@email.com")
                            .addHeader("client", "8WWepBI1JOByN5Tjr6YPEQ")
                            .addHeader("Content-Type", "application/json")
                            .addHeader("access_token", "PUqGGSIc6dh3youdvJAgcQ");
                    okhttp3.Request modifiedRequest = requestBuilder.build();
                    okhttp3.Response response = chain.proceed(modifiedRequest);
                    HiLog.debug(HILOG_LABEL, "getClient: " + response.code());

                    return response;
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://crowdhub.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(mOkHttpClient)
                .build();
        return retrofit;
    }

}
