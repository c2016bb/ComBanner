package com.hbsx.combanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hbsx.sliderbanner.AutoTurnViewPager;
import com.hbsx.sliderbanner.DefaultPageIndicator;
import com.hbsx.sliderbanner.OnPageClickListener;
import com.hbsx.sliderbanner.UIContact;

public class CustomUseActivity extends AppCompatActivity {
    AutoTurnViewPager autoTurnViewPager;
    DefaultPageIndicator defaultPageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_use);
        autoTurnViewPager =  findViewById(R.id.autoTurnViewPager);
        defaultPageIndicator =  findViewById(R.id.defaultPageIndicator);

        autoTurnViewPager.setPages(Common.holder)
                .setCanTurn(true)
                .setScrollDuration(1000);
        autoTurnViewPager.setPageTransformer(new ZoomOutPageTransformer());

        defaultPageIndicator.setPageIndicator(Common.indicatorGrouop);

        autoTurnViewPager.setOnItemClickListener(new OnPageClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(CustomUseActivity.this, "点击了第"+position+"页", Toast.LENGTH_SHORT).show();
            }
        });
        UIContact.with(autoTurnViewPager, defaultPageIndicator)
                .setData(Common.datas);

    }

    public void notifyDataSetChanged(View view) {
        autoTurnViewPager.getAdapter().notifyDataSetChanged();
    }
}
