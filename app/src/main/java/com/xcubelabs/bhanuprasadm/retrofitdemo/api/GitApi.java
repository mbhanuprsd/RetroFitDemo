package com.xcubelabs.bhanuprasadm.retrofitdemo.api;

import com.xcubelabs.bhanuprasadm.retrofitdemo.model.GitModel;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by bhanuprasadm on 15/10/15.
 */
public interface GitApi {
    @GET("/users/{user}")      //here is the other url part.best way is to start using /
    public void getFeed(@Path("user") String user, Callback<GitModel> response);     //string user is for passing values from edittext for eg: user=basil2style,google
    //response is the response from the server which is now in the POJO
}
