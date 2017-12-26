package com.hbsx.sliderbanner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

/**
 * Created by User on 2017/12/18.
 */

public class SliderBanner<T> extends RelativeLayout {
    AutoTurnViewPager viewPager;
    PageIndicatorListener pageIndicatorListener;
    View indicatorView;
    List<T> mDatas;
    UIContact uiContact;

    public SliderBanner(@NonNull Context context) {
        this(context, null);
    }

    public SliderBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SliderBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initAttr(attrs);
    }
    private void init(Context context) {
        View  hView = LayoutInflater.from(context).inflate(
                R.layout.slider_banner, this, true);
//          hView.setBackgroundColor(Color.BLUE);
//          RelativeLayout.LayoutParams lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
//          hView.setLayoutParams(lp);
        viewPager = hView.findViewById(R.id.vp_wenld_banner);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
       final int specMode = MeasureSpec.getMode(heightMeasureSpec);
      final   int specSize = MeasureSpec.getSize(heightMeasureSpec);
//        Toast.makeText(getContext(), "specMode---->"+specMode, Toast.LENGTH_SHORT).show();
        if (specMode==MeasureSpec.EXACTLY){
            ViewGroup.LayoutParams params = viewPager.getLayoutParams();
            params.height = specSize;
            viewPager.setLayoutParams(params);
        }else  if (specMode==MeasureSpec.AT_MOST){
          int vheight= viewPager.getMeasuredHeight();
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height=vheight;
            setLayoutParams(params);
        }
    }

    public SliderBanner setPages(SliderHolder<T> holer, List<T> data) {
        viewPager.setPages(holer);
        setData(data);
        return this;
    }

    public SliderBanner setPages(SliderImageHolder holder){
        viewPager.setPages(holder);
        setData(holder.getDatas());
        return this;
    }

    public SliderBanner setPages(CommonSliderHolder holder){
        viewPager.setPages(holder);
        setData(holder.getDatas());
        return this;
    }


    private void initAttr(AttributeSet attrs) {
        setRunning(true); //是否正在执行翻页中   如果是canLoop=false  到头了 那就不翻页
        setCanLoop(true);// 是否循环
        setCanTurn(true);   //能否能执行自动翻页
//     LayoutParams lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
//     setLayoutParams(lp);


        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.sliderBanner);
        if (a != null) {
            int n = a.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.sliderBanner_canLoop) {
                    setCanLoop(a.getBoolean(attr, true));
                } else if (attr == R.styleable.sliderBanner_canTurn) {
                    setCanTurn(a.getBoolean(attr, true));
                } else if (attr == R.styleable.sliderBanner_isTouchScroll) {
                    setTouchScroll(a.getBoolean(attr, true));
                } else if (attr == R.styleable.sliderBanner_autoTurnTime) {
                    setAutoTurnTime(a.getInteger(attr, getAutoTurnTime()));
                } else if (attr == R.styleable.sliderBanner_scrollDuration) {
                    setScrollDuration(a.getInteger(attr, getScrollDuration()));
                } else if (attr == R.styleable.sliderBanner_reverse) {
                    setReverse(a.getBoolean(attr, false));
                }
            }
            a.recycle();
        }
    }

    /**
     * 设置底部指示器是否可见
     *
     * @param visible
     */
    public SliderBanner setInvaildViewVisible(boolean visible) {
        indicatorView.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * @param gravityAlign RelativeLayout
     * @return
     */
    public SliderBanner setPageIndicatorAlign(int... gravityAlign) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) indicatorView.getLayoutParams();
        for (int i = 0; i < gravityAlign.length; i++) {
            layoutParams.addRule(gravityAlign[i]);
        }
        layoutParams.setMargins(5, 5, 5, 20);
        indicatorView.setLayoutParams(layoutParams);
        return this;
    }

    public SliderBanner setIndicatorView(View indicatorView) {
        removeView(this.indicatorView);
        this.indicatorView = indicatorView;
         this.addView(this.indicatorView);
        return this;
    }
   public  SliderBanner setDefaultPageIndicator(){
       DefaultPageIndicator defaultPageIndicator=new DefaultPageIndicator(getContext());
               setPageIndicatorListener(defaultPageIndicator);
               setIndicatorView(defaultPageIndicator);
               setPageIndicatorAlign(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.CENTER_HORIZONTAL);
               return this;
   }
    public  SliderBanner setDefaultPageIndicator(int[] page_indicatorId,int... gravityAlign){
        DefaultPageIndicator defaultPageIndicator=new DefaultPageIndicator(getContext());
        defaultPageIndicator.setPageIndicator(page_indicatorId);
        setPageIndicatorListener(defaultPageIndicator);
        setIndicatorView(defaultPageIndicator);
        setPageIndicatorAlign(gravityAlign);
        return this;
    }

    public SliderBanner setPageIndicatorListener(PageIndicatorListener pageIndicatorListener) {
        this.pageIndicatorListener = pageIndicatorListener;
        getUiContact().addListener(pageIndicatorListener);
        return this;
    }

    public UIContact getUiContact() {

        if (uiContact == null) {
            uiContact = UIContact.with(viewPager, pageIndicatorListener);
        }
        return uiContact;
    }

    public boolean isCanTurn() {
        return viewPager.isCanTurn();
    }

    public SliderBanner startTurn() {
        viewPager.startTurn();
        return this;
    }

    public SliderBanner stopTurning() {
        viewPager.stopTurning();
        return this;
    }

    public void setData(List<T> mDatas) {
        this.mDatas = mDatas;
        getUiContact().setData(mDatas);
    }

    /**
     * 自定义翻页动画效果
     *
     * @param transformer
     * @return
     */
    public SliderBanner setPageTransformer(ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(true, transformer);
        return this;
    }

    public SliderBanner setScrollDuration(int duration) {
        viewPager.setScrollDuration(duration);
        return this;
    }

    public int getScrollDuration() {
        return viewPager.getScrollDuration();
    }

    public void setCurrentItem(int page) {
        stopTurning();
        viewPager.setCurrentItem(page);
        startTurn();
    }

    public boolean isRunning() {
        return viewPager.isRunning();
    }

    public SliderBanner setRunning(boolean running) {
        viewPager.setRunning(running);
        return this;
    }

    public boolean isCanLoop() {
        return viewPager.isCanLoop();
    }

    public SliderBanner setCanLoop(boolean canLoop) {
        viewPager.setCanLoop(canLoop);
        return this;
    }

    public SliderBanner setCanTurn(boolean canTurn) {
        viewPager.setCanTurn(canTurn);
        return this;
    }

    public void setTouchScroll(boolean isCanScroll) {
        viewPager.setTouchScroll(isCanScroll);
    }

    public int getAutoTurnTime() {
        return viewPager.getAutoTurnTime();
    }

    public SliderBanner setAutoTurnTime(int autoTurnTime) {
        viewPager.setAutoTurnTime(autoTurnTime);
        return this;
    }

    public void setReverse(boolean reverse) {
        viewPager.setReverse(reverse);
    }

    public boolean isReverse() {
        return viewPager.isReverse();
    }

    public void setOnItemClickListener(OnPageClickListener onItemClickListener) {
        viewPager.setOnItemClickListener(onItemClickListener);

    }

    public AutoTurnViewPager getViewPager() {
        return viewPager;
    }
}
