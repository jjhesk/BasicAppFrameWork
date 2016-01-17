package com.hkm.lycollectionsample.pages.catelog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.hkm.disqus.DisqusClient;
import com.hkm.lycollectionsample.Dialog.ErrorMessage;
import com.hkm.lycollectionsample.adInterstitual.ListAd;
import com.hkm.lycollectionsample.life.Config;
import com.hkm.lycollectionsample.life.EBus;
import com.hkm.lycollectionsample.life.LifeCycleApp;
import com.hkm.lycollectionsample.pages.articlePage.comment_count_search;
import com.hkm.lycollectionsample.pages.featureList.basicfeed;
import com.hkm.lycollectionsample.pages.featureList.featureListFragment;
import com.hkm.lycollectionsample.pages.featureList.patchHyprbid;
import com.hypebeast.sdk.Util.Connectivity;
import com.hypebeast.sdk.api.exception.ApiException;
import com.hypebeast.sdk.api.model.hbeditorial.ArticleData;
import com.hypebeast.sdk.api.model.hbeditorial.PostsObject;
import com.hypebeast.sdk.api.model.hbeditorial.ResponsePostFromSearch;
import com.hypebeast.sdk.api.model.hbeditorial.ResponsePostW;
import com.hypebeast.sdk.api.resources.hypebeast.feedhost;
import com.hypebeast.sdk.clients.HBEditorialClient;
import com.neopixl.pixlui.components.textview.TextView;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.MemoryPolicy;

import java.text.ParseException;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hesk on 9/7/15.
 */
public class template_general_list extends featureListFragment<ArticleData> {
    protected final Callback<ResponsePostW> post_response = new Callback<ResponsePostW>() {
        @Override
        public void success(ResponsePostW list, Response response) {
            display_list(list.postList);
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d("tager", error.getMessage());
            com.hkm.advancedtoolbar.Util.ErrorMessage.alert(error.getMessage(), getChildFragmentManager());
        }
    };

    protected final Callback<ResponsePostFromSearch> search_response = new Callback<ResponsePostFromSearch>() {
        @Override
        public void success(ResponsePostFromSearch responsePostFromSearch, Response response) {
            display_list(responsePostFromSearch.posts);
        }

        @Override
        public void failure(RetrofitError error) {
            com.hkm.advancedtoolbar.Util.ErrorMessage.alert(error.getMessage(), getChildFragmentManager(), new Runnable() {
                @Override
                public void run() {
                    getActivity().finish();
                }
            });
        }
    };


    protected void display_list(PostsObject list) {
        try {
            if (isNowInitial()) {
                doneInitialLoading();
                createHypbridAdapter(list.getArticles());
                getSwitcherBi().setMaxPages(list.getPages());
                afterInitiateHyprbidAdapter();
                if (list.getArticles().size() == 0 && requestType == SEARCH) {
                    EBus.display_no_result c = new EBus.display_no_result(slugtag);
                    EBus.getInstance().post(c);
                }
            } else {
                getSwitcherBi().setMaxPages(list.getPages());
                getSwitcherBi().load_more_data(list.getArticles());
            }
        } catch (Exception e) {
            if (getChildFragmentManager() != null && getActivity() != null) {
                ErrorMessage.alert(e.getMessage(), getChildFragmentManager());
            }
        }
    }

    public static template_general_list B(final Bundle b) {
        final template_general_list t = new template_general_list();
        t.setArguments(b);
        return t;
    }

    protected feedhost interfacerequest;
    protected LifeCycleApp app;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        app = (LifeCycleApp) activity.getApplication();
        interfacerequest = HBEditorialClient.getInstance(activity).createFeedInterface();
    }

    @Override
    protected boolean is_list_with_ad_enabled() {
        return true;
    }


    protected void init_comment_count_search(final ArticleData data, final TextView textview, final String time) {
        try {
            DisqusClient r = DisqusClient.getInstance(getActivity(), ((LifeCycleApp) getActivity().
                    getApplication()).getConfiguration());

            r.createThreads().listPostByIDAsync(
                    data._embedded.disqus_identifier,
                    "hypebeast",
                    new comment_count_search(textview, time));

        } catch (com.hkm.disqus.api.exception.ApiException e) {


        }
    }


    @Override
    protected void binddata(binder holder, final ArticleData data, int position) {
        try {
            final boolean f = Connectivity.isConnectedFast(getActivity());
            final String i = f ? data._links.getThumbnail() : data._links.getThumbnail();
            picasso
                    .load(i)
                    .memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE)
                    .into(holder.big_image_single);
            holder.tvtime.setText(data.getCate());
            init_comment_count_search(data, holder.comment_counts, data.getMoment());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * trigger point to the new page.
         */
        holder.click_detection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  onClickItem(data.id);
                onClickItem(data._links.getSelf());
            }
        });
        holder.tvtitle.setText(data.title);
    }

    protected void createHypbridAdapter(final @NonNull List<ArticleData> source) {
        final featureListFragment.cateadapter noad = new featureListFragment.cateadapter(source);
        final featureListFragment.admobadp lolpp = new featureListFragment.admobadp(ListAd.newAdView(getActivity()), false, ListAd.LIST_AD_INTERVAL, source, lsi);
        if (source.size() < Config.setting.single_page_items) {
            listview_layout.disableLoadmore();
        }
        sw = new patchHyprbid(listview_layout, noad, lolpp);
    }

    /**
     * step 2:
     * this is the call for the loading the data stream externally
     */
    @Override
    protected void loadDataInitial() {
        try {
            onLoadMore(0, 1, Config.setting.single_page_items);
        } catch (ApiException e) {
            ErrorMessage.alert(e.getMessage(), getChildFragmentManager());
        }
    }

    @Override
    protected void onLoadMore(int viewType, int currentpage, int ppg) throws ApiException {
      /*  if (requestType == featureListFragment.LATEST) {
            interfacerequest.full_path_list(ppg,  this);
        } else if (requestType == featureListFragment.CATE) {
           // interfacerequest.category(slugtag, currentpage, ppg, "DESC", this);
            interfacerequest.
        } else if (requestType == featureListFragment.SEARCH) {
            triggerSearch(getArguments().getString(SEARCH_WORD));
        }*/

        if (requestType == featureListFragment.LATEST) {
            interfacerequest.the_recent_page(currentpage, post_response);
        } else if (requestType == featureListFragment.CATE) {
            interfacerequest.cate_list(currentpage, slugtag, post_response);
        } else if (requestType == featureListFragment.SEARCH) {
            //interfacerequest.search(slugtag, currentpage, search_response);
            triggerSearch(getArguments().getString(SEARCH_WORD));
        } else if (requestType == basicfeed.GENERAL) {
            HBEditorialClient.getInstance(getActivity()).createAPIUniversal(adapter_url).atPage(currentpage, post_response);
        }
    }

    public void triggerSearch(final String searchWord) {
        try {
            requestType = featureListFragment.SEARCH;
            slugtag = searchWord;
            setRefreshInitial();
            interfacerequest.search(searchWord, 1, search_response);
        } catch (Exception e) {
            ErrorMessage.alert(e.getMessage(), getChildFragmentManager());
        }
    }

    @Subscribe
    public void eventRefresh(EBus.refresh re) {
        if (re.isEventRequestStarted()) {
            setRefreshInitial();
        }
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();
        EBus.getInstance().register(this);
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to {@link Activity#onStop() Activity.onStop} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStop() {
        super.onStop();
        EBus.getInstance().unregister(this);
    }


}
