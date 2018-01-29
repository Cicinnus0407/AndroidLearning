package com.cicinnus.androidlearning.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;

/**
 * @author cicinnus
 *         2018/1/23.
 */

public class ElasticityView extends View{

    private final Scroller scroller;

    public ElasticityView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        scroller = new Scroller(context);
    }

    private void smoothScroll(int destX,int destY){
        int scrollX = getScrollX();
        int deltaX = destX-scrollX;
        scroller.startScroll(scrollX,0,deltaX,0,1000);
    }



    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            postInvalidate();
        }
    }
}
