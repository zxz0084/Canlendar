package com.swufestu.canlendar.Text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BorderText extends androidx.appcompat.widget.AppCompatTextView {

    public BorderText(@NonNull Context context) {
        super(context);
    }

    public BorderText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BorderText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawLine(0,this.getHeight()-1,this.getWidth()-1,this.getHeight()-1,paint);
    }
}
