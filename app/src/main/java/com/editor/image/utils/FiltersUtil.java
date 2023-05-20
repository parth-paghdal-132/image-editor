package com.editor.image.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.editor.image.models.Filter;

public class FiltersUtil {

    public Bitmap applyFilter(Context context, Filter filter, Bitmap sourceBitmap) {
        Bitmap result = Bitmap.createBitmap(
                sourceBitmap.getWidth(),
                sourceBitmap.getHeight(),
                Bitmap.Config.ARGB_8888
        );
        switch (filter.getFilterName()) {

            case BLACK_AND_WHITE:
                applyBlackAndWhiteFilter(sourceBitmap, result);
                break;
            case BRIGHTNESS:
                applyBrightnessFilter(sourceBitmap, result);
                break;
            case CONTRAST:
                applyContrastFilter(sourceBitmap, result);
                break;
            case GAUSSIAN:
                applyGaussianBlurFilter(context, sourceBitmap, result);
                break;
            case GRAYSCALE:
                applyGrayscaleFilter(sourceBitmap, result);
                break;
            case HUE:
                applyHueFilter(sourceBitmap, result);
                break;
            case INVERT:
                applyInvertFilter(sourceBitmap, result);
                break;
            case SATURATION:
                applySaturationFilter(sourceBitmap, result);
                break;
            case SEPIA:
                applySepiaFilter(sourceBitmap, result);
                break;
        }
        return result;
    }

    private void applyBlackAndWhiteFilter(Bitmap sourceBitmap, Bitmap destinationBitmap) {
        Canvas canvas = new Canvas(destinationBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();

        colorMatrix.setSaturation(0);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
    }

    private void applyGrayscaleFilter(Bitmap sourceBitmap, Bitmap destinationBitmap) {
        Canvas canvas = new Canvas(destinationBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();

        colorMatrix.setSaturation(0);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);

    }

    private void applySepiaFilter(Bitmap sourceBitmap, Bitmap destinationBitmap) {
        Canvas canvas = new Canvas(destinationBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();

        colorMatrix.set(new float[]{
                0.393f, 0.769f, 0.189f, 0, 0,
                0.349f, 0.686f, 0.168f, 0, 0,
                0.272f, 0.534f, 0.131f, 0, 0,
                0, 0, 0, 1, 0
        });
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
    }

    private void applyBrightnessFilter(Bitmap sourceBitmap, Bitmap destinationBitmap) {
        Canvas canvas = new Canvas(destinationBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();

        int brightness = 100;
        colorMatrix.set(new float[]{
                1, 0, 0, 0, brightness,
                0, 1, 0, 0, brightness,
                0, 0, 1, 0, brightness,
                0, 0, 0, 1, 0
        });
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
    }

    private void applyContrastFilter(Bitmap sourceBitmap, Bitmap destinationBitmap) {
        Canvas canvas = new Canvas(destinationBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();

        float contrast = 5;
        colorMatrix.set(new float[]{
                contrast, 0, 0, 0, 0.5f * (1 - contrast),
                0, contrast, 0, 0, 0.5f * (1 - contrast),
                0, 0, contrast, 0, 0.5f * (1 - contrast),
                0, 0, 0, 1, 0
        });
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
    }

    private void applySaturationFilter(Bitmap sourceBitmap, Bitmap destinationBitmap) {
        Canvas canvas = new Canvas(destinationBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();

        float saturation = 4F;
        colorMatrix.setSaturation(saturation);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
    }

    private void applyHueFilter(Bitmap sourceBitmap, Bitmap destinationBitmap) {
        Canvas canvas = new Canvas(destinationBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();

        float hue = 180;
        colorMatrix.setRotate(0, hue);
        colorMatrix.setRotate(1, hue);
        colorMatrix.setRotate(2, hue);

        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
    }

    private void applyGaussianBlurFilter(Context context, Bitmap sourceBitmap, Bitmap destinationBitmap) {
        RenderScript renderScript = RenderScript.create(context);
        Allocation input = Allocation.createFromBitmap(renderScript, sourceBitmap);
        Allocation output = Allocation.createTyped(renderScript, input.getType());
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

        script.setInput(input);
        script.setRadius(25);
        script.forEach(output);
        output.copyTo(destinationBitmap);
    }

    private void applyInvertFilter(Bitmap sourceBitmap, Bitmap destinationBitmap) {
        Canvas canvas = new Canvas(destinationBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();

        colorMatrix.set(new float[]{
                -1, 0, 0, 0, 255,
                0, -1, 0, 0, 255,
                0, 0, -1, 0, 255,
                0, 0, 0, 1, 0
        });
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
    }

}
