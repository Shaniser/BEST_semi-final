package com.godelsoft.bestsemi_final;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class StopableRecycleView extends RecyclerView {
    private boolean scrollEnable = true;
    public StopableRecycleView(@NonNull Context context) {
        super(context);
    }

    public StopableRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StopableRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return scrollEnable && super.canScrollVertically(direction);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return scrollEnable && super.canScrollHorizontally(direction);
    }

    public void setScrollEnable(boolean scrollEnable) {
        this.scrollEnable = scrollEnable;
    }
}
