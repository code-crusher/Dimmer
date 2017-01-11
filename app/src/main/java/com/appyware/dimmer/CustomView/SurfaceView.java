package com.appyware.dimmer.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.view.ViewGroup;

import com.appyware.dimmer.R;

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
        setBackgroundColor(context.getResources().getColor(R.color.overlay_color));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
    }
}
