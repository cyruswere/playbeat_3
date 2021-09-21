package com.cyruswere.playbeat_2.network;

import com.cyruswere.playbeat_2.models.TrackResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("/search")
    Call<TrackResponse> getTracks(
            @Query("term") String term
    );
}
