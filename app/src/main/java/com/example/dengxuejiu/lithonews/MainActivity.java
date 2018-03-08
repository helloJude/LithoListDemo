package com.example.dengxuejiu.lithonews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.dengxuejiu.lithonews.view.ListSection;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.LithoView;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.widget.RecyclerCollectionComponent;
import com.facebook.yoga.YogaEdge;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ComponentContext c = new ComponentContext(this);
        Component component = RecyclerCollectionComponent.create(c)
                .section(ListSection.create(new SectionContext(c)).build())
                .paddingDip(YogaEdge.TOP, 8).build();




        final LithoView lithoView = LithoView.create(c, component);

        setContentView(lithoView);
    }
}
