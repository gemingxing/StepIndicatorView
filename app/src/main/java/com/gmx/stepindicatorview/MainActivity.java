package com.gmx.stepindicatorview;

import android.os.Bundle;
import android.util.Log;

import com.gmx.stepview.StepIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    StepIndicatorView mStepIndicatorView0;
    private StepIndicatorView mStepIndicatorView3;
    private StepIndicatorView mStepIndicatorView1;
    private StepIndicatorView mStepIndicatorView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStepIndicatorView0 = (StepIndicatorView) findViewById(R.id.stepView0);
        mStepIndicatorView1 = (StepIndicatorView) findViewById(R.id.stepView1);
        mStepIndicatorView2 = (StepIndicatorView) findViewById(R.id.stepView2);
        mStepIndicatorView3 = (StepIndicatorView) findViewById(R.id.stepView3);

        setUp0();
        setUp1();
        setUp2();
        setUp3();


        float density = getResources().getDisplayMetrics().density;
        float densityDPI = getResources().getDisplayMetrics().densityDpi;
        int heightPixels = getResources().getDisplayMetrics().heightPixels;
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        float xdpi = getResources().getDisplayMetrics().xdpi;
        float ydpi = getResources().getDisplayMetrics().ydpi;
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;

        Log.e("TAG", "density: " + density);
        Log.e("TAG", "densityDPI: " + densityDPI);
        Log.e("TAG", "heightPixels: " + heightPixels);
        Log.e("TAG", "widthPixels: " + widthPixels);
        Log.e("TAG", "xdpi: " + xdpi);
        Log.e("TAG", "ydpi: " + ydpi);
        Log.e("TAG", "scaledDensity: " + scaledDensity);
    }

    private void setUp3() {
        List<String> list = new ArrayList<>();
        list.add("接单");
        list.add("打包");
        list.add("出发");
        list.add("送单");
        list.add("完成");
        list.add("支付");

        mStepIndicatorView3.initStepIndicatorViewTextList(list)
                .initStepIndicatorCount(list.size())
                .initStepIndicatorViewCompletedPosition(3);
    }

    private void setUp2() {
        List<String> list = new ArrayList<>();
        list.add("接单");
        list.add("打包");
        list.add("出发");
        list.add("送单");
        list.add("完成");
        list.add("支付");

        mStepIndicatorView2.initStepIndicatorViewTextList(list)
                .initStepIndicatorCount(list.size())
                .initStepIndicatorViewCompletedPosition(2);
    }

    private void setUp1() {
        List<String> list = new ArrayList<>();
        list.add("接单");
        list.add("打包");
        list.add("出发");
        list.add("送单");
        list.add("完成");
        list.add("支付");

        mStepIndicatorView1.initStepIndicatorViewTextList(list)
                .initStepIndicatorCount(list.size())
                .initStepIndicatorViewCompletedPosition(1);
    }

    private void setUp0() {
        List<String> list = new ArrayList<>();
        list.add("接单");
        list.add("打包");
        list.add("出发");
        list.add("送单");
        list.add("完成");
        list.add("支付");

        mStepIndicatorView0.initStepIndicatorViewTextList(list)
                .initStepIndicatorCount(list.size())
                .initStepIndicatorViewCompletedPosition(0);

    }


}
