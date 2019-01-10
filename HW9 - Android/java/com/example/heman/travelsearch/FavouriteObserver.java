package com.example.heman.travelsearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class FavouriteObserver extends RecyclerView.AdapterDataObserver {

    private View emptyView;
    private RecyclerView recyclerView;

    public FavouriteObserver(RecyclerView rv, View ev) {
        this.recyclerView = rv;
        this.emptyView    = ev;
        checkIfEmpty();
    }
    private void checkIfEmpty() {
        if (emptyView != null && recyclerView.getAdapter() != null) {
            boolean emptyViewVisible = recyclerView.getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
        }
    }

    public void onChanged() { checkIfEmpty(); }
    public void onItemRangeInserted(int positionStart, int itemCount) { checkIfEmpty(); }
    public void onItemRangeRemoved(int positionStart, int itemCount) { checkIfEmpty(); }
}
