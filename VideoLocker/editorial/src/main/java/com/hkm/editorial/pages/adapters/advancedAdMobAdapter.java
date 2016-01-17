package com.hkm.editorial.pages.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.hkm.editorial.R;
import com.hypebeast.sdk.api.model.hbeditorial.ArticleData;
import com.marshalchen.ultimaterecyclerview.AdmobAdapter;
import com.marshalchen.ultimaterecyclerview.URLogs;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.neopixl.pixlui.components.textview.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hesk on 20/5/15.
 */
public class advancedAdMobAdapter extends AdmobAdapter {

    final Picasso pica;

    class ViewHolder extends UltimateRecyclerviewViewHolder {

        TextView topline, article_topic, commentline;
        ImageView imageViewSample;

        public ViewHolder(View itemView) {
            super(itemView);
            topline = (TextView) itemView.findViewById(R.id.nd_line);
            article_topic = (TextView) itemView.findViewById(R.id.articletopic);
            commentline = (TextView) itemView.findViewById(R.id.comment);
            imageViewSample = (ImageView) itemView.findViewById(R.id.big_image_single);
        }
    }

    public advancedAdMobAdapter(PublisherAdView v, int e, List<ArticleData> f, Context ctx) {
        super(v, false, e, f);
        pica = Picasso.with(ctx);
    }


    public advancedAdMobAdapter(AdView v, int e, List<ArticleData> f, Context ctx) {
        super(v, false, e, f);
        pica = Picasso.with(ctx);
    }

    public advancedAdMobAdapter(AdView v, int e, List<ArticleData> f, AdviewListener listener, Context ctx) {
        super(v, false, e, f, listener);
        pica = Picasso.with(ctx);
    }


    /**
     * the layout id for the normal data
     *
     * @return the ID
     */
    @Override
    protected int getNormalLayoutResId() {
        return R.layout.item_general;
    }

    /**
     * create a new view holder for data binding
     *
     * @param mview the view layout with resource initialized
     * @return the view type
     */
    @Override
    protected UltimateRecyclerviewViewHolder newViewHolder(View mview) {
        return new ViewHolder(mview);
    }

    @Override
    public UltimateRecyclerviewViewHolder getViewHolder(View view) {
        return new UltimateRecyclerviewViewHolder(view);
    }

    /**
     * Returns the number of items in the adapter bound to the parent RecyclerView.
     *
     * @return The number of items in the bound adapter
     */
    @Override
    public int getAdapterItemCount() {
        return list.size();
    }


    @Override
    public long generateHeaderId(int position) {
        URLogs.d("position--" + position + "   " + getItem(position));
        if (getItem(position).length() > 0)
            return getItem(position).charAt(0);
        else return -1;
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the {@link ViewHolder#itemView} to reflect the item at
     * the given position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this
     * method again if the position of the item changes in the data set unless the item itself
     * is invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside this
     * method and should not keep a copy of it. If you need the position of an item later on
     * (e.g. in a click listener), use {@link ViewHolder#getPosition()} which will have the
     * updated position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (onActionToBindData(position, list)) {
            //  ((ViewHolder) holder).textViewSample.setText((String) list.get(getDataArrayPosition(position)));
            int corrected_position = getDataArrayPosition(position);

            try {
                ArticleData d = (ArticleData) list.get(corrected_position);
                ViewHolder h = (ViewHolder) holder;
                h.topline.setText(d.getCate());
                h.article_topic.setText(d.getTitle());
                h.commentline.setText(d.getMoment() + d.getShortCommentCount());
                pica.load(d.get_links().getThumbnail()).into(h.imageViewSample);
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

    public String getItem(int position) {
        return (String) super.getItem(position);
    }


    @Override
    public long getHeaderId(int position) {
        if (position == 0) {
            return -1;
        } else {
            return getItem(position).charAt(0);
        }
    }


    public void insert(String string, int position) {
        insert(list, string, position);
    }

    public void remove(int position) {
        remove(list, position);
    }

    public void clear() {
        clear(list);
    }

    @Override
    public void toggleSelection(int pos) {
        super.toggleSelection(pos);
    }

    @Override
    public void setSelected(int pos) {
        super.setSelected(pos);
    }

    @Override
    public void clearSelection(int pos) {
        super.clearSelection(pos);
    }


    public void swapPositions(int from, int to) {
        swapPositions(list, from, to);
    }
}
