package com.hkm.layout.Menu;

/**
 * Created by hesk on 21/9/15.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

public class TabIconView extends ImageView {

    public static class Icon {
        public final String title;
        public final int normal, active;
        boolean type_res_vector = false;

        public Icon(final String label, final int mIconRes_normal, final int mIconRes_active) {
            title = label;
            normal = mIconRes_normal;
            active = mIconRes_active;
        }

        private void setVectorEnabled() {
            type_res_vector = true;
        }


        public boolean isScalable() {
            return type_res_vector;
        }
    }

    private
    @DrawableRes
    int normal, active, current;

    public static Icon newIconTab(String label, int normal, int active) {
        return new Icon(label, normal, active);
    }

    public static Icon newVectorIconTab(String label, int normal, int active) {
        Icon c = new Icon(label, active, normal);
        c.setVectorEnabled();
        return c;
    }

    private Bubble mBubble;
    private Paint mPaint;
    private Bitmap mSelectedIcon;
    private Bitmap mNormalIcon;
    private Rect mSelectedRect;
    private Rect mNormalRect;
    private int mSelectedAlpha = 0;

    public TabIconView(Context context) {
        super(context);
    }

    public TabIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public final TabIconView addBubble(Bubble bubble) {
        mBubble = bubble;
        return this;
    }

    public final void initVector(int normal, int selected) {
        this.active = selected;
        this.normal = normal;
        current = 0;
        setImageResource(normal);
    }

    public final void init(int normal, int selected) {
        this.mNormalIcon = createBitmap(normal);
        this.mSelectedIcon = createBitmap(selected);
        this.mNormalRect = new Rect(0, 0, this.mNormalIcon.getWidth(), this.mNormalIcon.getHeight());
        this.mSelectedRect = new Rect(0, 0, this.mSelectedIcon.getWidth(), this.mSelectedIcon.getHeight());
        this.mPaint = new Paint(1);
        if (mBubble != null) {
            mBubble.setRectConfiguration(mNormalRect);
        }
    }

    private Bitmap createDrawable(final @DrawableRes int resId) {
        Drawable rs = ContextCompat.getDrawable(getContext(), resId);
        return ((BitmapDrawable) rs).getBitmap();
    }

    private Bitmap createVector(final @DrawableRes int resId) {
        //  Drawable drawable = VectorDrawableCompat.inflate(getResources(), R.drawable.your_vector);
        Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
        return ((BitmapDrawable) drawable).getBitmap();
    }

    private Bitmap createBitmap(int resId) {
        return BitmapFactory.decodeResource(getResources(), resId);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int current_alpha = 255 - this.mSelectedAlpha;
        if (this.mPaint == null) {
            if (this.mNormalIcon == null || this.mSelectedIcon == null) {
                if (current_alpha == 0 && current != normal) {
                    current = normal;
                    setImageResource(current);
                }
                if (current_alpha == 255 && current != active) {
                    current = active;
                    setImageResource(current);
                }
            }
            return;
        }
        this.mPaint.setAlpha(current_alpha);
        canvas.drawBitmap(this.mNormalIcon, null, this.mNormalRect, this.mPaint);
        this.mPaint.setAlpha(this.mSelectedAlpha);
        canvas.drawBitmap(this.mSelectedIcon, null, this.mSelectedRect, this.mPaint);
        if (mBubble != null) {
            mBubble.onDraw(canvas);
        }
    }

    public void updateBadget(int display_num) {
        if (mBubble != null) {
            mBubble.updateNumber(display_num);
            invalidate();
        }
    }

    public final void changeSelectedAlpha(int alpha) {
        this.mSelectedAlpha = alpha;
        invalidate();
    }

    public final void transformPage(float offset) {
        changeSelectedAlpha((int) (255 * (1 - offset)));
    }


}