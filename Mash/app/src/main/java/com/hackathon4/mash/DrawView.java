package com.hackathon4.mash;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.hackathon4.mash.R;

import java.util.ArrayList;
import java.util.List;

public class DrawView extends View implements View.OnTouchListener {

    private static final String TAG = DrawView.class.getName();

    private static final float STROKE_WIDTH = 10f;
    private static final int MIN_TIME = 2;
    private static final int MAX_TIME = 8;
    private static final int STOP_DRAWING_MESSAGE = 0;

    List<Point> points = new ArrayList<Point>();
    Paint paint = new Paint();

    Point startIntersectPath;
    Point endIntersectPath;

    DrawingListener listener;

    boolean drawingEnabled = true;

    Handler handler;

    public DrawView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setColor(Color.argb(255,15,255,49));

        handler = new Handler() {
            /* (non-Javadoc)
             * @see android.os.Handler#handleMessage(android.os.Message)
             */
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == STOP_DRAWING_MESSAGE) {
                    drawingEnabled = false;
                    Log.d(TAG, "Drawing disabled");
                }
            }
        };
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

//        if (startIntersectPath != null && endIntersectPath != null) {
//            Path intersectPath = new Path();
//            intersectPath.moveTo(startIntersectPath.x, startIntersectPath.y);
//            intersectPath.lineTo(endIntersectPath.x, endIntersectPath.y);
//            canvas.drawPath(intersectPath,paint);
//        }
    }

    public boolean onTouch(View view, MotionEvent event) {
        if (drawingEnabled) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                clear();
                int drawingTime = MIN_TIME + (int) (Math.random() * (MAX_TIME - MIN_TIME));
                Log.d(TAG, "Drawing time: " + drawingTime);
                Message msg = handler.obtainMessage(STOP_DRAWING_MESSAGE);
                handler.sendMessageDelayed(msg, drawingTime * 1000);
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
                handler.removeMessages(STOP_DRAWING_MESSAGE);
            }
        }
        else {
            int intersectionsCount = computeIntersections();
            if (listener != null) {
                listener.drawingStopped(intersectionsCount);
            }
        }
        return super.onTouchEvent(event);
    }

    public void startDrawing() {
        drawingEnabled = true;
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

        int intersectionsCount = 0;
        // Iterate over all points but the first and the last
        for (int i = 1; i < points.size() - 2; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i+1);
            if (intersects(startIntersectPath,endIntersectPath,p1,p2)) {
                intersectionsCount++;
            }
        }
        // Add an intersection for the start and the end
        intersectionsCount += 2;
        Log.d(TAG, "Intersections count: " + intersectionsCount);

        return intersectionsCount;
    }

    public void setListener(DrawingListener listener) {
        this.listener = listener;
    }

    private boolean intersects(Point start1, Point end1, Point start2, Point end2) {

        // First find Ax+By=C values for the two lines
        double A1 = end1.y - start1.y;
        double B1 = start1.x - end1.x;
        double C1 = A1 * start1.x + B1 * start1.y;

        double A2 = end2.y - start2.y;
        double B2 = start2.x - end2.x;
        double C2 = A2 * start2.x + B2 * start2.y;

        double det = (A1 * B2) - (A2 * B1);

        if (det == 0) {
            // Lines are either parallel, are collinear (the exact same
            // segment), or are overlapping partially, but not fully
            // To see what the case is, check if the endpoints of one line
            // correctly satisfy the equation of the other (meaning the two
            // lines have the same y-intercept).
            // If no endpoints on 2nd line can be found on 1st, they are
            // parallel.
            // If any can be found, they are either the same segment,
            // overlapping, or two segments of the same line, separated by some
            // distance.
            // Remember that we know they share a slope, so there are no other
            // possibilities

            // Check if the segments lie on the same line
            // (No need to check both points)
            if ((A1 * start2.x) + (B1 * start2.y) == C1) {
                // They are on the same line, check if they are in the same
                // space
                // We only need to check one axis - the other will follow
                if ((Math.min(start1.x, end1.x) < start2.x)
                        && (Math.max(start1.x, end1.x) > start2.x))
                    return true;

                // One end point is ok, now check the other
                if ((Math.min(start1.x, end1.x) < end2.x)
                        && (Math.max(start1.x, end1.x) > end2.x))
                    return true;

                // They are on the same line, but there is distance between them
                return false;
            }

            // They are simply parallel
            return false;
        } else {
            // Lines DO intersect somewhere, but do the line segments intersect?
            double x = (B2 * C1 - B1 * C2) / det;
            double y = (A1 * C2 - A2 * C1) / det;

            // Make sure that the intersection is within the bounding box of
            // both segments
            if ((x > Math.min(start1.x, end1.x) && x < Math.max(start1.x,
                    end1.x))
                    && (y > Math.min(start1.y, end1.y) && y < Math.max(
                    start1.y, end1.y))) {
                // We are within the bounding box of the first line segment,
                // so now check second line segment
                if ((x > Math.min(start2.x, end2.x) && x < Math.max(start2.x,
                        end2.x))
                        && (y > Math.min(start2.y, end2.y) && y < Math.max(
                        start2.y, end2.y))) {
                    // The line segments do intersect
                    return true;
                }
            }

            // The lines do intersect, but the line segments do not
            return false;
        }
    }

    public interface DrawingListener {
        void drawingStopped(int numberOfIntersections);
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
