package com.t3h.project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.t3h.project.MainActivity;
import com.t3h.project.R;
import com.t3h.project.WebViewActivity;
import com.t3h.project.dao.AppDatabase;
import com.t3h.project.model.News;
import com.t3h.project.model.NewsAdapter;

import java.util.List;


public class FavoriteFragment extends BaseFragment<MainActivity> implements NewsAdapter.FaceItemListener, PopupMenu.OnMenuItemClickListener {
    private RecyclerView recyclerFavorite;
    private NewsAdapter adapter;
    private List<News> data;
    private int position;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_favorite;
    }

    @Override
    public String getTitle() {
        return "Favorite";
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    public void initData() {
        data = AppDatabase.getInstance(getContext()).getNewsDao().getNewsFavorite();
        if (data != null) {
            adapter.setData(data);
        }
    }

    private void initView() {
        recyclerFavorite = findViewById(R.id.recycler_favorite);
        adapter = new NewsAdapter(getContext());
        recyclerFavorite.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        long id = data.get(position).getId();
        AppDatabase.getInstance(getContext()).getNewsDao().delFavorite(id);
        data.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, data.size());
        return false;
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("url",data.get(position).getContent());
        startActivity(intent);
    }

    @Override
    public void onLongClick(int position) {
        this.position = position;
        PopupMenu popup = new PopupMenu(getContext(),recyclerFavorite.getChildAt(position) );
        popup.inflate(R.menu.menu_delete);
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }
}
