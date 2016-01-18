package com.hkm.downloadmedialocker.pages.content;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hkm.downloadmedialocker.R;
import com.hkm.downloadmedialocker.pages.adapters.shareactionSpline;
import com.hkm.downloadmedialocker.life.Config;
import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by hesk on 29/4/15.
 */
public class ShareArticle extends Fragment {

    public final static String
            PAGENUM = "PNUM",
            POS = "PAGEPOS",
            LOADURL = "LOAD",
            TAG = "LISTFEED";

    public static int RELOAD = 1, BYARGUMENT = -1;
    private UltimateRecyclerView pager;
    protected ProgressBar uProgressbar;
    private shareactionSpline adapter;
    private LinearLayoutManager linearLayoutManager;
    private int now_page;
    private ItemTouchListenerAdapter itemInteraction;

    private List<ResolveInfo> list;
    //  private PostsObject article;


    public ShareArticle() {
    }

    public interface dgg {
        void postResults(shareactionSpline result);
    }

    /*public interface bindArticleData {
        SingleArticle bind();
    }

    private bindArticleData sharer;

    public void setBindShareObject(bindArticleData share) {
        sharer = share;
    }*/

    protected void doneInitialLoading() {
        uProgressbar.animate().alpha(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                uProgressbar.setVisibility(View.GONE);
            }
        });
    }

    public static class loadshareapps extends AsyncTask<Void, Void, shareactionSpline> {
        private Activity here;
        private String[] filterout = new String[]{
                "com.antlib.thisavhelper",
                "com.beetalk"
        };
        private dgg callback;

        public loadshareapps(Activity place, dgg cb) {
            here = place;
            callback = cb;
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected shareactionSpline doInBackground(Void... params) {
            //     try {

            //    } catch (IndexOutOfBoundsException e) {
            //        return new shareactionSpline(new ArrayList<ResolveInfo>(), here);
            //    }

            final PackageManager pm = here.getPackageManager();
            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "action shared");

            final List<ResolveInfo> L = pm.queryIntentActivities(shareIntent, 0);
            Iterator<ResolveInfo> itEs = L.iterator();
            while (itEs.hasNext()) {
                ResolveInfo k = itEs.next();
                //  String packageName = k.activityInfo.packageName;
                for (int j = 0; j < filterout.length; j++) {
                    if (k.activityInfo.applicationInfo.packageName.contains(filterout[j])) {
                        itEs.remove();
                    }
                }
            }
           /* for (int i = 0; i < L.size(); i++) {
                ResolveInfo k = L.get(i);
                String packageName = k.activityIznfo.packageName;
                for (int j = 0; j < filterout.length; j++) {
                    if (packageName.contains(filterout[j])) {
                        L.remove(i);
                    }
                }
            }*/

            final int applyhere = Math.min(L.size(), Config.setting.show_share_items);
            if (applyhere == Config.setting.show_share_items) {
                //      return L.subList(0, Config.setting.show_share_items);
            }
            //  int total = L.size();
            //  return new ArrayList<>(L.subList(0, 20));
            return new shareactionSpline(L, here);
        }

        @Override
        protected void onPostExecute(shareactionSpline result) {
            callback.postResults(result);
        }

        private List<ResolveInfo> getList() /*throws IndexOutOfBoundsException */ {

            final PackageManager pm = here.getPackageManager();
            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "action shared");

            final List<ResolveInfo> L = pm.queryIntentActivities(shareIntent, 0);
            for (int i = 0; i < L.size(); i++) {
                ResolveInfo k = L.get(i);
                String packageName = k.activityInfo.packageName;
                for (int j = 0; j < filterout.length; j++) {
                    if (packageName.contains(filterout[j])) {
                        L.remove(i);
                    }
                }
            }

            final int applyhere = Math.min(L.size(), Config.setting.show_share_items);
            if (applyhere == Config.setting.show_share_items) {
                return L.subList(0, Config.setting.show_share_items);
            }

            int total = L.size();
            return new ArrayList<ResolveInfo>(L.subList(0, 20));
        }
    }

    public static shareactionSpline findapps(String[] filterout, Context here) {

        final PackageManager pm = here.getPackageManager();
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "action shared");

        final List<ResolveInfo> L = pm.queryIntentActivities(shareIntent, 0);
        Iterator<ResolveInfo> itEs = L.iterator();
        while (itEs.hasNext()) {
            ResolveInfo k = itEs.next();
            //  String packageName = k.activityInfo.packageName;
            for (int j = 0; j < filterout.length; j++) {
                if (k.activityInfo.applicationInfo.packageName.contains(filterout[j])) {
                    itEs.remove();
                }
            }
        }
           /* for (int i = 0; i < L.size(); i++) {
                ResolveInfo k = L.get(i);
                String packageName = k.activityInfo.packageName;
                for (int j = 0; j < filterout.length; j++) {
                    if (packageName.contains(filterout[j])) {
                        L.remove(i);
                    }
                }
            }*/

        final int applyhere = Math.min(L.size(), Config.setting.show_share_items);
        if (applyhere == Config.setting.show_share_items) {
            return new shareactionSpline(L.subList(0, Config.setting.show_share_items), here);
        }
        return new shareactionSpline(L, here);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.jazz_list_home, container, false);
    }

    private String getShareContent() {
      /* SingleArticle article = null;
        if (sharer != null) {
         article = sharer.bind();
        } else {
            return "error nothing can be shared";
        }
      String title = article.single_article_title;
       String link = article._links.self.getHref();
        return "I just read an article about " + title + ", check it out @ " + link;
        */
        return "";
    }

    private void withevents(final View v) {
        try {
            pager = (UltimateRecyclerView) v.findViewById(R.id.recyclerlistview);
            itemInteraction = new ItemTouchListenerAdapter(pager.mRecyclerView,
                    new ItemTouchListenerAdapter.RecyclerViewOnItemClickListener() {
                        @Override
                        public void onItemClick(RecyclerView parent, View clickedView, int position) {
                            Log.d(TAG, "get click view ID:" + clickedView.getId() + " pos:" + position);
                            // getActivity().startActivity(newpage(position));

                            ActivityInfo activity = list.get(position).activityInfo;
                            ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);

                            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, getShareContent());

                            Intent newIntent = (Intent) shareIntent.clone();
                            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            newIntent.setComponent(name);
                            getActivity().startActivity(newIntent);

                        }

                        @Override
                        public void onItemLongClick(RecyclerView parent, View clickedView, int position) {
                     /*
                        if (isDrag) {
                            URLogs.d("onItemLongClick()" + isDrag);
                            toolbar.startActionMode(CustomSwipeToRefreshRefreshActivity.this);
                            toggleSelection(position);
                            dragDropTouchListener.startDrag();
                            ultimateRecyclerView.enableDefaultSwipeRefresh(false);
                        }
*/
                        }
                    });
            pager.mRecyclerView.addOnItemTouchListener(itemInteraction);
            /* View newlayout = LayoutInflater.from(getActivity()).inflate(R.layout.custom_bottom_progressbar, null);*/
            //adapter.setCustomLoadMoreView(newlayout);
            pager.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            pager.setAdapter(adapter);
            //  pager.enableLoadmore();
            //reload(BYARGUMENT);
        } catch (Exception e) {
        }
    }


    private String[] filterout = new String[]{
            "com.antlib.thisavhelper",
            "com.beetalk"
    };


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(final View v, Bundle b) {
        try {
            uProgressbar = (ProgressBar) v.findViewById(R.id.ul_loading_progress);
            adapter = findapps(filterout, getActivity());
            list = adapter.getListContent();
            withevents(v);
            doneInitialLoading();
            /*new loadshareapps(getActivity(), new dgg() {
                @Override
                public void postResults(shareactionSpline here) {
                    adapter = here;
                    list = here.getListContent();
                    withevents(v);
                }
            }).execute();*/
        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, e.getLocalizedMessage());
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
    }


}
