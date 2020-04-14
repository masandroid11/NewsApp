package com.udacityprojects.newsapp.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udacityprojects.newsapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final OnClickHandler clickHandler;
    private TypedArray categoryIconsIds;
    private List<String> categoryTitleList;

    public interface OnClickHandler{
        void onClick(int position);
    }

    public CategoryAdapter(OnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.category_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId,parent,false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.categoryIcon.setImageResource(categoryIconsIds.getResourceId(position,-1));
        holder.categoryTitle.setText(categoryTitleList.get(position));
    }

    @Override
    public int getItemCount() {
        return (categoryTitleList != null) ? categoryTitleList.size() : 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.category_iv)
        ImageView categoryIcon;
        @BindView(R.id.category_tv)
        TextView categoryTitle;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            clickHandler.onClick(position);
        }
    }

    public TypedArray getCategoryIcons() {
        return categoryIconsIds;
    }

    public void setCategoryIcons(TypedArray categoryIcons) {
        this.categoryIconsIds = categoryIcons;
        notifyDataSetChanged();
    }

    public List<String> getCategoryTitleList() {
        return categoryTitleList;
    }

    public void setCategoryTitleList(List<String> categoryTitleList) {
        this.categoryTitleList = categoryTitleList;
        notifyDataSetChanged();
    }
}
