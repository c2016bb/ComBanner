package com.hbsx.sliderbanner;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by User on 2017/12/18.
 */

public interface SliderHolder<T> {
    SliderViewHolder createView(Context context,
                                ViewGroup parent, int position, int viewType);
    void UpdateUI(Context context, SliderViewHolder viewHolder, int position, T data);
    int getViewType(int position);


}
