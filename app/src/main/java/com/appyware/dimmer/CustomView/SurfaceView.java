package com.appyware.dimmer.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.ViewGroup;

/**
 * Created by
 * --Vatsal Bajpai on
 * --18/09/16 at
 * --8:00 AM
 */

// custom view to draw overlay service layer
class SurfaceView extends ViewGroup {

    public SurfaceView(Context context) {
        super(context);
        setBackgroundColor(Color.parseColor("#10000000"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
    }
}
