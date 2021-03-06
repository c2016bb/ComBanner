package com.hbsx.sliderbanner;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by User on 2017/12/18.
 */

public class SliderPagerAdapter<T> extends PagerAdapter {
    final String TAG = "WenldPagerAdapter";
    DataSetObservable mRealCanLoopObservable = new DataSetObservable(); //realCanLoop变化模式
    protected List<T> mDatas;
    SliderHolder holderCreator;
    private boolean canLoop;
    private boolean realCanLoop = true;
    ViewPager wenldViewPager;
    private LinkedList<SliderViewHolder> mViewHolderCache = null;
    private LinkedList<SliderViewHolder> mViewHolderUsedCache = null;
    private OnPageClickListener onItemClickListener;

    @Override
    public int getCount() {
        return realCanLoop ? getRealCount() * 600 : getRealCount();
    }

    public int getRealCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getView(position, container);
        container.addView(view);
        return view;
    }

    public boolean myNotify = false;

    public void notifyDataSetChanged(boolean isRefresh) {
        myNotify = isRefresh;
        super.notifyDataSetChanged();
        myNotify = false;
    }

    @Override
    public int getItemPosition(Object object) {
        if (!myNotify) {
            return super.getItemPosition(object);
        } else {
            return POSITION_NONE;
        }
    }

    public int realPostiton2AdapterPosition(int curAdapterPosition, int realPosition) {
        if (realCanLoop) {
            int oldRealCur = adapterPostiton2RealDataPosition(curAdapterPosition);
            int toCur = curAdapterPosition + realPosition - oldRealCur;
            return toCur;
        }
        return realPosition >= 0 ? realPosition : 0;
    }

    public int startAdapterPosition(int dataPosition) {
        if (realCanLoop) {
            return getRealCount() * 300 + dataPosition;
        }
        return dataPosition;
    }

    /**
     * 返回真实数据的下标
     *
     * @param adapterPosition
     * @return
     */
    public int adapterPostiton2RealDataPosition(int adapterPosition) {
        int realCount = getRealCount();
        if (realCount == 0)
            return 0;
        return adapterPosition % realCount;
    }

    /**
     * 控制AdapterPosition范围
     *
     * @param adapterPosition
     * @return
     */
    public int controlAdapterPosition(int adapterPosition) {

        if (realCanLoop) {
            if (adapterPosition > getRealCount() * 400 || adapterPosition < getRealCount() * 200) {
                return startAdapterPosition(adapterPostiton2RealDataPosition(adapterPosition));
            }
        }
        return adapterPosition;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);

        SliderViewHolder viewHolder = null;
        for (int i = mViewHolderUsedCache.size() - 1; i >= 0; i--) {
            viewHolder = mViewHolderUsedCache.get(i);

            if (viewHolder.getPosition() == position) {
                mViewHolderUsedCache.remove(viewHolder);
                break;
            }
            viewHolder = null;
        }
        if (viewHolder != null) {
            mViewHolderCache.add(viewHolder);
        }
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        boolean loop = canLoop ? (getRealCount() > 1 ? true : false) : false;
        if (realCanLoop ^ loop) {
            realCanLoop = loop;
            mRealCanLoopObservable.notifyChanged();
        }
    }

    public void setOnItemClickListener(OnPageClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setViewPager(ViewPager viewPager) {
        this.wenldViewPager = viewPager;
    }

    public SliderPagerAdapter(SliderHolder holderCreator) {
        this.holderCreator = holderCreator;
        mViewHolderCache = new LinkedList<>();
        mViewHolderUsedCache = new LinkedList<>();
        setCanLoop(true);
    }

    private SliderPagerAdapter(SliderHolder holderCreator, List<T> datas) {
        this.holderCreator = holderCreator;
        this.mDatas = datas;
        setCanLoop(true);
    }

    public void setmDatas(List<T> mDatas) {
        this.mDatas = mDatas;
        setCanLoop(canLoop);
    }

    public List<T> getmDatas() {
        return mDatas;
    }

    public View getView(int position, ViewGroup container) {
        SliderViewHolder holder = null;
        final int realPosition = adapterPostiton2RealDataPosition(position);
        int viewType = holderCreator.getViewType(realPosition);

        for (int i = mViewHolderCache.size() - 1; i >= 0; i--) {
            if (mViewHolderCache.get(i).getViewType() == viewType && mViewHolderCache.get(i).getPosition() == position) {
                holder = mViewHolderCache.get(i);
                mViewHolderCache.remove(holder);
                break;
            }
        }
        if (holder == null) {
            for (int i = mViewHolderCache.size() - 1; i >= 0; i--) {
                if (mViewHolderCache.get(i).getViewType() == viewType) {
                    holder = mViewHolderCache.get(i);
                    mViewHolderCache.remove(holder);
                    break;
                }
            }
        }

        if (holder == null) {
            holder = holderCreator.createView(wenldViewPager.getContext(), container, realPosition, viewType);
        }
        mViewHolderUsedCache.add(holder);
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(realPosition);
                }
            }
        });

        if (mDatas != null && !mDatas.isEmpty()) {
            if (myNotify || position != holder.getPosition()) {
                // 恢复一下状态
                holderCreator.UpdateUI(container.getContext(), holder, realPosition, mDatas.get(realPosition));
            }
        }
        holder.setPosition(position);
        return holder.getConvertView();
    }

    public boolean isRealCanLoop() {
        return realCanLoop;
    }

    public void registerRealCanLoopObserver(DataSetObserver observer) {
        mRealCanLoopObservable.registerObserver(observer);
    }

}
