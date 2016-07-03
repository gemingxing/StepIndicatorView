package com.gmx.stepindicatorview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.gmx.stepview.Orientation;
import com.gmx.stepview.StepIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    StepIndicatorView mStepIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        step0();

        step1();
    }


    private void step1() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> list0 = new ArrayList<>();
                list0.add("接已提交定案，等待系统确认");
                list0.add("您的商品需要从外地调拨，我们会尽快处理，请耐心等待");
                list0.add("您的订单已经进入亚洲第一仓储中心1号库准备出库");
                list0.add("您的订单预计6月23日送达您的手中，618期间促销火爆，可能影响送货时间，请您谅解，我们会第一时间送到您的手中");
                list0.add("您的订单已打印完毕");
                list0.add("您的订单已拣货完成");
                list0.add("扫描员已经扫描");
                list0.add("打包成功");
                list0.add("配送员【包牙齿】已出发，联系电话【130-0000-0000】，感谢您的耐心等待，参加评价还能赢取好多礼物哦");
                list0.add("感谢你在京东购物，欢迎你下次光临！");

                mStepIndicatorView.setStepIndicatorViewTextList(list0);
            }
        });


        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.valueOf(((EditText) findViewById(R.id.editText)).getText().toString());
                mStepIndicatorView.setStepIndicatorViewCompletedPosition(position);

            }
        });
    }

    private void step0() {
        mStepIndicatorView = (StepIndicatorView) findViewById(R.id.step_view0);

        List<String> list0 = new ArrayList<>();

        mStepIndicatorView.initStepIndicatorCount(5)
                .initStepIndicatorViewCompletedPosition(3)
                .initStepIndicatorViewOrientation(Orientation.VERTICAL);

    }


}
