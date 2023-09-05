package com.nicare.ves.ui.customwidget.camera;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class RectOverlay extends GraphicOverlay.Graphic {

    private int RECT_COLOR = Color.RED;
    private float STROKE_WIDTH = 4.0f;
    private Paint rectPaint;


    private GraphicOverlay mGraphicOverlay;
    private Rect mRect;

    public RectOverlay(GraphicOverlay graphicOverlay, Rect rect) {

        super(graphicOverlay);

        rectPaint = new Paint();
        rectPaint.setColor(RECT_COLOR);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(STROKE_WIDTH);

        mGraphicOverlay = graphicOverlay;
        mRect = rect;

        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {

        RectF rectF = new RectF(mRect);
        rectF.right = translateX(mRect.right);
        rectF.left = translateX(mRect.left);
        rectF.top = translateY(mRect.top);
        rectF.bottom = translateY(mRect.bottom);


        canvas.drawRect(rectF, rectPaint);
    }
}
