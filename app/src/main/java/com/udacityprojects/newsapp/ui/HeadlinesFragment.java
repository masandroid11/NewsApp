package com.udacityprojects.newsapp.ui;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacityprojects.newsapp.R;
import com.udacityprojects.newsapp.adapters.HeadlinesAdapter;
import com.udacityprojects.newsapp.models.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeadlinesFragment extends Fragment implements HeadlinesAdapter.OnClickHandler {

    private List<Article> headlines;
    private HeadlinesAdapter adapter;
    @BindView(R.id.headline_rv) RecyclerView recyclerView;
    
    public HeadlinesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_headlines,container,false);
        ButterKnife.bind(this,rootView);

        if (getActivity() instanceof  MainActivity){
            headlines = ((MainActivity) getActivity()).getHeadlines();
        }else if (getActivity() instanceof  HeadlinesActivity){
            headlines = ((HeadlinesActivity) getActivity()).getHeadlines();
        }

        setupViews();

        return rootView;
    }

    private void setupViews(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();

        adapter = new HeadlinesAdapter(this);
        adapter.setHeadlines(headlines);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getContext(),DetailActivity.class);
        intent.putExtra(Article.PARCELABLE_KEY,headlines.get(position));

        startActivity(intent);
    }
}
