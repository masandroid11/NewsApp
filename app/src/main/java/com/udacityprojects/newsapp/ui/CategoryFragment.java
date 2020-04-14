package com.udacityprojects.newsapp.ui;


import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacityprojects.newsapp.R;
import com.udacityprojects.newsapp.adapters.CategoryAdapter;
import com.udacityprojects.newsapp.models.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements CategoryAdapter.OnClickHandler {

    private TypedArray categoryIconIds;
    private List<String> categoryTitleList = new ArrayList<>();

    private CategoryAdapter adapter;

    @BindView(R.id.category_rv)
    RecyclerView recyclerView;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this,rootView);

        categoryIconIds = getResources().obtainTypedArray(R.array.category_ic_resourceId);

        String[] titles = getResources().getStringArray(R.array.category_array);

        categoryTitleList = Arrays.asList(titles);

        adapter = new CategoryAdapter(this);
        adapter.setCategoryIcons(categoryIconIds);
        adapter.setCategoryTitleList(categoryTitleList);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
        // Inflate the layout for this fragment
        return rootView;

    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getContext(),HeadlinesActivity.class);
        intent.putExtra(Category.PARCELABLE_KEY,categoryTitleList.get(position));

        startActivity(intent);
    }

    public TypedArray getCategoryIconIds() {
        return categoryIconIds;
    }

    public List<String> getCategoryTitleList() {
        return categoryTitleList;
    }
}
