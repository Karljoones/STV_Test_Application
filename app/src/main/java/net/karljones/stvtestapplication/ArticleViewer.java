/*
 * Copyright (c) 2018 Iarratais Development
 *
 * karl.development@gmail.com
 */

package net.karljones.stvtestapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class ArticleViewer extends AppCompatActivity {
    
    public static final String EXTRA_CONTENT_HTML = "content_html";
    public static final String EXTRA_TITLE = "article_title";
    public static final String EXTRA_IMAGE_URL = "image_url";

    private WebView Webview_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_viewer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Webview_content = findViewById(R.id.content_article_viewer_webview);

        // Allow the user to press "back" and set the icon of the icon.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_close_white_24px);

        getContentFromIntent(getIntent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: onBackPressed();
            default: return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Get the information, such as contentHTML, title and image url from the intent that was pushed
     * to the activity. This information is then loaded into the correct views and processed
     * accordingly.
     * @param intent that was sent to the activity.
     */
    private void getContentFromIntent(Intent intent){
        // If there has been content passed into the activity, show it to the user. Otherwise, finish the activity and advise the user that something has gone wrong
        if (intent.hasExtra(EXTRA_CONTENT_HTML)) {
            loadContent(intent.getStringExtra(EXTRA_CONTENT_HTML), intent.getStringExtra(EXTRA_IMAGE_URL));
        } else {
            Toast.makeText(ArticleViewer.this, getString(R.string.ArticleViewer_error_something_went_wrong), Toast.LENGTH_SHORT).show();
            finish();
        }

        // If there has been a title passed into the viewer, then set it as the title of the activity
        if (intent.hasExtra(EXTRA_TITLE)){
            ((TextView)findViewById(R.id.content_article_viewer_title)).setText(intent.getStringExtra(EXTRA_TITLE));
        } else {
            findViewById(R.id.content_article_viewer_title).setVisibility(View.GONE);
        }
    }

    /**
     * Load the HTML content into the webview. This adds the image onto the top of the HTML for viewing by the user.
     * @param contentHTML to display through the webview.
     * @param imageURL to display alongside the story.
     */
    private void loadContent(String contentHTML, String imageURL){
        // The article image is attached to the top of the HTML here.
        String htmlContent = "<div style=\"max-width: 100%; height:200px; text-align:center;\"> <img style=\"max-width: 100%; height:200px;\" src=\"" + imageURL + "\"/> </div>";
        htmlContent = htmlContent.concat(contentHTML);

        // Load the HTML into the webview.
        Webview_content.loadData(htmlContent, "text/html", "UTF-8");
    }

    /**
     * Load the content of the HTML tag of the article. This is used when there is no image to show to the user.
     * @param contentHTML to display in the webview.
     */
    private void loadContent(String contentHTML){
        // Load the HTML into the webview.
        Webview_content.loadData(contentHTML, "text/html", "UTF-8");
    }
}
