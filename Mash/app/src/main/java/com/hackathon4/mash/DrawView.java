package com.hackathon4.mash;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.hackathon4.mash.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class DrawView extends View implements View.OnTouchListener {

    private static final float STROKE_WIDTH = 10f;

    List<Point> points = new ArrayList<Point>();
    Paint paint = new Paint();

    Point startIntersectPath;
    Point endIntersectPath;

    public DrawView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setColor(Color.argb(255,15,255,49));
    }

    @Override
    public void onDraw(Canvas canvas) {
        Path path = new Path();
        boolean first = true;
        for (Point point : points) {
            if (first) {
                first = false;
                path.moveTo(point.x, point.y);
            } else {
                path.lineTo(point.x, point.y);
            }
        }

        canvas.drawPath(path, paint);

        if (startIntersectPath != null && endIntersectPath != null) {
            Path intersectPath = new Path();
            intersectPath.moveTo(startIntersectPath.x, startIntersectPath.y);
            intersectPath.lineTo(endIntersectPath.x, endIntersectPath.y);
            canvas.drawPath(intersectPath,paint);
        }
    }

    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            clear();
        }
        if (event.getAction() != MotionEvent.ACTION_UP) {
            for (int i = 0; i < event.getHistorySize(); i++) {
                Point point = new Point();
                point.x = event.getHistoricalX(i);
                point.y = event.getHistoricalY(i);
                points.add(point);
            }
            invalidate();
            return true;
        }
        else {
            computeIntersections();
        }
        return super.onTouchEvent(event);
    }

    public void clear() {
        points.clear();
        startIntersectPath = null;
        endIntersectPath = null;
    }

    public int computeIntersections() {
        startIntersectPath = new Point();
        endIntersectPath = new Point();

        // Construct a path that includes the start and end points
        Point start = points.get(0);
        Point end = points.get(points.size() - 1);
        // Get slope
        float slope = (end.y - start.y)/(end.x - start.x);
        // Get y intercept
        float yInt = start.y - (slope * start.x);
        // get x intercept
        float xInt = -yInt/slope;
        // Get xmax and ymax
        int xMax = getWidth();
        int yMax = getHeight();
        // get xmax intercept
        float xMaxInt = slope*xMax + yInt;
        // get yMax intercept
        float yMaxInt = (yMax - yInt)/slope;

        // Get the two coordinates that are within the screen bounds

        // Check if the line intersects the top of the screen
        if (yInt >= 0 && yInt < yMax) {
            // Intersects left
            startIntersectPath.x = 0;
            startIntersectPath.y = yInt;
            if (xInt >= 0 && xInt < xMax) {
                // Intersects top
                endIntersectPath.x = xInt;
                endIntersectPath.y = 0;
            }
            else {
                if (xMaxInt >= 0 && xMaxInt < yMax) {
                    // Intersects right
                    endIntersectPath.x = xMax;
                    endIntersectPath.y = xMaxInt;
                }
                else {
                    // Intersects bottom
                    endIntersectPath.x = yMaxInt;
                    endIntersectPath.y = yMax;
                }
            }
        }
        else if (xInt >= 0  && xInt < xMax) {
            // Intersects top
            startIntersectPath.x = xInt;
            startIntersectPath.y = 0;
            if (xMaxInt >= 0 && xMaxInt < yMax) {
                // Intersects right
                endIntersectPath.x = xMax;
                endIntersectPath.y = xMaxInt;
            } else {
                // Intersects bottom
                endIntersectPath.x = yMaxInt;
                endIntersectPath.y = yMax;
            }
        }
        else {
            // Intersects right and bottom
            startIntersectPath.x = yMaxInt;
            startIntersectPath.y = yMax;
            endIntersectPath.x = xMax;
            endIntersectPath.y = xMaxInt;
        }

        return 0;

    }

    class Point {
        float x, y;
        float dx, dy;

        @Override
        public String toString() {
            return x + ", " + y;
        }
    }
}
