package com.zohar.draganddraw;

import android.graphics.PointF;

public class Box {

    private PointF mOrign;
    private PointF mCurrent;

    public Box(PointF orign) {
        mOrign = orign;
        mCurrent = orign;
    }

    public PointF getOrign() {
        return mOrign;
    }

    public void setOrign(PointF orign) {
        mOrign = orign;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }
}
