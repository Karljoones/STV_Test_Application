/*
 * Copyright (c) 2018 Iarratais Development
 *
 * karl.development@gmail.com
 */

package net.karljones.stvtestapplication.templates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Article {
    @JsonProperty("title") private String title;
    @JsonProperty("publishDate") private String publishDate;
    @JsonProperty("subHeadline") private String subHeadline;
    @JsonProperty("contentHTML") private String contentHTML;
    private Images articleImages;

    public Article() {
        articleImages = new Images();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getSubHeadline() {
        return subHeadline;
    }

    public void setSubHeadline(String subHeadline) {
        this.subHeadline = subHeadline;
    }

    public String getContentHTML() {
        return contentHTML;
    }

    public void setContentHTML(String contentHTML) {
        this.contentHTML = contentHTML;
    }

    public Images getArticleImages() {
        return articleImages;
    }

    public void setArticleImages(Images articleImages) {
        this.articleImages = articleImages;
    }

    @Override
    public String toString() {
        return "Article{" + "title='" + title + '\'' + ", publishDate='" + publishDate + '\'' + ", subHeadline='" + subHeadline + '\'' + ", contentHTML='" + contentHTML + '\'' + ", articleImages=" + articleImages.getUrl() + '}';
    }
}
