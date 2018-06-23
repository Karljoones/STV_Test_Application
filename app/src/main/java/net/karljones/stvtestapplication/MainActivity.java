package net.karljones.stvtestapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.karljones.stvtestapplication.templates.Article;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    List<Article> articles;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new DownloadStories().execute();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
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
            for (int i = 0 ; i < articles.size() ; i++) {
                ((TextView) findViewById(R.id.content_main_text)).append(articles.get(i).toString());
            }

            super.onPostExecute(aVoid);
        }
    }


}
