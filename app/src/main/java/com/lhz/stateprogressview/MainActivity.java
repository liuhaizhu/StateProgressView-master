package com.lhz.stateprogressview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lhz.stateprogress.StateProgressView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final List<String> list = new ArrayList<String>();
        list.add("已下单");
        list.add("已付款");
        list.add("已发货");
        list.add("已收货");
        list.add("已评价");
        final StateProgressView stateProgressView01 = (StateProgressView) findViewById(R.id.spv_01);
        final StateProgressView stateProgressView02 = (StateProgressView) findViewById(R.id.spv_02);
        final StateProgressView stateProgressView03 = (StateProgressView) findViewById(R.id.spv_03);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stateProgressView01.setItems(list, 4, 200);

                stateProgressView02.setItems(list);
                stateProgressView02.startAnim(2,1000);

                stateProgressView03.setItems(4);
                stateProgressView03.startAnim(2,1000);
            }
        });

    }
}
