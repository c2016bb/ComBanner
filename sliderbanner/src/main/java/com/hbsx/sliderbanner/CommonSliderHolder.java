package com.hbsx.sliderbanner;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by User on 2017/12/26.
 */

public abstract class CommonSliderHolder<T> implements SliderHolder<T> {
    List<T> datas;

    public List<T> getDatas() {
        return datas;
    }

    public CommonSliderHolder(List<T> datas) {
        this.datas = datas;
    }

    @Override
    public SliderViewHolder createView(Context context, ViewGroup parent, int position, int viewType) {
        return SliderViewHolder.createViewHolder(context, parent, getLayoutID(position%datas.size()), getViewType(position));
    }

    @Override
    public void UpdateUI(Context context, SliderViewHolder viewHolder, int position, T data) {
              updateView(viewHolder,position%datas.size(),data);
    }

    @Override
    public int getViewType(int position) {
        return Integer.MIN_VALUE;
    }
    public abstract @LayoutRes int getLayoutID(int position);

    public abstract void updateView(SliderViewHolder viewHolder, int position, T data);


}
