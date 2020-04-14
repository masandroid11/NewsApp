package com.udacityprojects.newsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.udacityprojects.newsapp.R;
import com.udacityprojects.newsapp.models.Article;
import com.udacityprojects.newsapp.utilities.DataUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeadlinesAdapter extends RecyclerView.Adapter<HeadlinesAdapter.HeadlineViewHolder> {

    private List<Article> headlines;
    private final OnClickHandler clickHandler;



    public interface OnClickHandler {
        void onClick(int position);
    }

    public HeadlinesAdapter(OnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public HeadlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.headline_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId,parent,false);

        return new HeadlineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeadlineViewHolder holder, int position) {
        holder.headlineText.setText(headlines.get(position).getTitle());
        holder.headlineDate.setText(DataUtils.formatDataTime(headlines.get(position).getPublishedAt()));
        Picasso.get()
                .load(headlines.get(position).getUrlToImage())
                .resize(250,250)
                .into(holder.headlineThumbnail);
    }

    @Override
    public int getItemCount() {
        return (headlines != null) ? headlines.size() : 0;
    }

    public class HeadlineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.headline_thumbnail) ImageView headlineThumbnail;
        @BindView(R.id.headline_text) TextView headlineText;
        @BindView(R.id.headline_date) TextView headlineDate;

        public HeadlineViewHolder(@NonNull View itemView) {
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

    public List<Article> getHeadlines() {
        return headlines;
    }

    public void setHeadlines(List<Article> headlines) {
        this.headlines = headlines;
        notifyDataSetChanged();
    }
}
