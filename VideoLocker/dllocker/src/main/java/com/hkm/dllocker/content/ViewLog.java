package com.hkm.dllocker.content;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hkm.advancedtoolbar.Util.ErrorMessage;
import com.hkm.dllocker.R;
import com.hkm.dllocker.module.DLUtil;

/**
 * Created by zJJ on 1/23/2016.
 */
public class ViewLog extends Fragment {
    @LayoutRes
    protected int getLayoutListId() {
        return R.layout.content_log;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //  rm_container = RecordContainer.getInstnce(getActivity());
        return inflater.inflate(getLayoutListId(), container, false);
    }

    private ProgressBar loadingbar;
    private TextView blog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        try {
            blog = (TextView) view.findViewById(R.id.text_display_console);
            loadingbar = (ProgressBar) view.findViewById(R.id.lylib_ui_loading_circle);
            blog.setText(DLUtil.Log);
            enableLoading(false);
        } catch (Exception e) {
            ErrorMessage.alert(e.getMessage(), getChildFragmentManager());
        }
    }

    protected void enableLoading(boolean b) {
        if (b) {
            loadingbar.animate().alpha(1f);
        } else {
            loadingbar.animate().alpha(0f);
        }
    }

}
