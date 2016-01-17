package com.hkm.downloadmedialocker.pages.catelog;

import android.util.Log;

import com.hkm.downloadmedialocker.life.LifeCycleApp;
import com.hkm.downloadmedialocker.pages.featureList.gridListBasic;
import com.hkm.downloadmedialocker.pages.featureList.videoListFragment;
import com.hypebeast.sdk.api.model.hbeditorial.ArticleData;
import com.hypebeast.sdk.api.model.hbeditorial.ResponsePostFromSearch;
import com.hypebeast.sdk.api.model.hbeditorial.ResponsePostW;
import com.hypebeast.sdk.api.resources.hypebeast.feedhost;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hesk on 13/1/16.
 */
public class template_video_grid extends gridListBasic {
    protected feedhost interfacerequest;
    protected LifeCycleApp app;

    @Override
    protected void setUltimateRecyclerViewExtra(UltimateRecyclerView listview) {

    }

    @Override
    protected void binddata(videoListFragment.binder holder, ArticleData data, int position) {

    }

    /**
     * step 2:
     * this is the call for the loading the data stream externally
     */
    @Override
    protected void loadDataInitial() {
      /*  try {
            if (isNowInitial()) {
                doneInitialLoading();


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
        }*/
    }


    protected final Callback<ResponsePostW> post_response = new Callback<ResponsePostW>() {
        @Override
        public void success(ResponsePostW list, Response response) {
            //  display_list(list.postList);
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
            //display_list(responsePostFromSearch.posts);
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

}
