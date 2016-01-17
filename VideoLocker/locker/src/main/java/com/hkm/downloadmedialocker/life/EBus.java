package com.hkm.downloadmedialocker.life;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.squareup.otto.Bus;

/**
 * Created by hesk on 19/8/15.
 */
public class EBus {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private EBus() {

    }


    public static final int EVENT_REQUEST = 12;
    public static final int EVENT_SUCCESS = 13;

    public static class display_no_result {
        private String search_query;

        public display_no_result(String q) {
            search_query = q;
        }

        public String getSearch_query() {
            return search_query;
        }
    }

    public static class refresh {
        private int event;

        public refresh(int event_start) {
            this.event = event_start;
        }

        public boolean isEventRequestStarted() {
            return event == EVENT_REQUEST;
        }

        public boolean isEventReturnSuccess() {
            return event == EVENT_SUCCESS;
        }
    }

    public static class Scrolling {
        public static boolean touchSync;
        private boolean enabled;
        private boolean directionUp;

        public Scrolling(boolean statusEnabled, boolean directionUp) {
            this.enabled = statusEnabled;
            this.directionUp = directionUp;
        }

        public boolean getEnabled() {
            return this.enabled;
        }

        public boolean getDirectionUpFreeHand() {
            return this.directionUp;
        }

        public static class ScrollEvent extends RecyclerView.OnScrollListener {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    LinearLayoutManager lmg = (LinearLayoutManager) recyclerView.getLayoutManager();
                    final boolean F1PosA32 = lmg.findFirstCompletelyVisibleItemPosition() == 0;
                    EBus.getInstance().post(new EBus.Scrolling(F1PosA32, dy < 0 && !Scrolling.touchSync));
                }

            }
        }

        public static class SyncTouchEvent implements View.OnTouchListener {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                    Scrolling.touchSync = true;
                } else {
                    Scrolling.touchSync = false;
                }
                return false;
            }
        }

    }
}
