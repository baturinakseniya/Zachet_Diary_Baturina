package com.example.diary;

import android.view.View;

public interface RecycleItemOnClickListener
{
    /**
     * Листенер для каждого view, соответствующего позиции
     * @param v
     * @param position
     */
    public void onItemClick(View v, int position);
}
