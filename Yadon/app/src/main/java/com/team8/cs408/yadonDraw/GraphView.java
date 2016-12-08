package com.team8.cs408.yadonDraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


public class GraphView extends View {
    private int totalMember;
    private int repaidMember;
    private boolean isPieGraph = false;     // if false, the graph is for the bar graph in groupInfo activity.

    public GraphView(Context context) {
        super(context);
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setArgs(int totalMember, int repaidMember) {
        this.totalMember = totalMember;
        this.repaidMember = repaidMember;
    }

    public void setPieGraph() {
        this.isPieGraph = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        if (isPieGraph) {
            paint.setTextSize(50);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            //canvas.drawText(Math.round((float)repaidMember / totalMember * 100f) +"%", 110, 185, paint);
            canvas.drawText(repaidMember + " / " + totalMember, 100, 185, paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(10);

            RectF rectF = new RectF(50, 70, 250, 270);
            if (totalMember > 0) {
                canvas.drawArc(rectF, -90f, 360f * repaidMember / totalMember, false, paint);
            }
        } else {           //bar graph
            paint.setColor(Color.rgb(0, 0, 0));
            paint.setStrokeWidth(10);

            //canvas.drawRect(150, 120, 1250, 250, paint);
            if (totalMember > 0) {
                int subBarInterval = 20;
                int subBarLength = (1130 - subBarInterval * (totalMember - 1)) / totalMember;
                paint.setStyle(Paint.Style.STROKE);
                for (int i = 0; i < totalMember; i++) {
                    canvas.drawRect(150 + i * (subBarLength + subBarInterval), 120,
                            150 + i * (subBarLength + subBarInterval) + subBarLength, 250, paint);
                }
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.rgb(50, 50, 255));
                for (int i = totalMember-repaidMember; i < totalMember; i++) {
                    canvas.drawRect(150 + i * (subBarLength + subBarInterval), 120,
                            150 + i * (subBarLength + subBarInterval) + subBarLength, 250, paint);
                }
            }

        }
    }
}
