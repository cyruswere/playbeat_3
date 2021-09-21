package com.cyruswere.playbeat_2.ui;

import static com.cyruswere.playbeat_2.network.TracksClient.getRetrofitClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cyruswere.playbeat_2.R;
import com.cyruswere.playbeat_2.adapter.TrackListAdapter;
import com.cyruswere.playbeat_2.models.Result;
import com.cyruswere.playbeat_2.models.TrackResponse;
import com.cyruswere.playbeat_2.network.ApiInterface;
import com.cyruswere.playbeat_2.network.TracksClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;

public class ResultsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager layoutManager;
    TrackListAdapter adapter;
    List<Result> resultList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        recyclerView = findViewById(R.id.postRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        layoutManager = new LinearLayoutManager(this);
        Log.e("TAG", "We got here");
        recyclerView.setLayoutManager(layoutManager);
        Intent intent = getIntent();
        String TypedSearchKeyWOrd = intent.getStringExtra("TypedSearchKeyWOrd");

        fetchPosts(TypedSearchKeyWOrd);

    }
    private void fetchPosts(String term){
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface clients=getRetrofitClient();
        Call <TrackResponse> call=clients.getTracks(term);
        call.enqueue(new Callback<TrackResponse>() {
            @Override
            public void onResponse(Call<TrackResponse> call, Response<TrackResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    resultList = response.body().getResults();

                    progressBar.setVisibility(View.GONE);
                    adapter = new TrackListAdapter(ResultsActivity.this,resultList);
                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                }else{
                    showUnsuccessfulMessage();
                }

            }

            @Override
            public void onFailure(Call<TrackResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ResultsActivity.this,"Error "+ t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void showUnsuccessfulMessage() {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(ResultsActivity.this,"Something went wrong, please make another search, or try later",Toast.LENGTH_SHORT).show();

    }
}