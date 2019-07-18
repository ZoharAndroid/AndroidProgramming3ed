package com.zohar.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BoxDrawingView extends View {

    private static final String TAG = "BoxDrawingView";

    private Box mBox;
    private List<Box> mBoxes = new ArrayList<>();

    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    public BoxDrawingView(Context context) {
        super(context);
    }

    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);


        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    int pointerId1 = -1;
    int pointerId2 = -1;
    float x1 = 0, x2 = 0, y1= 0, y2 = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mBox = new Box(current);
                mBoxes.add(mBox);
                action = "down";
                // 获取第一个手指按下的point id
                pointerId1 = event.getPointerId(event.getActionIndex());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                action = "pointer_down";
                // 获取第二个手指按下的 point id
                pointerId2 = event.getPointerId(event.getActionIndex());

                // 分别获取两个点的横纵坐标
                x1 = event.getX(event.findPointerIndex(pointerId1));
                y1 = event.getY(event.findPointerIndex(pointerId1));
                x2 = event.getX(event.findPointerIndex(pointerId2));
                y2 = event.getY(event.findPointerIndex(pointerId2));
                mBox = null;
                break;
            case MotionEvent.ACTION_UP:
                mBox = null;
                action = "up";
                pointerId1 = -1;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mBox = null;
                action = "pointer_up";
                pointerId2 = -1;
                break;
            case MotionEvent.ACTION_MOVE:

                if (pointerId1 != -1 && pointerId2 != -1) {
                    // 获取移动后的位置
                    float nx1 = event.getX(event.findPointerIndex(pointerId1));
                    float ny1 = event.getY(event.findPointerIndex(pointerId1));
                    float nx2 = event.getX(event.findPointerIndex(pointerId2));
                    float ny2 = event.getY(event.findPointerIndex(pointerId2));

                    float mAngle = angleBetweenLines(x1, y1, x2, y2, nx1, ny1, nx2, ny2);
                }

                if(mBox!=null && pointerId2 == -1){
                    mBox.setCurrent(current);
                }
                invalidate();

//                if (mBox != null) {
//                    mBox.setCurrent(current);
//                    invalidate();
//                }
                action = "move";
                break;
        }

        Log.d(TAG, action + " x = " + current.x + "; y = " + current.y);

        return true;
    }


    public float angleBetweenLines(float fx, float fy, float sx, float sy, float nfx, float nfy, float nsx, float nsy) {
        float radian1 = (float) Math.atan2(fy - sy, fx - sx);
        float radian2 = (float) Math.atan2(nfy - nsy, nfx - nsx);
        float angle = (float) (Math.toDegrees(radian2 - radian1) % 360);
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawPaint(mBackgroundPaint);

        for (Box box : mBoxes) {
            float left = Math.min(box.getOrign().x, box.getCurrent().x);
            float right = Math.max(box.getOrign().x, box.getCurrent().x);
            float top = Math.min(box.getOrign().y, box.getCurrent().y);
            float bottom = Math.max(box.getCurrent().y, box.getCurrent().y);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        // 保存父类的状态
        bundle.putParcelable("onSaveInstanceState", super.onSaveInstanceState());
        // 保存当前view的状态
        bundle.putSerializable("boxes", (Serializable) mBoxes);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // 读取保存的值
        Bundle bundle = (Bundle) state;

        Parcelable restoreState = bundle.getParcelable("onSaveInstanceState");// 恢复父类的
        super.onRestoreInstanceState(restoreState);
        mBoxes = (List<Box>) bundle.getSerializable("boxes");
    }
}
