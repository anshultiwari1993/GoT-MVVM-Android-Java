package com.anshultiwari.androidassignment.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.anshultiwari.androidassignment.Utilities.Util;
import com.anshultiwari.androidassignment.ViewModel.CelebViewModel;
import com.anshultiwari.androidassignment.Model.Celebrity;
import com.anshultiwari.androidassignment.Adapter.CelebsAdapter;
import com.anshultiwari.androidassignment.Utilities.CenterZoomLayoutManager;
import com.anshultiwari.androidassignment.MyVolley;
import com.anshultiwari.androidassignment.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView mCelebsRecyclerView;
    private CelebsAdapter mCelebsAdapter;
    private List<Celebrity> mCelebsList;
    private ProgressBar mPb;
    private RequestQueue mRequestQueue;
    private Spinner mSpinner;
    private Toolbar mToolbar;

    private CelebViewModel mCelebViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCelebsList = new ArrayList<>();
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        // Setup views
        mCelebsRecyclerView = findViewById(R.id.celebs_rv);
        mPb = findViewById(R.id.pb);
        mSpinner = findViewById(R.id.sort_spinner);
        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        mCelebViewModel = ViewModelProviders.of(this).get(CelebViewModel.class);

        // Check for internet
        if (!Util.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();

        } else {
            mCelebViewModel.fetchCelebsAndStore();
        }

        mCelebViewModel.getAllCelebs().observe(this, new Observer<List<Celebrity>>() {
            @Override
            public void onChanged(List<Celebrity> celebrities) {
                Log.d(TAG, "onChanged: called");
                Log.d(TAG, "onChanged: celebrities size = " + celebrities.size());
                mCelebsList = celebrities;

                setupRecyclerView(celebrities);
                setupSortSpinner();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.grid: {
                mCelebsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
                mCelebsRecyclerView.setAdapter(mCelebsAdapter);
                return true;
            }

            case R.id.list: {
                mCelebsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
                mCelebsRecyclerView.setAdapter(mCelebsAdapter);
                return true;
            }

            case R.id.carousel: {
                mCelebsRecyclerView.setLayoutManager(new CenterZoomLayoutManager(this, RecyclerView.HORIZONTAL, false));
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }

    }

    private void setupRecyclerView(List<Celebrity> celebsList) {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
//        CenterZoomLayoutManager layoutManager = new CenterZoomLayoutManager(this, RecyclerView.HORIZONTAL, false);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mCelebsRecyclerView.setLayoutManager(layoutManager);
        mCelebsRecyclerView.setHasFixedSize(true);

        mCelebsAdapter = new CelebsAdapter(this, celebsList);
        mCelebsRecyclerView.setAdapter(mCelebsAdapter);
    }

    private void setupSortSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this,
                R.array.sort, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        sortByAge();
                        break;
                    case 1:
                        sortByHeight();
                        break;
                    case 2:
                        sortByPopularity();
                        break;
                }
                mCelebsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sortByAge() {
        Collections.sort(mCelebsList, new Comparator<Celebrity>() {
            @Override
            public int compare(Celebrity obj1, Celebrity obj2) {
                return obj1.getAge().compareToIgnoreCase(obj2.getAge()); // To sort by age in ascending order
            }
        });
    }

    private void sortByHeight() {
        Collections.sort(mCelebsList, new Comparator<Celebrity>() {
            @Override
            public int compare(Celebrity obj1, Celebrity obj2) {
                return obj1.getHeight().compareToIgnoreCase(obj2.getHeight()); // To sort by height in ascending order
            }
        });
    }

    private void sortByPopularity() {
        Collections.sort(mCelebsList, new Comparator<Celebrity>() {
            @Override
            public int compare(Celebrity obj1, Celebrity obj2) {
                return obj1.getPopularity().compareToIgnoreCase(obj2.getPopularity()); // To sort by height in ascending order
            }
        });
    }
}
