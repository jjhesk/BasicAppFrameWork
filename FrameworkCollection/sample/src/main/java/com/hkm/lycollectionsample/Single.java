package com.hkm.lycollectionsample;


import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.hkm.disqus.DisqusClient;
import com.hkm.disqus.api.exception.ApiException;
import com.hkm.disqus.api.model.Response;
import com.hkm.disqus.api.model.posts.Post;

import com.hkm.lycollectionsample.pages.articlePage.ArticlePageSetup;
import com.hkm.lycollectionsample.life.LifeCycleApp;
import com.hypebeast.sdk.api.model.hbeditorial.EmbedPayload;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrListener;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by hesk on 23/4/15.
 */
public class Single extends ArticlePageSetup implements Callback<Response<List<Post>>>, SlidrListener {
    private List<Post> comment_object = new ArrayList<>();

    protected void controlNagivation(Toolbar ab) {
        ab.setNavigationIcon(R.drawable.ic_back_adjusted);
        ab.setBackgroundResource(R.drawable.actionbar_bg_hb_white);
        Slidr.attach(this, ((LifeCycleApp) getApplication()).getSlidrConfig(this));
    }

    // toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
    protected void triggerLoadComments(EmbedPayload article) {
        if (comment_object.size() < 0) {
            updateNumberOnCommentIcon();
        } else {
            try {
                DisqusClient r = DisqusClient.getInstance(getApplication(), ((LifeCycleApp) getApplication()).getConfiguration());
                r.createThreads().listPostByIDAsync(
                        article.disqus_identifier,
                        "hypebeast",
                        this);
            } catch (ApiException e) {
            }
        }
    }

    @Override
    public void success(com.hkm.disqus.api.model.Response<List<Post>> p, retrofit.client.Response res) {
        comment_object.clear();
        comment_object.addAll(p.data);
        // retention.currentReadingPost.setDisQusPost(p.data);
        // adapterPointer().notifyDataSetChanged();
        updateNumberOnCommentIcon(p.data.size());
        // _panelamo.loadingComments();
    }


    @Override
    public void failure(RetrofitError error) {
        Log.d(TAG, error.getMessage());
    }


    @Override
    protected void updateNumberOnCommentIcon() {
        if (_panelamo != null) {
            _panelamo.commentCount(comment_object.size());
        }
    }

    @Override
    protected void updateNumberOnCommentIcon(int n) {
        if (_panelamo != null) {
            _panelamo.commentCount(n);
        }
    }

    @Override
    public void onBackPressed() {
        if (_panelamo == null) {
            super.onBackPressed();
        } else if (_panelamo.onBackPress()) {
            super.onBackPressed();
        }
    }


    /**
     * This is called when the {@link ViewDragHelper} calls it's
     * state change callback.
     *
     * @param state the {@link ViewDragHelper} state
     * @see ViewDragHelper#STATE_IDLE
     * @see ViewDragHelper#STATE_DRAGGING
     * @see ViewDragHelper#STATE_SETTLING
     */
    @Override
    public void onSlideStateChanged(int state) {

    }

    @Override
    public void onSlideChange(float percent) {

    }

    @Override
    public void onSlideOpened() {

    }

    @Override
    public void onSlideClosed() {

    }
}