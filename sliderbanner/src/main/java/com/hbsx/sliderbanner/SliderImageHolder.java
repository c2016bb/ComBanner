package com.hbsx.sliderbanner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by User on 2017/12/25.
 */

public  abstract class SliderImageHolder<T> implements   SliderHolder<T> {
    List<T>datas;

    public List<T> getDatas() {
        return datas;
    }

    public SliderImageHolder(List<T> datas) {
        this.datas = datas;
    }

    @Override
    public SliderViewHolder createView(Context context, ViewGroup parent, int position, int viewType) {
        return SliderViewHolder.createViewHolder(context, parent, R.layout.layout_slider_banner_image, getViewType(position));
    }

    @Override
    public void UpdateUI(Context context, SliderViewHolder viewHolder, int position, T data) {
            updateImage((ImageView) viewHolder.getView(R.id.sliderbannerimage),position%datas.size());
    }

    @Override
    public int getViewType(int position) {
        return Integer.MIN_VALUE;
    }

   public abstract void  updateImage(ImageView imageview, int position);

}
