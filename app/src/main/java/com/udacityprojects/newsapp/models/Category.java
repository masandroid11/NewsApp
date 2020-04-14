package com.udacityprojects.newsapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.picasso.Picasso;

public class Category implements Parcelable {
    private int drawable_id;
    private String category_name;
    public static final String PARCELABLE_KEY = "category";

    public Category(int drawable_id, String category_name) {
        this.drawable_id = drawable_id;
        this.category_name = category_name;
    }

    protected Category(Parcel in) {
        drawable_id = in.readInt();
        category_name = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public int getDrawable_id() {
        return drawable_id;
    }

    public void setDrawable_id(int drawable_id) {
        this.drawable_id = drawable_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(drawable_id);
        dest.writeString(category_name);
    }
}
