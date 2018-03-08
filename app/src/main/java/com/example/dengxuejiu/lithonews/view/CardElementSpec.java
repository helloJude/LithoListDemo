package com.example.dengxuejiu.lithonews.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.ImageView;

import com.example.dengxuejiu.lithonews.R;
import com.example.dengxuejiu.lithonews.model.Feed;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.Row;
import com.facebook.litho.annotations.FromEvent;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.fresco.FrescoImage;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.common.DataDiffSection;
import com.facebook.litho.sections.common.RenderEvent;
import com.facebook.litho.sections.widget.ListRecyclerConfiguration;
import com.facebook.litho.sections.widget.RecyclerCollectionComponent;
import com.facebook.litho.widget.ComponentRenderInfo;
import com.facebook.litho.widget.Image;
import com.facebook.litho.widget.RenderInfo;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaEdge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengxuejiu on 2018/3/8.
 */

@LayoutSpec
public class CardElementSpec {
    @OnCreateLayout
    static Component onCreateLayout(
            ComponentContext c,
            @Prop int id,
            @Prop Feed.FeedType type,
            @Prop String title,
            @Prop String description,
            @Prop int[] imageRes) {
        Component titleComp = Text.create(c, 0, R.style.TextAppearance_AppCompat_Title)
                .text(title)
                .marginDip(YogaEdge.TOP, 16)
                .marginDip(YogaEdge.BOTTOM, 8)
                .marginDip(YogaEdge.HORIZONTAL, 8)
                .typeface(Typeface.DEFAULT_BOLD)
                .textColor(Color.BLACK)
                .build();
        Component descComp = Text.create(c)
                .text(description)
                .maxLines(4)
                .ellipsize(TextUtils.TruncateAt.END)
                .textSizeSp(17)
                .paddingDip(YogaEdge.BOTTOM, 8)
                .marginDip(YogaEdge.VERTICAL, 16)
                .marginDip(YogaEdge.HORIZONTAL, 8)
                .build();

        Component imageComp;
//        if (type == Feed.FeedType.NEWS_FEED || type == Feed.FeedType.AD_FEED) {
//        imageComp = getImageComp(c, imageRes[0]);
//        } else {
//            imageComp = getRecyclerComp(c, imageRes);//when use
//        }

        switch (type) {
            case NEWS_FEED:
                imageComp = getImageComp(c, imageRes[0]);
                break;
            case AD_FEED:
                imageComp = getADFeedComp(c);
                break;
            case PHOTO_FEED:
                imageComp = getRecyclerComp(c, imageRes);
                break;
            case FOURTH:
                imageComp = getFourthFeedComp(c);
                break;
            case FIFTH:
                imageComp = getFifthFeedComp(c, "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=4182416542,664071745&fm=173&s=D6849143564304F40480A08C0300D087&w=218&h=146&img.JPEG");
                break;
            default:
                imageComp = getImageComp(c, imageRes[0]);
                break;
        }
        return Column.create(c)
                .child(Column.create(c).heightDip(10).backgroundColor(Color.RED).build())
                .child(titleComp)
                .child(imageComp)
                .child(descComp)
                .build();

    }

    private static Component getFifthFeedComp(ComponentContext c, String image) {
        final DraweeController controller = Fresco.newDraweeControllerBuilder().setUri(image).build();

        return FrescoImage.create(c)
                .controller(controller)
                .placeholderImageRes(R.mipmap.ic_launcher_round)
                .imageAspectRatio(2f)
                .build();
    }

    private static Component getFourthFeedComp(ComponentContext c) {
        return Row.create(c)
                .alignItems(YogaAlign.CENTER)
                .paddingDip(YogaEdge.ALL, 16)
                .child(
                        Row.create(c)
                                .heightPx(1)
                                .backgroundColor(0xFFAAAAAA)
                                .flex(1))
                .child(
                        Text.create(c)
                                .text(String.valueOf("1998"))
                                .textSizeDip(14)
                                .textColor(0xFFAAAAAA)
                                .marginDip(YogaEdge.HORIZONTAL, 10)
                                .flex(0))
                .child(
                        Row.create(c)
                                .heightPx(1)
                                .backgroundColor(0xFFAAAAAA)
                                .flex(1))
                .backgroundColor(0xFFFAFAFA)
                .build();
    }

    private static Component getADFeedComp(ComponentContext c) {
        return Row.create(c)
                .child(Image.create(c)
                        .drawableRes(R.mipmap.ic_launcher))
                .child(Text.create(c)
                        .text("ADDAD")
                        .textSizeDip(20)).build();
    }

    private static Component getImageComp(ComponentContext c, int imageRes) {
        return Image.create(c)
                .drawableRes(imageRes)
                .widthPercent(100)
                .heightDip(200)
                .scaleType(ImageView.ScaleType.CENTER_CROP)
                .build();
    }

    private static Component getRecyclerComp(ComponentContext c, int[] imageRes) {
        return RecyclerCollectionComponent.create(c)
                .heightDip(200)
                .itemDecoration(new ImageItemDecoration())
                .recyclerConfiguration(new ListRecyclerConfiguration<>(LinearLayoutManager.HORIZONTAL, false))
                .section(
                        DataDiffSection.<Integer>create(new SectionContext(c))
                                .data(CardElementSpec.getImageArray(imageRes))
                                .renderEventHandler(CardElement.onRenderImages(c))
                                .build()
                )
                .build();
    }

    @OnEvent(RenderEvent.class)
    static RenderInfo onRenderImages(final ComponentContext c, @FromEvent Integer model) {
        return ComponentRenderInfo.create()
                .component(
                        Image.create(c)
                                .drawableRes(model)
                                .widthPercent(100)
                                .heightDip(200)
                                .scaleType(ImageView.ScaleType.CENTER_CROP)
                                .build()
                )
                .build();
    }

    private static List<Integer> getImageArray(int[] imageRes) {
        List<Integer> images = new ArrayList<>(imageRes.length);
        for (int image : imageRes) {
            images.add(image);
        }
        return images;
    }
}
