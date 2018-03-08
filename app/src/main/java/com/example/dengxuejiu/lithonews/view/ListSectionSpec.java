package com.example.dengxuejiu.lithonews.view;

import android.graphics.Color;

import com.example.dengxuejiu.lithonews.DataService;
import com.example.dengxuejiu.lithonews.model.Feed;
import com.example.dengxuejiu.lithonews.model.FeedModel;
import com.facebook.litho.StateValue;
import com.facebook.litho.annotations.FromEvent;
import com.facebook.litho.annotations.OnCreateInitialState;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.OnUpdateState;
import com.facebook.litho.annotations.Param;
import com.facebook.litho.annotations.State;
import com.facebook.litho.sections.Children;
import com.facebook.litho.sections.LoadingEvent;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.SectionLifecycle;
import com.facebook.litho.sections.annotations.GroupSectionSpec;
import com.facebook.litho.sections.annotations.OnBindService;
import com.facebook.litho.sections.annotations.OnCreateChildren;
import com.facebook.litho.sections.annotations.OnCreateService;
import com.facebook.litho.sections.annotations.OnRefresh;
import com.facebook.litho.sections.annotations.OnUnbindService;
import com.facebook.litho.sections.annotations.OnViewportChanged;
import com.facebook.litho.sections.common.SingleComponentSection;
import com.facebook.litho.widget.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengxuejiu on 2018/3/8.
 */
@GroupSectionSpec
public class ListSectionSpec {

    @OnCreateInitialState
    static void createInitialState(
            final SectionContext c,
            StateValue<List<Feed>> feeds,
            StateValue<Integer> start,
            StateValue<Integer> count,
            StateValue<Boolean> isFetching) {
        start.set(0);
        count.set(15);
        feeds.set(new DataService().getData(0, 15).feeds);
        isFetching.set(false);
    }

    @OnCreateChildren
    static Children onCreateChildren(
            final SectionContext c,
            @State List<Feed> feeds) {
        Children.Builder builder = Children.create();
        for (int i = 0; i < feeds.size(); i++) {
            Feed model = feeds.get(i);
            builder.child(
                    SingleComponentSection.create(c)
                            .key("" + model.id)
                            .component(
                                    Card.create(c)
                                            .content(
                                                    CardElement.create(c)
                                                            .id(model.id)
                                                            .type(model.type)
                                                            .title(model.data.title)
                                                            .description(model.data.description)
                                                            .imageRes(model.data.photos)
                                                            .build()
                                            )
                                            .cardBackgroundColor(Color.WHITE)
                                            .elevationDip(6)//增加第三维的高度
                                            .build()
                            )
            );
        }
        builder.child(
                SingleComponentSection.create(c)
                        .component(ProgressLayout.create(c))
                        .build()
        );

        return builder.build();
    }

    @OnCreateService
    static DataService onCreateService(
            final SectionContext c,
            @State List<Feed> feeds,
            @State int start,
            @State int count) {
        return new DataService();
    }


    @OnBindService
    static void onBindService(final SectionContext c, final DataService service) {
        /**
         * onBindService() is called (along with onUnbindService()) every time
         * the section tree is updated (usually because of a state update).
         * This function is passed the service created by onCreateService as the second function parameter.
         * In this function you should bind any EventHandler that will make state changes to your service.
         **/
        service.registerLoadingEvent(ListSection.onDataLoaded(c));
    }

    @OnUnbindService
    static void onUnbindService(final SectionContext c, final DataService service) {
        service.unregisterLoadingEvent();
    }

    @OnEvent(FeedModel.class)//会生成 EventHandler ，用于传递数据
    static void onDataLoaded(final SectionContext c, @FromEvent List<Feed> feeds) {
        ListSection.updateData(c, feeds);
        ListSection.setFetching(c, false);
        SectionLifecycle.dispatchLoadingEvent(c, false, LoadingEvent.LoadingState.SUCCEEDED, null);
    }

    @OnUpdateState
    static void updateData(
            final StateValue<List<Feed>> feeds,//设置的state，一直伴随section
            final StateValue<Integer> start,//
            @Param List<Feed> newFeeds) {//newFeeds  跟随 event 被传递过来
        if (start.get() == 0) {
            feeds.set(newFeeds);
        } else {
            List<Feed> feeds1 = new ArrayList<>();
            feeds1.addAll(feeds.get());
            feeds1.addAll(newFeeds);
            feeds.set(feeds1);
        }
    }

    @OnRefresh
    static void onRefresh(
            SectionContext c,
            DataService service,
            @State List<Feed> feeds,
            @State int start,
            @State int count) {
        ListSection.updateStartParam(c, 0);
        service.refetch(0, 15);
    }

    @OnUpdateState
    static void setFetching(final StateValue<Boolean> isFetching, @Param boolean fetch) {
        isFetching.set(fetch);
    }

    @OnUpdateState
    static void updateStartParam(final StateValue<Integer> start, @Param int newStart) {
        start.set(newStart);
    }

    /**
     * 触发下拉刷新的逻辑
     *
     * @param c
     * @param firstVisiblePosition
     * @param lastVisiblePosition
     * @param firstFullyVisibleIndex
     * @param lastFullyVisibleIndex
     * @param totalCount
     * @param service
     * @param feeds
     * @param start
     * @param count
     * @param isFetching
     */
    @OnViewportChanged
    static void onViewportChanged(
            SectionContext c,
            int firstVisiblePosition,
            int lastVisiblePosition,
            int firstFullyVisibleIndex,
            int lastFullyVisibleIndex,
            int totalCount,
            DataService service,
            @State List<Feed> feeds,
            @State int start,
            @State int count,
            @State boolean isFetching) {
        if (totalCount >= feeds.size() - 1 && !isFetching) {
            ListSection.setFetching(c, true);
            ListSection.updateStartParam(c, feeds.size());
            service.fetch(feeds.size(), count);
        }
    }
}
