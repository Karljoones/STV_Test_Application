/*
 * Copyright (c) 2018 Iarratais Development
 *
 * karl.development@gmail.com
 */

package net.karljones.stvtestapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import net.karljones.stvtestapplication.templates.Article;

public class ArticleViewer extends AppCompatActivity {
    
    public static final String EXTRA_ARTICLE = "content_article";

    private WebView Webview_content;

    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_viewer);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        Webview_content = findViewById(R.id.content_article_viewer_webview);

        // Allow the user to press "back" and set the icon of the icon.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_close_white_24px);
        }

        // Get the extras that were passed into the activity.
        if (getIntent().hasExtra(EXTRA_ARTICLE)) {
            article = (Article) getIntent().getSerializableExtra(EXTRA_ARTICLE);
        } else {
            Toast.makeText(ArticleViewer.this, getString(R.string.ArticleViewer_error_something_went_wrong), Toast.LENGTH_SHORT).show();
            finish();
        }

        // If the article has an image, load it into the HTML
        if (article.getArticleImages().hasURl()){
            loadContent(article.getContentHTML(), article.getArticleImages().getUrl());
        } else {
            loadContent(article.getContentHTML());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: onBackPressed(); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Load the HTML content into the webview. This adds the image onto the top of the HTML for viewing by the user.
     * @param contentHTML to display through the webview.
     * @param imageURL to display alongside the story.
     */
    private void loadContent(String contentHTML, String imageURL){
        // The article image is attached to the top of the HTML here.
        String htmlContent
                = "<div style=\"max-width: 100%; height:200px; text-align:center;\"> <img style=\"max-width: 100%; height:200px;\" src=\"" + imageURL + "\" alt=\"" + article.getTitle() + "\"/> </div>";
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