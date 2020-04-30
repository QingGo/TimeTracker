package com.goqing.timetracker;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

public class MyUtility{

    // 生成圆角按钮
    static public Drawable getRoundRect(int background_color_button) {
        RoundRectShape rectShape = new RoundRectShape(new float[]{
                30, 30, 30, 30,
                30, 30, 30, 30
        }, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(rectShape);
        shapeDrawable.getPaint().setColor(background_color_button);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.getPaint().setAntiAlias(true);
        shapeDrawable.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        return shapeDrawable;
    }
}
