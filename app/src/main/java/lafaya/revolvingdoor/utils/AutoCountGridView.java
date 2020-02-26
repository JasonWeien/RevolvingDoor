package lafaya.revolvingdoor.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

public class AutoCountGridView extends GridView {

    public AutoCountGridView(Context context) {
        super(context);
    }

    public AutoCountGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoCountGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }

    @Override
    public int getNumColumns() {//可以不复写，但必须在xml中声明android:numColumns="3"
        return 3;
    }
}
