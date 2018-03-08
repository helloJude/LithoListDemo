package com.example.dengxuejiu.lithonews;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.soloader.SoLoader;

/**
 * Created by Srijith on 19-11-2017.
 */

public class FbLithoDemoApp extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, false);
    Fresco.initialize(this);
  }
}
