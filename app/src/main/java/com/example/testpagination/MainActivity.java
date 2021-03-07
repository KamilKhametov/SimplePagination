package com.example.testpagination;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.testpagination.adapter.MyAdapter;
import com.example.testpagination.api.ApiConfig;
import com.example.testpagination.api.ApiConstructor;
import com.example.testpagination.api.ApiService;
import com.example.testpagination.model.ModelProduct;
import com.example.testpagination.model.ProductResponse;
import com.example.testpagination.pagination.Pagination;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static int START_PAGE = ApiConfig.offset;
    private int TOTAL_PAGE = ApiConfig.limit;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int CURRENT_PAGE = START_PAGE;

    ProgressBar progressBar;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager = new LinearLayoutManager ( this );
    MyAdapter adapter;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        progressBar = findViewById ( R.id.progressBar );
        recyclerView = findViewById ( R.id.recyclerView );
        recyclerView.setLayoutManager ( layoutManager );

        ApiService apiService =ApiConstructor.CreateService ( ApiService.class );

        // добавление пагинации
        recyclerView.addOnScrollListener ( new Pagination (layoutManager) {
            @Override
            protected void loadMoreItem() {
                isLoading = true;
                CURRENT_PAGE += 1;

                new Handler ().postDelayed ( new Runnable () {
                    @Override
                    public void run() {
                        loadNextPage();
                        // Завершение загрузки данных, когда страница 2
                        if (CURRENT_PAGE == 1){
                            isLastPage = true;
                        }
                    }
                }, 1000 );
            }

            @Override
            protected int getTotalPages() {
                return TOTAL_PAGE;
            }

            @Override
            protected boolean isLastPage() {
                return isLastPage;
            }

            @Override
            protected boolean isLoading() {
                return isLoading;
            }
        } );

        // 1-ый запрос для загрузки данных
        Call<ProductResponse> call = apiService.getProducts ( ApiConfig.category_id, ApiConfig.sort_by, ApiConfig.sort_type, ApiConfig.limit, START_PAGE, ApiConfig.device_id, ApiConfig.isAndroid, ApiConfig.app_version, ApiConfig.location);
        call.enqueue ( new Callback<ProductResponse> () {
            @Override
            public void onResponse( Call<ProductResponse> call, Response<ProductResponse> response ) {
                List<ModelProduct> modelList = response.body ().items;

                adapter = new MyAdapter (modelList, MainActivity.this);
                recyclerView.setAdapter ( adapter );

                if (CURRENT_PAGE <= TOTAL_PAGE){
                    adapter.addBottomItem ();
                }else {
                    isLastPage = true;
                }

                progressBar.setVisibility ( View.GONE );

            }

            @Override
            public void onFailure( Call<ProductResponse> call, Throwable t ) {

            }
        } );

    }

    // загрузка оставшихся данных
    private void loadNextPage() {
        progressBar.setVisibility ( View.VISIBLE );

        ApiService apiService =ApiConstructor.CreateService ( ApiService.class );
        Call<ProductResponse> call = apiService.getProducts ( ApiConfig.category_id, ApiConfig.sort_by, ApiConfig.sort_type, ApiConfig.limit, CURRENT_PAGE, ApiConfig.device_id, ApiConfig.isAndroid, ApiConfig.app_version, ApiConfig.location);
        call.enqueue ( new Callback<ProductResponse> () {
            @Override
            public void onResponse( Call<ProductResponse> call, Response<ProductResponse> response ) {
                List<ModelProduct> modelList = response.body ().items;

                adapter.removedLastEmptyItem ();
                isLoading = false;
                adapter.addAll ( modelList );

                if (CURRENT_PAGE != TOTAL_PAGE){
                    adapter.addBottomItem ();
                }else {
                    isLastPage = true;
                }

                progressBar.setVisibility ( View.GONE );
            }

            @Override
            public void onFailure( Call<ProductResponse> call, Throwable t ) {

            }
        } );

    }
}