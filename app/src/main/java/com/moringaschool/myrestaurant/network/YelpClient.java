package com.moringaschool.myrestaurant.network;

import com.moringaschool.myrestaurant.models.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YelpClient {

    private static Retrofit retrofit = null;

    public static synchronized YelpApi getClient() {

        if (retrofit == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            Interceptor interceptor1 = new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request newRequest  = chain.request().newBuilder()
                                    .addHeader("Authorization", "Bearer " + "b3DAtEi7MimM3NfdE4OuKromHFxMCjmyk95qKvuUlo603FZdvVheXjC1KU2FME5k1suyd2uFdkNsC6RnHad1Y4fbOjXABgr_SMrU9o0hrQ10mYE5zIKWx3_PYX_LXnYx")
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    };

            OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(interceptor1);

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.YELP_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client.build())
                    .build();
        }

        return retrofit.create(YelpApi.class);
    }
}