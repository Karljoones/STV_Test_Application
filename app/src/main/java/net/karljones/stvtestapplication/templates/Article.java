/*
 * Copyright (c) 2018 Iarratais Development
 *
 * karl.development@gmail.com
 */

package net.karljones.stvtestapplication.templates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Article implements Serializable{
    @JsonProperty("title") private String title;
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

    public String getSubHeadline() {
        return subHeadline;
    }

    public String getContentHTML() {
        return contentHTML;
    }

    public Images getArticleImages() {
        return articleImages;
    }

    @Override
    public String toString() {
        return "Article{" + "title='" + title + '\'' + ", subHeadline='" + subHeadline + '\'' + ", contentHTML='" + contentHTML + '\'' + ", articleImages=" + articleImages + '}';
    }
}
