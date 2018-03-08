package com.example.dengxuejiu.lithonews.model;

import com.facebook.litho.annotations.Event;

import java.util.List;

/**
 * Created by Srijith on 21-11-2017.
 */

@Event
public class FeedModel {//与litho的框架配合使用则需要加上 @Event？？？
  public List<Feed> feeds;
}
