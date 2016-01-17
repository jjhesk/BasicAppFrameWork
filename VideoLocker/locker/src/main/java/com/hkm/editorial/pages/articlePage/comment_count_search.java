package com.hkm.editorial.pages.articlePage;

import android.widget.TextView;

import com.hkm.disqus.api.model.Response;
import com.hkm.disqus.api.model.posts.Post;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by hesk on 15/12/15.
 */
public class comment_count_search implements Callback<Response<List<Post>>> {

    private TextView tc;
    private String _time_ago;

    public comment_count_search(TextView tv, String ago) {
        tc = tv;
        _time_ago = ago;
    }

    /**
     * Successful HTTP response.
     *
     * @param listResponse response
     * @param response     response
     */
    @Override
    public void success(Response<List<Post>> listResponse, retrofit.client.Response response) {
        if (tc != null) {
            StringBuilder b = new StringBuilder();
            b.append(_time_ago);
            b.append(" · ");
            b.append(listResponse.data.size());
            if (listResponse.data.size() > 1) {
                b.append(" comments");
            } else {
                b.append(" comment");
            }
            tc.setText(b.toString());
        }
    }

    /**
     * Unsuccessful HTTP response due to network failure, non-2XX status code, or unexpected
     * exception.
     *
     * @param error response
     */
    @Override
    public void failure(RetrofitError error) {

    }
}
