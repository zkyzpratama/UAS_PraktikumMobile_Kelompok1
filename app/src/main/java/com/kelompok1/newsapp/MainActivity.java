package com.kelompok1.newsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.color.MaterialColors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set initial fragment
        loadFragment(new AllCategoriesFragment());

        // Set click listeners for category buttons
        findViewById(R.id.btn_1).setOnClickListener(this); // GENERAL
        findViewById(R.id.btn_2).setOnClickListener(this); // BUSINESS
        findViewById(R.id.btn_3).setOnClickListener(this); // SPORTS
        findViewById(R.id.btn_4).setOnClickListener(this); // TECHNOLOGY
        findViewById(R.id.btn_5).setOnClickListener(this); // HEALTH
        findViewById(R.id.btn_6).setOnClickListener(this); // ENTERTAINMENT
        findViewById(R.id.btn_7).setOnClickListener(this); // SCIENCE
    }

    @Override
    public void onClick(View v) {
        // Handle button clicks
        int id = v.getId();
        if (id == R.id.btn_1) {
            loadFragment(new GeneralNewsFragment());
        } else if (id == R.id.btn_2) {
            loadFragment(new BusinessNewsFragment());
        } else if (id == R.id.btn_3) {
            loadFragment(new SportsNewsFragment());
        } else if (id == R.id.btn_4) {
            loadFragment(new TechnologyNewsFragment());
        } else if (id == R.id.btn_5) {
            loadFragment(new HealthNewsFragment());
        } else if (id == R.id.btn_6) {
            loadFragment(new EntertainmentNewsFragment());
        } else if (id == R.id.btn_7) {
            loadFragment(new ScienceNewsFragment());
        }
        // Add conditions for other buttons
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
