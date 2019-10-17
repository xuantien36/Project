package com.t3h.project.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.t3h.project.MainActivity;
import com.t3h.project.R;
import com.t3h.project.WebViewActivity;
import com.t3h.project.api.ApiBuilder;
import com.t3h.project.api.DialogUtils;
import com.t3h.project.dao.AppDatabase;
import com.t3h.project.download.DownloadAsync;
import com.t3h.project.model.News;
import com.t3h.project.model.NewsAdapter;
import com.t3h.project.model.NewsResponsive;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.content.ContextCompat.checkSelfPermission;


public class NewsFragment extends BaseFragment<MainActivity> implements Callback<NewsResponsive>, SearchView.OnQueryTextListener, NewsAdapter.FaceItemListener, View.OnClickListener, DownloadAsync.DownloadCallback {
    private RecyclerView recyclerNews;
    private NewsAdapter adapter;
    private SearchView searchView;
    private List<News> news;
    private Dialog dialog;
    private ProgressDialog progressDialog;
    private String language = "vi";
    private MenuItem item1;
    private int po;
    private Button btnVN, btnUS, btnUR, btnHoly, btnVin,
            btnVene, btnVirg, btnKorea, btnKoso, btnVanua,
            btnUzu, btnMyo, btnJama, btnKaz, btnDomi;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    public String getTitle() {
        return "News";
    }

    private final String[] PERMISSION = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String p : PERMISSION) {
                int check = checkSelfPermission(getContext(), p);
                if (check != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        recyclerNews = findViewById(R.id.recycler_news);
        adapter = new NewsAdapter(getContext());
        recyclerNews.setAdapter(adapter);
        setHasOptionsMenu(true);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_searchview, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        item1 = menu.findItem(R.id.language);
        searchView = (SearchView) item.getActionView();
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public void onResponse(Call<NewsResponsive> call, Response<NewsResponsive> response) {
        List<News> news = response.body().getNews();
        if (!news.isEmpty()) {
            News[] arr = new News[news.size()];
            news.toArray(arr);
            progressDialog.dismiss();
            adapter.setData(Arrays.asList(arr));
            this.news = news;
        } else {
            DialogUtils.cancelDialog();
            Toast.makeText(getActivity(), "Nothing to show!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onFailure(Call<NewsResponsive> call, Throwable t) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        String apiKey = "df459e641ab049f7a109d69ebbd3e584"; progressDialog =new ProgressDialog(getContext());
        progressDialog.setMessage("Loading........");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String keySearch = searchView.getQuery().toString();
        ApiBuilder.getInstance().getNews(keySearch, apiKey, language).enqueue(this);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.language) {
            showAddDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("url", news.get(position).getUrl());
        startActivity(intent);
    }


    @Override
    public void onLongClick(final int position) {

        if (checkPermission()) {
            po = position;
            progressDialog =new ProgressDialog(getContext());
            progressDialog.setMessage("Loading........");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            DownloadAsync async = new DownloadAsync(this);
            News item = adapter.getData().get(position);
            async.execute(item.getUrl());

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMISSION, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermission()) {
            Toast.makeText(getActivity(), "Đồng ý", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Từ chối", Toast.LENGTH_SHORT).show();


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showAddDialog() {
        dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.setContentView(R.layout.dialog_language);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        btnVN = dialog.findViewById(R.id.btn_vn);
        btnUS = dialog.findViewById(R.id.btn_us);
        btnUR = dialog.findViewById(R.id.btn_uru);
        btnHoly = dialog.findViewById(R.id.btn_holy);
        btnVin = dialog.findViewById(R.id.btn_vincent);
        btnVene = dialog.findViewById(R.id.btn_vene);
        btnVirg = dialog.findViewById(R.id.btn_virgin);
        btnKorea = dialog.findViewById(R.id.btn_korea);
        btnKoso = dialog.findViewById(R.id.kosovo);
        btnVanua = dialog.findViewById(R.id.btn_vanatu);
        btnUzu = dialog.findViewById(R.id.btn_uzube);
        btnMyo = dialog.findViewById(R.id.btn_mayote);
        btnJama = dialog.findViewById(R.id.btn_jamai);
        btnKaz = dialog.findViewById(R.id.btn_kazak);
        btnDomi = dialog.findViewById(R.id.btn_domi);

        btnVN.setOnClickListener(this);
        btnUS.setOnClickListener(this);
        btnUR.setOnClickListener(this);
        btnHoly.setOnClickListener(this);
        btnVin.setOnClickListener(this);
        btnVene.setOnClickListener(this);
        btnVirg.setOnClickListener(this);
        btnKorea.setOnClickListener(this);
        btnKoso.setOnClickListener(this);
        btnVanua.setOnClickListener(this);
        btnUzu.setOnClickListener(this);
        btnMyo.setOnClickListener(this);
        btnJama.setOnClickListener(this);
        btnKaz.setOnClickListener(this);
        btnDomi.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_vn:
                language = "vi";
                item1.setIcon(R.drawable.vietnam);
                dialog.dismiss();
                break;
            case R.id.btn_us:
                language = "en";
                item1.setIcon(R.drawable.us);
                dialog.dismiss();
                break;
            case R.id.btn_uru:
                language = "ur";
                item1.setIcon(R.drawable.uru);
                dialog.dismiss();
                break;
            case R.id.btn_holy:
                language = "en";
                item1.setIcon(R.drawable.holysee);
                dialog.dismiss();
                break;
            case R.id.btn_vincent:
                language = "en";
                item1.setIcon(R.drawable.vinsen);
                dialog.dismiss();
                break;
            case R.id.btn_vene:
                language = "es";
                item1.setIcon(R.drawable.vene);
                dialog.dismiss();
                break;
            case R.id.btn_virgin:
                language = "en";
                item1.setIcon(R.drawable.virgin);
                dialog.dismiss();
                break;
            case R.id.btn_korea:
                language = "ko";
                item1.setIcon(R.drawable.korea);
                dialog.dismiss();
                break;
            case R.id.kosovo:
                language = "sr";
                item1.setIcon(R.drawable.kosovo);
                dialog.dismiss();
                break;
            case R.id.btn_vanatu:
                language = "bi";
                item1.setIcon(R.drawable.vanuatu);
                dialog.dismiss();
                break;
            case R.id.btn_uzube:
                language = "uz";
                item1.setIcon(R.drawable.uzube);
                dialog.dismiss();
                break;
            case R.id.btn_mayote:
                language = "fr";
                item1.setIcon(R.drawable.mayotee);
                dialog.dismiss();
                break;
            case R.id.btn_jamai:
                language = "sr";
                item1.setIcon(R.drawable.jamaica);
                dialog.dismiss();
                break;
            case R.id.btn_kazak:
                language = "kk";
                item1.setIcon(R.drawable.kz);
                dialog.dismiss();
                break;
            case R.id.btn_domi:
                language = "sr";
                item1.setIcon(R.drawable.doan);
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void onDownloadUpdate(int percent) {

    }
    @Override
    public void onDownloadSuccess(String path) {
        News item = adapter.getData().get(po);
        item.setContent(path);
        AppDatabase.getInstance(getContext()).getNewsDao().insert(item);
        getParentActivity().getFmSaved().initData();
        progressDialog.dismiss();
        Toast.makeText(getContext(), "DownLoad Success", Toast.LENGTH_SHORT).show();
    }

}
