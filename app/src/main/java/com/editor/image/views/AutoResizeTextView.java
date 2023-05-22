package com.editor.image.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.widget.TextView;

public class AutoResizeTextView extends TextView {

    private interface SizeTester {
        public int onTestSize(int suggestedSize, RectF availableSpace);
    }

    private RectF textRect = new RectF();

    private RectF availableSpaceRect;

    private SparseIntArray textCachedSizes;

    private TextPaint paint;

    private float maxTextSize;

    private float spacingMultiply = 1.0f;

    private float spacingAdd = 0.0f;

    private float minTextSize = 20;

    private int widthLimit;

    private static final int NO_LINE_LIMIT = -1;
    private int maxLines;

    private boolean enableSizeCache = true;
    private boolean isInitiallized;

    public AutoResizeTextView(Context context) {
        super(context);
        initialize();
    }

    public AutoResizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public AutoResizeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        paint = new TextPaint(getPaint());
        maxTextSize = getTextSize();
        availableSpaceRect = new RectF();
        textCachedSizes = new SparseIntArray();
        if (maxLines == 0) {
            maxLines = NO_LINE_LIMIT;
        }
        isInitiallized = true;
    }

    @Override
    public void setText(final CharSequence text, BufferType type) {
        super.setText(text, type);
        adjustTextSize(text.toString());
    }

    @Override
    public void setTextSize(float size) {
        maxTextSize = size;
        textCachedSizes.clear();
        adjustTextSize(getText().toString());
    }

    @Override
    public void setMaxLines(int maxlines) {
        super.setMaxLines(maxlines);
        maxLines = maxlines;
        reAdjust();
    }

    public int getMaxLines() {
        return maxLines;
    }

    @Override
    public void setSingleLine() {
        super.setSingleLine();
        maxLines = 1;
        reAdjust();
    }

    @Override
    public void setSingleLine(boolean singleLine) {
        super.setSingleLine(singleLine);
        if (singleLine) {
            maxLines = 1;
        } else {
            maxLines = NO_LINE_LIMIT;
        }
        reAdjust();
    }

    @Override
    public void setLines(int lines) {
        super.setLines(lines);
        maxLines = lines;
        reAdjust();
    }

    @Override
    public void setTextSize(int unit, float size) {
        Context c = getContext();
        Resources r;

        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();
        maxTextSize = TypedValue.applyDimension(unit, size,
                r.getDisplayMetrics());
        textCachedSizes.clear();
        adjustTextSize(getText().toString());
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        super.setLineSpacing(add, mult);
        spacingMultiply = mult;
        spacingAdd = add;
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start,
                                 final int before, final int after) {
        super.onTextChanged(text, start, before, after);
        reAdjust();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldwidth,
                                 int oldheight) {
        textCachedSizes.clear();
        super.onSizeChanged(width, height, oldwidth, oldheight);
        if (width != oldwidth || height != oldheight) {
            reAdjust();
        }
    }

    private void reAdjust() {
        adjustTextSize(getText().toString());
    }

    private void adjustTextSize(String string) {
        if (!isInitiallized) {
            return;
        }
        int startSize = (int) minTextSize;
        int heightLimit = getMeasuredHeight() - getCompoundPaddingBottom()
                - getCompoundPaddingTop();
        widthLimit = getMeasuredWidth() - getCompoundPaddingLeft()
                - getCompoundPaddingRight();
        availableSpaceRect.right = widthLimit;
        availableSpaceRect.bottom = heightLimit;
        super.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                efficientTextSizeSearch(startSize, (int) maxTextSize,
                        mSizeTester, availableSpaceRect));
    }

    private int efficientTextSizeSearch(int start, int end,
                                        SizeTester sizeTester, RectF availableSpace) {
        if (!enableSizeCache) {
            return binarySearch(start, end, sizeTester, availableSpace);
        }
        String text = getText().toString();
        int key = text == null ? 0 : text.length();
        int size = textCachedSizes.get(key);
        if (size != 0) {
            return size;
        }
        size = binarySearch(start, end, sizeTester, availableSpace);
        textCachedSizes.put(key, size);
        return size;
    }

    private static int binarySearch(int start, int end, SizeTester sizeTester,
                                    RectF availableSpace) {
        int lastBest = start;
        int lo = start;
        int hi = end - 1;
        int mid = 0;
        while (lo <= hi) {
            mid = (lo + hi) >>> 1;
            int midValCmp = sizeTester.onTestSize(mid, availableSpace);
            if (midValCmp < 0) {
                lastBest = lo;
                lo = mid + 1;
            } else if (midValCmp > 0) {
                hi = mid - 1;
                lastBest = hi;
            } else {
                return mid;
            }
        }
        return lastBest;

    }

    private final SizeTester mSizeTester = new SizeTester() {
        @Override
        public int onTestSize(int suggestedSize, RectF availableSPace) {
            paint.setTextSize(suggestedSize);
            String text = getText().toString();
            boolean singleline = getMaxLines() == 1;
            if (singleline) {
                textRect.bottom = paint.getFontSpacing();
                textRect.right = paint.measureText(text);
            } else {
                StaticLayout layout = new StaticLayout(text, paint,
                        widthLimit, Layout.Alignment.ALIGN_NORMAL, spacingMultiply,
                        spacingAdd, true);
                if (getMaxLines() != NO_LINE_LIMIT
                        && layout.getLineCount() > getMaxLines()) {
                    return 1;
                }
                textRect.bottom = layout.getHeight();
                int maxWidth = -1;
                for (int i = 0; i < layout.getLineCount(); i++) {
                    if (maxWidth < layout.getLineWidth(i)) {
                        maxWidth = (int) layout.getLineWidth(i);
                    }
                }
                textRect.right = maxWidth;
            }

            textRect.offsetTo(0, 0);
            if (availableSPace.contains(textRect)) {
                return -1;
            } else {
                return 1;
            }
        }
    };
}
