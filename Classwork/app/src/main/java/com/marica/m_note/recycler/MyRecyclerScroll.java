package com.marica.m_note.recycler;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Neston on 17/12/2017.
 */

public abstract class MyRecyclerScroll extends RecyclerView.OnScrollListener {

    private static final float MINIMUM = 25;
    int scrollDist = 0;
    boolean isVisible = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (isVisible && scrollDist > MINIMUM) {
            hide();
            scrollDist = 0;
            isVisible = false;
        }
        else if (!isVisible && scrollDist < -MINIMUM) {
            show();
            scrollDist = 0;
            isVisible = true;
        }

        if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
            scrollDist += dy;
        }

    }

    protected abstract void show();

    protected abstract void hide();
}
