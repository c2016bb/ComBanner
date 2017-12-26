package com.hbsx.sliderbanner;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/12/18.
 */

public class LoopViewPager extends ViewPager {
    final String TAG = "LoopViewPager";
    private List<OnPageChangeListener> mOuterPageChangeListeners;
    private SliderPagerAdapter mAdapter;

    private boolean isTouchScroll = true;
    private boolean canLoop = true;

    public void setAdapter(SliderPagerAdapter adapter) {
        mAdapter = adapter;
        mAdapter.setCanLoop(canLoop);
        mAdapter.setViewPager(this);
        super.setAdapter(mAdapter);
        setSuperCurrentItem(mAdapter.startAdapterPosition(0), false);

        mAdapter.registerRealCanLoopObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                int cur = getCurrentItem();
                mAdapter.notifyDataSetChanged(true);
                setSuperCurrentItem(mAdapter.startAdapterPosition(cur), false);
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
            }
        });
    }

    public boolean isTouchScroll() {
        return isTouchScroll;
    }

    public void setTouchScroll(boolean isCanScroll) {
        this.isTouchScroll = isCanScroll;
    }


    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(mAdapter.realPostiton2AdapterPosition(getSuperCurrentItem(), item));
    }

    public void setSuperCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        int cur = mAdapter.realPostiton2AdapterPosition(getSuperCurrentItem(), item);
        Log.e(TAG, "setCurrentItem:" + cur);
        super.setCurrentItem(cur);
    }

    public int getSuperCurrentItem() {
        return super.getCurrentItem();
    }

    @Override
    public int getCurrentItem() {
        return mAdapter.adapterPostiton2RealDataPosition(super.getCurrentItem());
    }

    int getRealItem(int position) {
        return mAdapter.adapterPostiton2RealDataPosition(position);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isTouchScroll) {
            return super.onTouchEvent(ev);
        } else
            return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isTouchScroll)
            return super.onInterceptTouchEvent(ev);
        else
            return false;
    }

    public SliderPagerAdapter getAdapter() {
        return mAdapter;
    }


    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        getmOuterPageChangeListeners().add(listener);
    }

    List<OnPageChangeListener> getmOuterPageChangeListeners() {
        if (mOuterPageChangeListeners == null) {
            mOuterPageChangeListeners = new ArrayList<>();
        }
        return mOuterPageChangeListeners;
    }

    @Override
    public void removeOnPageChangeListener(OnPageChangeListener listener) {
        getmOuterPageChangeListeners().remove(listener);
    }

    public LoopViewPager(Context context) {
        super(context);
        init();
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        super.addOnPageChangeListener(onPageChangeListener);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // find the first child view
        View view = getChildAt(0);
        if (view != null) {
            // measure the first child view with the specified measure spec
            view.measure(widthMeasureSpec, heightMeasureSpec);
        }
       int viewHeight=measureHeight(heightMeasureSpec, view);
//        Toast.makeText(getContext(), "viewHeight--->"+viewHeight, Toast.LENGTH_SHORT).show();
        setMeasuredDimension(getMeasuredWidth(), viewHeight);
    }

    private int measureHeight(int measureSpec, View view) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
//        Toast.makeText(getContext(), "specMode--->"+specMode+"specSize---->"+specSize, Toast.LENGTH_SHORT).show();
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            // set the height from the base view if available
            if (view != null) {
                result = view.getMeasuredHeight();
            }
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }



    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        private float mPreviousPosition = -1;

        @Override
        public void onPageSelected(int position) {

            int realPosition = getRealItem(position);
            if (mPreviousPosition != realPosition) {
                mPreviousPosition = realPosition;
                Log.d(TAG, String.format("onPageSelected mPreviousPosition  %s", mPreviousPosition));
                for (int i = 0; i < getmOuterPageChangeListeners().size(); i++) {
                    getmOuterPageChangeListeners().get(i).onPageSelected(realPosition);
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            int realPosition = position;
            realPosition = mAdapter.adapterPostiton2RealDataPosition(realPosition);
            for (int i = 0; i < getmOuterPageChangeListeners().size(); i++) {
                getmOuterPageChangeListeners().get(i).onPageScrolled(realPosition,
                        positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == SCROLL_STATE_IDLE) {
                int currentItem = getSuperCurrentItem();
                int realAdapterPosition = mAdapter.controlAdapterPosition(currentItem);
                if (currentItem != realAdapterPosition) {
                    setSuperCurrentItem(realAdapterPosition, false);
                }
            }
            for (int i = 0; i < getmOuterPageChangeListeners().size(); i++) {
                getmOuterPageChangeListeners().get(i).onPageScrollStateChanged(state);
            }
        }
    };

    public boolean isCanLoop() {
        return canLoop;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (transformer != null) {
            final int scrollX = getScrollX();
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = getChildAt(i);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                if (lp.isDecor) continue;
                final float transformPos = (float) (child.getLeft() - scrollX) / getClientWidth();
                transformer.transformPage(child, transformPos);
            }
        }
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        if (mAdapter == null) return;

        mAdapter.setCanLoop(canLoop);

    }

    private int getClientWidth() {
        return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    PageTransformer transformer;

    @Override
    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        this.transformer = transformer;
        super.setPageTransformer(reverseDrawingOrder, transformer);
    }

    public void setOnItemClickListener(OnPageClickListener onItemClickListener) {
        Log.d(TAG, "setOnItemClickListener: mAdapter--->"+mAdapter);
        if (mAdapter != null){


            mAdapter.setOnItemClickListener(onItemClickListener);
        }
    }

}
