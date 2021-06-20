/**
 * Copyright (c) 2012 Todoroo Inc
 * <p>
 * See the file "LICENSE" for the full license governing this code.
 */
package com.isajoh.app.utills;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;

public class CustomBorderDrawable extends ShapeDrawable {
    private final Paint fillpaint, strokepaint;
    private final float strokeWidth;

    public CustomBorderDrawable(Shape s, String fill, String stroke, int strokeWidth) {
        super(s);
        fillpaint = new Paint(this.getPaint());
        fillpaint.setColor(Color.parseColor(fill));
        strokepaint = new Paint(fillpaint);
        strokepaint.setStyle(Paint.Style.STROKE);
        strokepaint.setStrokeWidth(strokeWidth);
        strokepaint.setColor(Color.parseColor(stroke));
        this.strokeWidth = strokeWidth;
    }

    public static StateListDrawable customButton(int tl, int tr, int br, int bl, String onColor, String offColor, String borderColor, int strokeWidth) {
        Shape shape = new RoundRectShape(new float[]{tl, tl, tr, tr, br, br, bl, bl}, null, null);
        ShapeDrawable sdOn = new CustomBorderDrawable(shape, onColor, borderColor, strokeWidth);
        ShapeDrawable sdOff = new CustomBorderDrawable(shape, offColor, borderColor, strokeWidth);

        StateListDrawable stld = new StateListDrawable();
        stld.addState(new int[]{android.R.attr.state_pressed}, sdOn);
        stld.addState(new int[]{android.R.attr.state_checked}, sdOn);
        stld.addState(new int[]{android.R.attr.state_enabled}, sdOff);
        return stld;
    }

    @Override
    protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
        shape.resize(canvas.getClipBounds().right,
                canvas.getClipBounds().bottom);

        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF(0, 0, canvas.getClipBounds().right,
                        canvas.getClipBounds().bottom),
                new RectF(strokeWidth / 2, strokeWidth / 2, canvas.getClipBounds().right - strokeWidth / 2,
                        canvas.getClipBounds().bottom - strokeWidth / 2),
                Matrix.ScaleToFit.FILL);
        canvas.concat(matrix);

        shape.draw(canvas, fillpaint);
        shape.draw(canvas, strokepaint);
    }
}
