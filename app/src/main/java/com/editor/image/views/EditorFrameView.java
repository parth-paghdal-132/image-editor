package com.editor.image.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.editor.image.R;

public abstract class EditorFrameView extends FrameLayout {

    private BorderView borderView;
    private ImageView imgScale;
    private ImageView imgDelete;
    private ImageView imgEdit;

    private float scaleOrgX = -1, scaleOrgY = -1;
    // For moving
    private float moveOrgX =-1, moveOrgY = -1;

    private double centerX, centerY;

    private final static int BUTTON_SIZE_IN_DP = 30;
    private final static int SELF_SIZE_IN_DP = 100;

    protected abstract View getMainView();
    protected abstract void onChildClick();

    public EditorFrameView(Context context) {
        super(context);
        init(context);
    }

    public EditorFrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EditorFrameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        this.borderView = new BorderView(context);
        this.imgScale = new ImageView(context);
        this.imgDelete = new ImageView(context);
        this.imgEdit = new ImageView(context);

        this.imgScale.setImageResource(R.drawable.ic_resize);
        this.imgDelete.setImageResource(R.drawable.ic_close);
        this.imgEdit.setImageResource(R.drawable.ic_edit);

        this.setTag("DraggableViewGroup");
        this.imgScale.setTag("imgScale");

        int margin = convertDpToPixel(BUTTON_SIZE_IN_DP, getContext())/2;
        int size = convertDpToPixel(SELF_SIZE_IN_DP, getContext());

        LayoutParams this_params = new LayoutParams(size, size);
        this_params.gravity = Gravity.CENTER;

        LayoutParams mainLayoutParams =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mainLayoutParams.setMargins(margin,margin,margin,margin);

        LayoutParams borderLayoutParams =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        borderLayoutParams.setMargins(margin,margin,margin,margin);

        LayoutParams imgScaleLayoutParams =
                new LayoutParams(convertDpToPixel(BUTTON_SIZE_IN_DP, getContext()), convertDpToPixel(BUTTON_SIZE_IN_DP, getContext()));
        imgScaleLayoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;

        LayoutParams imgDeleteLayoutParams =
                new LayoutParams(convertDpToPixel(BUTTON_SIZE_IN_DP, getContext()), convertDpToPixel(BUTTON_SIZE_IN_DP, getContext()));
        imgDeleteLayoutParams.gravity = Gravity.TOP | Gravity.RIGHT;

        LayoutParams imgEditParams =
                new LayoutParams(convertDpToPixel(BUTTON_SIZE_IN_DP, getContext()), convertDpToPixel(BUTTON_SIZE_IN_DP, getContext()));
        imgEditParams.gravity = Gravity.TOP | Gravity.LEFT;

        this.setLayoutParams(this_params);
        this.addView(getMainView(), mainLayoutParams);
        this.addView(borderView, borderLayoutParams);
        this.addView(imgScale, imgScaleLayoutParams);
        this.addView(imgDelete, imgDeleteLayoutParams);
        this.addView(imgEdit, imgEditParams);

