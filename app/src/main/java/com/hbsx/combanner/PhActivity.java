package com.hbsx.combanner;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hbsx.sliderbanner.CommonSliderHolder;
import com.hbsx.sliderbanner.DefaultPageIndicator;
import com.hbsx.sliderbanner.SliderBanner;
import com.hbsx.sliderbanner.SliderHolder;
import com.hbsx.sliderbanner.SliderImageHolder;
import com.hbsx.sliderbanner.SliderViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PhActivity extends AppCompatActivity {
   SliderBanner sliderBanner;
   List<Integer>images;
 List<String>datas;

    DefaultPageIndicator defaultPageIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ph);
        sliderBanner=findViewById(R.id.sb);
    images=new ArrayList<>();
     images.add(R.mipmap.banner_icon_new1);
     images.add(R.mipmap.banner_icon_new2);
     images.add(R.mipmap.banner_icon_new3);
        datas=new ArrayList<>();
        datas.add("第一个");
        datas.add("第二个");
        datas.add("第三个");
        defaultPageIndicator = new DefaultPageIndicator(this);
        defaultPageIndicator.setPageIndicator(Common.indicatorGrouop);


//        sliderBanner.setData(images);
//        sliderBanner.setPages(new SliderImageHolder(images) {
//            @Override
//            public void updateImage(ImageView imageview, int position) {
//                imageview.setImageResource(images.get(position));
//            }
//        });
        sliderBanner.setPages(new CommonSliderHolder<Integer>(images) {
            @Override
            public int getLayoutID(int position) {
                return R.layout.layout_slider_banner_image;
            }

            @Override
            public void updateView(SliderViewHolder viewHolder, int position, Integer data) {
                       viewHolder.setImageResource(R.id.sliderbannerimage,data);
                       viewHolder.setText(R.id.text123,datas.get(position));
            }

            @Override
            public int getViewType(int position) {
                return 5;
            }
        }).setDefaultPageIndicator();

    }

public  void dianji(View v){
    Toast.makeText(this, "gaodu--->"+sliderBanner.getMeasuredHeight(), Toast.LENGTH_SHORT).show();


}


}
