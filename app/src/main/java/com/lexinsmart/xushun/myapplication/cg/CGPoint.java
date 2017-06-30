package com.lexinsmart.xushun.myapplication.cg;

/**
 * Created by xushun on 2017/6/20.
 */

public class CGPoint {
    public double x;
    public double y;

    public CGPoint() {

    }
    public CGPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "x=" + this.x + ", y=" + this.y;
    }
}