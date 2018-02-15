package com.marica.m_note.recycler;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.marica.m_note.Adapter.NotesAdapter;
import com.marica.m_note.AppManager.AppManager;
import com.marica.m_note.R;

/**
 * Created by Marica on 03/02/2018.
 */

public class RecyclerHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener listener;
    private Resources resources;
    public static final float ALPHA_FULL = 1.0f;

    public RecyclerHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((NotesAdapter.MyViewHolder) viewHolder).relativeLayoutnote;

            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((NotesAdapter.MyViewHolder) viewHolder).viewbackground;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Get RecyclerView item from the ViewHolder
            View itemView = viewHolder.itemView;

            Paint p = new Paint();
            Bitmap icon;
            if (dX > 0) {
            /* Set your color for positive displacement */

                // Draw Rect with varying right side, equal to displacement dX
                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                        (float) itemView.getBottom(), p);

            } else {
            /* Set your color for negative displacement */

                int color = AppManager.getAppContext().getResources()
                        .getColor(R.color.red_delete);

                p.setColor(color);

                // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                        (float) itemView.getRight(), (float) itemView.getBottom(), p);

                //draw an icon if the recycler is running
                if (recyclerView.getItemAnimator().isRunning()) {
                    // find first child with translationY > 0
                    // draw from it's top to translationY whatever you want

                    int top = 0;
                    int bottom = 0;

                    int childCount = recyclerView.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = recyclerView.getLayoutManager().getChildAt(i);
                        if (child.getTranslationX() == 15) {
                            top = child.getTop();
                            bottom = top + (int) child.getTranslationX();
                            Toast.makeText(AppManager.getAppContext(),"5",Toast.LENGTH_SHORT).show();
                        }
                    }

                    // draw whatever you want
                    icon = BitmapFactory.decodeResource(
                            AppManager.getAppContext().getResources(), R.drawable.ic_delete);

                    //Set the image icon for Left swipe
                    c.drawBitmap(icon,
                            (float) itemView.getRight() - convertDpToPx(16) - icon.getWidth(),
                            (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight())/2,
                            p);

                }
            }

        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    private int convertDpToPx(int dp){
        return Math.round(dp * (AppManager.getAppContext().getResources()
                .getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((NotesAdapter.MyViewHolder) viewHolder).relativeLayoutnote;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((NotesAdapter.MyViewHolder) viewHolder).relativeLayoutnote;

        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
