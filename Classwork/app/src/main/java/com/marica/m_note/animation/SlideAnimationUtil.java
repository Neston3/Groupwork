package com.marica.m_note.animation;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.marica.m_note.R;

/**
 * Created by Neston on 15/12/2017.
 */

public class SlideAnimationUtil {
    public static void slideOutToLeft(Context context, View view) {
        runSimpleAnimation(context, view, R.anim.to_left);
    }
    public static void slideInFromRight(Context context, View view) {
        runSimpleAnimation(context, view, R.anim.from_right);
    }
    public static void slideInFromLeft(Context context, View view) {
        runSimpleAnimation(context, view, R.anim.from_left);
    }
    public static void slideOutToRight(Context context, View view) {
        runSimpleAnimation(context, view, R.anim.to_right);
    }

    private static void runSimpleAnimation(Context context, View view, int animationId) {
        view.startAnimation(AnimationUtils.loadAnimation(
                context, animationId
        ));
    }
}
