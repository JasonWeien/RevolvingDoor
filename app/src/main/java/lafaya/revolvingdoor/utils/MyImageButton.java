package lafaya.revolvingdoor.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class MyImageButton extends ImageButton {
    private String text = null;
    private int color;

    public MyImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setText(String text){
        this.text = text;
    }

    public void setColor(int color){
        this.color = color;
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(color);
        canvas.drawText(text, 15, 20, paint);
    }

}
