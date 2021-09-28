package com.cyruswere.playbeat_2.ui;

import static com.cyruswere.playbeat_2.network.TracksClient.getRetrofitClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.cyruswere.playbeat_2.Constants;
import com.cyruswere.playbeat_2.R;
import com.cyruswere.playbeat_2.adapter.TrackListAdapter;
import com.cyruswere.playbeat_2.models.Result;
import com.cyruswere.playbeat_2.models.TrackResponse;
import com.cyruswere.playbeat_2.network.ApiInterface;
import com.cyruswere.playbeat_2.network.TracksClient;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;

public class ResultsActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mRecentKeyWords;

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

        if (mRecentKeyWords != null) {
            fetchPosts(mRecentKeyWords);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();


        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchKeyWord) {
                addToSharedPreferences(searchKeyWord);
                fetchPosts(searchKeyWord);
                return false;
            }


            @Override
            public boolean onQueryTextChange(String searchKeyWord) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    private void fetchPosts(String term){
        progressBar.setVisibility(View.VISIBLE);
        TracksClient.getRetrofitClient().getTracks(term).enqueue(new Callback<TrackResponse>() {
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

    private void addToSharedPreferences(String keyWord) {
        mEditor.putString(Constants.PREFERENCES_RESULT_KEY, keyWord).apply();
    }
}