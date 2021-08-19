package com.example.wip_android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wip_android.fragments.AddImagePin;
import com.example.wip_android.fragments.DeficiencyFragment;

import java.util.ArrayList;

public class DrawView extends androidx.appcompat.widget.AppCompatImageView {

    float x, y;
    float a, b;
    float width = 60.0f;
    float height = 50.0f;
    int radius = 20;
    boolean touched = false;
    Paint paint = new Paint();
    ArrayList<Circle> listCircle = new ArrayList<>();
    int numberOfCircles;
    Fragment switchFragment;

    FragmentTransaction transaction;
    AddImagePin addImagePin;
    private final String TAG = this.getClass().getCanonicalName();

    public DrawView(Context context) {
        super(context);

    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (touched) {
            if (numberOfCircles > 0) {

                for (int i = 0; i < numberOfCircles; i++) {

                    paint.setColor(Color.RED);
                    paint.setStrokeWidth(10);
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);

                    a = listCircle.get(i).getX();
                    b = listCircle.get(i).getY();

                    canvas.drawCircle(a, b, radius, paint);
                }

            } else {
                Log.d(TAG, "onDraw: No Circle to draw");
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touched = true;
        // getting the touched x and y position
        x = event.getX();
        y = event.getY();
        listCircle.add(new Circle(x, y, radius));
        numberOfCircles = listCircle.size();
        Log.d(TAG, "onClick: Touch Listener DrawView : " + x + "," + y);
        Log.d(TAG, "onTouchEvent: listCircle list: " + listCircle);
        invalidate();
        return true;
    }
}
