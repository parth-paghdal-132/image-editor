package com.editor.image.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.editor.image.models.Text;

public class EditorTextView extends EditorFrameView {
    private AutoResizeTextView textView;

    private Text data = new Text("", Color.RED);

    private OnClick onClick;
    public EditorTextView(Context context) {
        super(context);
    }

    public EditorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditorTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View getMainView() {
        if(textView != null)
            return textView;

        textView = new AutoResizeTextView(getContext());
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(400);
        textView.setShadowLayer(4, 0, 0, Color.BLACK);
        textView.setMaxLines(1);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.CENTER;
        textView.setLayoutParams(params);

        return textView;
    }

    @Override
    protected void onEditClick() {
        onClick.onEditClick();
    }

    @Override
    protected void onSelfClick() {
        onClick.onSelfClick();
    }

    @Override
    protected void onScaling(boolean scaleUp) {
        super.onScaling(scaleUp);
    }

    public void setData(Text text) {
        data = text;
        textView.setText(text.getText());
        textView.setTextColor(text.getColor());
    }

    public Text getData() {
        return data;
    }


    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public interface OnClick {
        void onEditClick();
        void onSelfClick();
    }
}
