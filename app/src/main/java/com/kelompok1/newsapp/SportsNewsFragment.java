package com.kelompok1.newsapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

public class SportsNewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Article> allCategoriesArticles;
    private NewsRecyclerAdapter adapter;

    private LinearProgressIndicator progressIndicator;

    public SportsNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_categories, container, false);

        recyclerView = view.findViewById(R.id.all_categories_recycler_view);
        progressIndicator = view.findViewById(R.id.progress_bar);

        setupRecyclerView();
        getNews();

        return view;
    }

    private void setupRecyclerView() {
        allCategoriesArticles = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new NewsRecyclerAdapter(allCategoriesArticles);
        recyclerView.setAdapter(adapter);
    }

    private void changeInProgress(boolean show) {
        if (show) {
            progressIndicator.setVisibility(View.VISIBLE);
        } else {
            progressIndicator.setVisibility(View.INVISIBLE);
        }
    }

    private void getNews() {
        changeInProgress(true);
        NewsApiClient newsApiClient = new NewsApiClient("bf2e2041d80f421f861b81c9f0010160");
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .category("sports")
                        .language("en")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        requireActivity().runOnUiThread(() -> {
                            changeInProgress(false);
                            allCategoriesArticles = response.getArticles();
                            adapter.updateData(allCategoriesArticles);
                            adapter.notifyDataSetChanged();
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("GOT FAILURE", throwable.getMessage());
                        changeInProgress(false);
                    }
                }
        );
    }
}
