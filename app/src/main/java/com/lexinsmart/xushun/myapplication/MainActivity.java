package com.lexinsmart.xushun.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lexinsmart.xushun.myapplication.cg.CGLine;
import com.lexinsmart.xushun.myapplication.cg.CGPoint;

import java.util.ArrayList;
import java.util.List;

import static com.lexinsmart.xushun.myapplication.cg.CGGeometryLib.cos;
import static com.lexinsmart.xushun.myapplication.cg.CGGeometryLib.getWhatIWanted;
import static com.lexinsmart.xushun.myapplication.cg.CGGeometryLib.sin;

public class MainActivity extends AppCompatActivity {
    BasesBean basesBean = new BasesBean();
    List<BasesBean.BaseBean> base = new ArrayList<>();
    PositionBg mPositionBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPositionBg = (PositionBg) findViewById(R.id.basesPosition);

        BasesBean.BaseBean baseBean = new BasesBean.BaseBean();
        baseBean.setBasemac("1");
        baseBean.setBasex(0);
        baseBean.setBasey(0);
        baseBean.setR(1);

        BasesBean.BaseBean baseBean2 = new BasesBean.BaseBean();
        baseBean2.setBasemac("2");
        baseBean2.setBasex(4.2);
        baseBean2.setBasey(0);
        baseBean2.setR(1);

        BasesBean.BaseBean baseBean3 = new BasesBean.BaseBean();
        baseBean3.setBasemac("3");
        baseBean3.setBasex(2.1);
        baseBean3.setBasey(2.1);
        baseBean3.setR(1);

        base.add(baseBean);
        base.add(baseBean2);
        base.add(baseBean3);
        basesBean.setBase(base);

        mPositionBg.setData(basesBean);



        double currentRadian = 0;
        double deltaRadian = Math.PI / 360;
        double bigRadius = 50;
        double smallRadius = 20;
        CGPoint origin = new CGPoint(0, 0); // 原点~
        CGPoint tail = null;    // tail 是尾巴、末梢的意思~
        for(int i = 0; i < 720; ++ i) {
            System.out.println(" -- 第 "+ i + "度!");
            tail = new CGPoint(bigRadius*cos(currentRadian), bigRadius*sin(currentRadian));
            currentRadian += deltaRadian;
            getWhatIWanted(origin, tail, smallRadius);
        }


    }
}
