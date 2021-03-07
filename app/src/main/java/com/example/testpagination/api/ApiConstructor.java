package com.example.testpagination.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConstructor {

    public static <T> T CreateService(Class<T> serviceClass){

        Retrofit retrofit = new Retrofit.Builder ()
                .baseUrl ( ApiConfig.HOST_URL )
                .addConverterFactory ( GsonConverterFactory.create () )
                .build ();

        return retrofit.create ( serviceClass );
    }

}