        this.setOnTouchListener(touchListener);
        this.imgScale.setOnTouchListener(touchListener);
        this.imgDelete.setOnClickListener(view -> {
            if(EditorFrameView.this.getParent()!=null){
                ViewGroup myCanvas = ((ViewGroup)EditorFrameView.this.getParent());
                myCanvas.removeView(EditorFrameView.this);
            }
        });
        this.imgEdit.setOnClickListener(v -> onChildClick());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {

            if(view.getTag().equals("DraggableViewGroup")) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        moveOrgX = event.getRawX();
                        moveOrgY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float offsetX = event.getRawX() - moveOrgX;
                        float offsetY = event.getRawY() - moveOrgY;
                        EditorFrameView.this.setX(EditorFrameView.this.getX()+offsetX);
                        EditorFrameView.this.setY(EditorFrameView.this.getY() + offsetY);
                        moveOrgX = event.getRawX();
                        moveOrgY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
            }else if(view.getTag().equals("imgScale")){
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        scaleOrgX = event.getRawX();
                        scaleOrgY = event.getRawY();


                        centerX = EditorFrameView.this.getX()+
                                ((View)EditorFrameView.this.getParent()).getX()+
                                (float)EditorFrameView.this.getWidth()/2;


                        //double statusBarHeight = Math.ceil(25 * getContext().getResources().getDisplayMetrics().density);
                        int result = 0;
                        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                        if (resourceId > 0) {
                            result = getResources().getDimensionPixelSize(resourceId);
                        }
                        double statusBarHeight = result;
                        centerY = EditorFrameView.this.getY()+
                                ((View)EditorFrameView.this.getParent()).getY()+
                                statusBarHeight+
                                (float)EditorFrameView.this.getHeight()/2;

                        break;
                    case MotionEvent.ACTION_MOVE:


                        double angle_diff = Math.abs(
                                Math.atan2(event.getRawY() - scaleOrgY, event.getRawX() - scaleOrgX)
                                        - Math.atan2(scaleOrgY - centerY, scaleOrgX - centerX))*180/Math.PI;

                        double length1 = getLength(centerX, centerY, scaleOrgX, scaleOrgY);
                        double length2 = getLength(centerX, centerY, event.getRawX(), event.getRawY());

                        int size = convertDpToPixel(SELF_SIZE_IN_DP, getContext());
                        if(length2 > length1
                                && (angle_diff < 25 || Math.abs(angle_diff-180)<25)
                        ) {
                            //scale up
                            double offsetX = Math.abs(event.getRawX() - scaleOrgX);
                            double offsetY = Math.abs(event.getRawY() - scaleOrgY);
                            double offset = Math.max(offsetX, offsetY);
                            offset = Math.round(offset);
                            EditorFrameView.this.getLayoutParams().width += offset;
                            EditorFrameView.this.getLayoutParams().height += offset;
                            onScaling(true);
                        }else if(length2 < length1
                                && (angle_diff < 25 || Math.abs(angle_diff-180)<25)
                                && EditorFrameView.this.getLayoutParams().width > size/2
                                && EditorFrameView.this.getLayoutParams().height > size/2) {
                            //scale down
                            double offsetX = Math.abs(event.getRawX() - scaleOrgX);
                            double offsetY = Math.abs(event.getRawY() - scaleOrgY);
                            double offset = Math.max(offsetX, offsetY);
                            offset = Math.round(offset);
                            EditorFrameView.this.getLayoutParams().width -= offset;
                            EditorFrameView.this.getLayoutParams().height -= offset;
                            onScaling(false);
                        }

                        double angle = Math.atan2(event.getRawY() - centerY, event.getRawX() - centerX) * 180 / Math.PI;

                        setRotation((float) angle - 45);

                        onRotating();

                        scaleOrgX = event.getRawX();
                        scaleOrgY = event.getRawY();

                        postInvalidate();
                        requestLayout();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
            }
            return true;
        }
    };

    private double getLength(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow(y2-y1, 2)+Math.pow(x2-x1, 2));
    }

    public void commitChanges(boolean save) {
        if(save) {
            borderView.setVisibility(View.GONE);
            imgScale.setVisibility(View.GONE);
            imgDelete.setVisibility(View.GONE);
            imgEdit.setVisibility(View.GONE);
            imgScale.setOnTouchListener(null);
            this.setOnTouchListener(null);
            this.setOnClickListener(v -> commitChanges(false));
        } else {
            borderView.setVisibility(View.VISIBLE);
            imgScale.setVisibility(View.VISIBLE);
            imgDelete.setVisibility(View.VISIBLE);
            imgEdit.setVisibility(View.VISIBLE);
            imgScale.setOnTouchListener(touchListener);
            this.setOnTouchListener(touchListener);
            this.setOnClickListener(null);
        }
    }

    protected void onScaling(boolean scaleUp){}

    protected void onRotating(){}

    private static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int)px;
    }

    private class BorderView extends View {

        public BorderView(Context context) {
            super(context);
        }

        public BorderView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public BorderView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            LayoutParams params = (LayoutParams)this.getLayoutParams();

            Rect border = new Rect();
            border.left = (int)this.getLeft()-params.leftMargin;
            border.top = (int)this.getTop()-params.topMargin;
            border.right = (int)this.getRight()-params.rightMargin;
            border.bottom = (int)this.getBottom()-params.bottomMargin;
            Paint borderPaint = new Paint();
            borderPaint.setStrokeWidth(6);
            borderPaint.setColor(Color.WHITE);
            borderPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(border, borderPaint);

        }
    }
}
