package com.example.dengxuejiu.lithonews.model;

/**
 * Created by dengxuejiu on 2018/3/8.
 */

public class Feed {

    public enum FeedType {NEWS_FEED, PHOTO_FEED, AD_FEED, FOURTH, FIFTH}

    public int id;
    public FeedType type;
    public FeedData data;

    @Override
    public String toString() {
        return "Feed [ id: " + id + ", type: " + type + "]";
    }
}

