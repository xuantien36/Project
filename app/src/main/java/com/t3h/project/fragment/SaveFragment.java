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


public class SaveFragment extends BaseFragment<MainActivity> implements NewsAdapter.FaceItemListener, PopupMenu.OnMenuItemClickListener {
    private RecyclerView recyclerSave;
    private NewsAdapter adapter;
    private int position;
    private List<News> arr;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_saved;
    }

    @Override
    public String getTitle() {
        return "Saved";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();

    }

    public void initData() {
        arr = AppDatabase.getInstance(getContext()).getNewsDao().getAll();
        adapter.setData(arr);

    }

    private void initView() {
        recyclerSave = findViewById(R.id.recycler_saved);
        adapter = new NewsAdapter(getContext());
        recyclerSave.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.it_like:
                long id = arr.get(position).getId();
                AppDatabase.getInstance(getContext()).getNewsDao().setFavorite(id);
                getParentActivity().getFmFavorite().initData();
                break;
            case R.id.it_delete:
                arr.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, arr.size());

                News[] news = new News[arr.size()];
                arr.toArray(news);
                AppDatabase.getInstance(getContext()).getNewsDao().deleteAll();
                AppDatabase.getInstance(getContext()).getNewsDao().insertAll(news);
                break;
        }
        return false;
    }

    @Override
    public void onClick(int position) {
       String path =  arr.get(position).getContent();
       Intent intent = new Intent(getContext(), WebViewActivity.class);
       intent.putExtra("url",path);
       startActivity(intent);
    }

    @Override
    public void onLongClick(int position) {
        this.position = position;
        PopupMenu popup = new PopupMenu(getContext(), recyclerSave.getChildAt(position));
        popup.inflate(R.menu.menu);
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }
}
