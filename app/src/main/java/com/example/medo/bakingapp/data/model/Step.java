package com.example.medo.bakingapp.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.example.medo.bakingapp.BR;
import com.example.medo.bakingapp.R;
import com.example.medo.bakingapp.utils.GlideApp;


public class Step extends BaseObservable implements Parcelable {

    private String videoURL;
    private String description;
    private int id;
    private String shortDescription;
    private String thumbnailURL;

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public Step() {
    }

    public Step(String videoURL, String description, int id, String shortDescription, String thumbnailURL) {
        this.videoURL = videoURL;
        this.description = description;
        this.id = id;
        this.shortDescription = shortDescription;
        this.thumbnailURL = thumbnailURL;
    }

    protected Step(Parcel in) {
        videoURL = in.readString();
        description = in.readString();
        id = in.readInt();
        shortDescription = in.readString();
        thumbnailURL = in.readString();
    }


    @BindingAdapter({"errorImage"})
    public static void loadImage(ImageView view, String imageUrl) {
        GlideApp.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.error)
                .error(R.drawable.error)
                .into(view);
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getDescription() {
        return description;
    }


    @Bindable
    public int getId() {
        notifyPropertyChanged(BR.id);
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Bindable
    public String getShortDescription() {
        return shortDescription;
    }



    @Bindable
    public String getThumbnailURL() {
        return thumbnailURL;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoURL);
        dest.writeString(description);
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(thumbnailURL);
    }
}
