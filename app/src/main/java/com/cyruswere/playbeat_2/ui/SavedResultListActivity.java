package com.cyruswere.playbeat_2.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.cyruswere.playbeat_2.Constants;
import com.cyruswere.playbeat_2.R;
import com.cyruswere.playbeat_2.adapter.FirebaseResultsViewHolder;
import com.cyruswere.playbeat_2.models.Result;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedResultListActivity extends AppCompatActivity {

    private DatabaseReference mResultReference;
    private FirebaseRecyclerAdapter<Result, FirebaseResultsViewHolder> mFirebaseAdapter;

    @BindView(R.id.postRecyclerView) RecyclerView mRecyclerView;
    //@BindView(R.id.errorTextView) TextView mErrorTextView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ButterKnife.bind(this);

        mResultReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_RESULTS);
        setUpFirebaseAdapter();
        hideProgressBar();
        showResults();
    }

    private void setUpFirebaseAdapter(){
        FirebaseRecyclerOptions<Result> options =
                new FirebaseRecyclerOptions.Builder<Result>()
                        .setQuery(mResultReference, Result.class)
                        .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Result, FirebaseResultsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseResultsViewHolder firebaseResultViewHolder, int position, @NonNull Result result) {
                firebaseResultViewHolder.bindResult(result);
            }

            @NonNull
            @Override
            public FirebaseResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                return new FirebaseResultsViewHolder(view);
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mFirebaseAdapter!= null) {
            mFirebaseAdapter.stopListening();
        }
    }

    private void showResults() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

}