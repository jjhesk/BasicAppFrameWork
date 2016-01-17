package com.hkm.downloadmedialocker.pages.adapters;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hkm.downloadmedialocker.R;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.neopixl.pixlui.components.textview.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hesk on 29/4/15.
 */
public class shareactionSpline extends UltimateViewAdapter implements DialogInterface.OnClickListener {

    private List<ResolveInfo> stringList;
    final Picasso pica;
    private PackageManager pm;
    private Context mcontext;

    public shareactionSpline(List<ResolveInfo> strlist, Context ctx) {
        stringList = strlist;
        pica = Picasso.with(ctx);
        mcontext = ctx;
        pm = mcontext.getPackageManager();
    }

    public List<ResolveInfo> getListContent() {
        return stringList;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= stringList.size() : position < stringList.size()) && (customHeaderView != null ? position > 0 : true)) {
            try {
                ResolveInfo d = stringList.get(customHeaderView != null ? position - 1 : position);
                ViewHolder h = (ViewHolder) holder;
                h.commentline.setText(d.loadLabel(pm));
                h.imageViewSample.setImageDrawable(d.loadIcon(pm));
                //  pica.l(d.loadIcon(pm)).into(h.imageViewSample);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected int getResLayoutId() {
        return R.layout.item_share;
    }

    @Override
    public int getAdapterItemCount() {
        return stringList.size();
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
    public void toggleSelection(int pos) {
        super.toggleSelection(pos);
    }

    @Override
    public void setSelected(int pos) {
        super.setSelected(pos);
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    @Override
    public void clearSelection(int pos) {
        super.clearSelection(pos);
    }


    public void swapPositions(int from, int to) {
        swapPositions(stringList, from, to);
    }

    /**
     * This method will be invoked when a button in the dialog is clicked.
     *
     * @param dialog The dialog that received the click.
     * @param which  The button that was clicked (e.g.
     *               {@link android.content.DialogInterface#BUTTON1}) or the position
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        ActivityInfo activity = stringList.get(which).activityInfo;
        ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);

        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "share content in here");

        // Intent newIntent = (Intent) shareIntent.clone();
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        shareIntent.setComponent(name);
        mcontext.startActivity(shareIntent);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }


    class ViewHolder extends UltimateRecyclerviewViewHolder {

        private TextView commentline;
        private ImageView imageViewSample;
        // ProgressBar progressBarSample;

        public ViewHolder(View itemView) {
            super(itemView);
            commentline = (TextView) itemView.findViewById(R.id.textframe);
            imageViewSample = (ImageView) itemView.findViewById(R.id.itemframe);
            // progressBarSample = (ProgressBar) itemView.findViewById(R.id.progressbar);
            //  progressBarSample.setVisibility(View.GONE);
            // imageViewSample.setVisibility(View.INVISIBLE);
        }
      /*  public String getItem(int position) {
            if (customHeaderView != null)
                position--;
            if (position < stringList.size())
                return stringList.get(position);
            else return "";
        }*/
    }

}
