package net.karljones.stvtestapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.karljones.stvtestapplication.adapter.ClickListener;
import net.karljones.stvtestapplication.adapter.MainListAdapter;
import net.karljones.stvtestapplication.templates.Article;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ArrayList<Article> articles;

    RecyclerView RecyclerView_list;
    MainListAdapter mainListAdapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        RecyclerView_list = findViewById(R.id.content_main_list);

        new DownloadStories().execute();
    }

    private void insertInformationToList(){
        mainListAdapter.setClickListener(new ClickListener() {
            @Override
            public void ItemClicked(View v, int position) {
                Intent viewer = new Intent(MainActivity.this, ArticleViewer.class);
                viewer.putExtra(ArticleViewer.EXTRA_CONTENT_HTML, articles.get(position).getContentHTML());
                viewer.putExtra(ArticleViewer.EXTRA_TITLE, articles.get(position).getTitle());
                viewer.putExtra(ArticleViewer.EXTRA_IMAGE_URL, articles.get(position).getArticleImages().getUrl());
                startActivity(viewer);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        RecyclerView_list.setLayoutManager(layoutManager);
        RecyclerView_list.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(8), true));
        RecyclerView_list.setItemAnimator(new DefaultItemAnimator());
        RecyclerView_list.setAdapter(mainListAdapter);
    }

    private class DownloadStories extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground (Void... voids) {
            try {
                articles = new ArrayList<>();

                OkHttpClient client = new OkHttpClient();

                final ObjectMapper mapper = new ObjectMapper();

                // Step 1: Download the JSON file that contains the information for the articles
                Request request = new Request.Builder()
                        .url("http://api.stv.tv/articles/?count=50&navigationLevelId=1218&orderBy=ranking+DESC%2C+createdAt+DESC&full=1&count=20")
                        .get()
                        .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()){
                    String jsonData = response.body().string();

                    try {
                        articles = mapper.reader().forType(new TypeReference<List<Article>>(){}).readValue(jsonData);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }

                // Step 2: Download the images for the articles
                for (int i = 0 ; i < articles.size() ; i++){

                    // This is the code for the image requests for the images
//                    Request imageRequest = new Request.Builder()
//                            .url(getString(R.string.Link_image_download, articles.get(i).getArticleImages().getId()))
//                            .get()
//                            .build();

                    Request imageRequest = new Request.Builder()
                            .url("https://karljoones.github.io/tests/stv/experiment.json")
                            .get()
                            .build();

                    Response imageResponse = client.newCall(imageRequest).execute();

                    if (response.isSuccessful()){
                        String jsonResponse = imageResponse.body().string();

                        JsonNode root = mapper.readTree(jsonResponse);
                        JsonNode urlNode = root.path("url");
                        articles.get(i).getArticleImages().setUrl(urlNode.asText());
                    }
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mainListAdapter = new MainListAdapter(articles, MainActivity.this);

            insertInformationToList();

            // Hide the loading progress bar.
            if (articles.size() != 0){
                findViewById(R.id.content_main_loading).setVisibility(View.GONE);
            }

            Log.i(getClass().getSimpleName(), "Information retrieved");

            super.onPostExecute(aVoid);
        }
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        private GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
