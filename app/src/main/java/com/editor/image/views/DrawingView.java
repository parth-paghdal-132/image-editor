package com.editor.image.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.editor.image.models.Brush;

public class DrawingView extends View {
    Paint paint;
    Bitmap bitmap;
    Canvas canvas;
    Path path;
    Paint mbitmappaint;
    Brush brush = new Brush(10, Color.RED);

    private boolean commited = false;
    public DrawingView(Context context) {
        super(context);
        init();
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(brush.getColor());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(brush.getRadius());

        path = new Path();
        mbitmappaint = new Paint();
        mbitmappaint.setColor(Color.RED);
    }

    public void setBrush(Brush brush) {
        this.brush = brush;
        init();
    }

    public void commitChanges(boolean save) {
        commited = save;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, mbitmappaint);
        canvas.drawPath(path, paint);
    }
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;


    private void touchStart(float x, float y) {
        path.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    private void touchUp() {
        path.lineTo(mX, mY);
        canvas.drawPath(path, paint);
        path.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (commited) return false;
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }
}
