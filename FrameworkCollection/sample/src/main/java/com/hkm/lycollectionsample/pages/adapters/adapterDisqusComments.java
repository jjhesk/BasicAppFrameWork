package com.hkm.lycollectionsample.pages.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hkm.disqus.api.model.posts.Post;
import com.hkm.lycollectionsample.R;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.neopixl.pixlui.components.textview.TextView;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by hesk on 30/4/15.
 */
public class adapterDisqusComments extends UltimateViewAdapter {
    private final ArrayList<Post> posts;
    final Picasso pica;

    public adapterDisqusComments(ArrayList<Post> postslist, Context ctx) {
        posts = postslist;
        pica = Picasso.with(ctx);
    }

    protected int getResLayoutId() {
        return R.layout.comment_blank;
    }

    @Override
    public UltimateRecyclerviewViewHolder getViewHolder(View view) {
        return new UltimateRecyclerviewViewHolder(view);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getResLayoutId(), parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return posts.size();
    }


    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    private String getMoment(Date inp) throws ParseException {
        PrettyTime p = new PrettyTime();
        return p.format(inp);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= posts.size() : position < posts.size()) && (customHeaderView != null ? position > 0 : true)) {
            try {
                Post d = posts.get(customHeaderView != null ? position - 1 : position);
                ViewHolder h = (ViewHolder) holder;
                h.commentor.setText(d.author.name);
                h.timestamp.setText(getMoment(d.createdAt));
                h.comment_field.setText(d.rawMessage);
                pica.load(d.author.avatar.permalink).into(h.icon);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    class ViewHolder extends UltimateRecyclerviewViewHolder {
        ImageView icon;
        TextView commentor;
        TextView timestamp;
        TextView comment_field;

        public ViewHolder(View itemView) {
            super(itemView);
            commentor = (TextView) itemView.findViewById(R.id.commentor);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            comment_field = (TextView) itemView.findViewById(R.id.comment_field);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }
}
