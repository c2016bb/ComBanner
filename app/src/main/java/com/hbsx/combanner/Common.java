package com.hbsx.combanner;

import android.content.Context;
import android.view.ViewGroup;

import com.hbsx.sliderbanner.SliderHolder;
import com.hbsx.sliderbanner.SliderViewHolder;


import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Author: 温利东 on 2017/11/7 14:18.
 * blog: http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */

public class Common {
    public static List<String> datas;
    public static List<String> datas2;
    public static SliderHolder<String> holder;
    public static int[] indicatorGrouop;
    public static int[] bgColors;
    public static int[] indicatorColors;

    static {
        datas = new ArrayList<>();        datas2 = new ArrayList<>();
        datas.add("第一个");
        datas.add("第二个");
        datas.add("第三个");

        datas2.add("datas 2第一个");
        datas2.add("datas 2第二个");
        datas2.add("datas 2第三个");
        datas2.add("datas 2第四个");
        datas2.add("datas 2第五个");
        datas2.add("datas 2第六个");

        bgColors = new int[]{0xff66cccc, 0xffccff66, 0xffff99cc};
        indicatorColors = new int[]{0xffff0000, 0xffffff66, 0xff666633};

        holder = new SliderHolder<String>() {
            @Override
            public SliderViewHolder createView(Context context, ViewGroup parent, int pos, int viewType) {
                return SliderViewHolder.createViewHolder(context, parent, R.layout.layout_text, getViewType(pos));
            }

            @Override
            public void UpdateUI(Context context, SliderViewHolder viewHolder, int position, String data) {
                viewHolder.setText(R.id.tv, data);
                viewHolder.setBackgroundColor(R.id.tv, bgColors[position % bgColors.length]);
            }

            @Override
            public int getViewType(int position) {
                return 0;
            }
        };

        indicatorGrouop = new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused};
    }
}
