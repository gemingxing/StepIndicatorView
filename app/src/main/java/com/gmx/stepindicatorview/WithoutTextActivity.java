package com.gmx.stepindicatorview;

import android.os.Bundle;

import com.gmx.stepview.Orientation;
import com.gmx.stepview.StepIndicatorView;

public class WithoutTextActivity extends BaseActivity {

    StepIndicatorView mStepIndicatorView0, mStepIndicatorView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_without_text);

        mStepIndicatorView0 = (StepIndicatorView) findViewById(R.id.stepView0);
        mStepIndicatorView1 = (StepIndicatorView) findViewById(R.id.stepView1);


        mStepIndicatorView0.initStepIndicatorCount(5)
                .initStepIndicatorViewCompletedPosition(2);



        mStepIndicatorView1.initStepIndicatorCount(5)
                .initStepIndicatorViewCompletedPosition(2)
        .initStepIndicatorViewOrientation(Orientation.VERTICAL);

    }



}
