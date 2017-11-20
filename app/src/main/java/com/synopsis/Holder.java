package com.synopsis;

import android.graphics.Bitmap;

/**
 * Created by ADMIN on 6/29/2016.
 */
public class Holder {
    String Catid,Name,Count,Image;
    Bitmap catbitmap,newsbitmap;
    String NewsId,NewsTitle,NewsSummary,NewsVideo,NewsImage,NewsDate,News_source,News_like,news_url,share_url,published_by,cattype;
Boolean Like_status;
    byte[] imagebitmap;

    public byte[] getImagebitmap() {
        return imagebitmap;
    }

    public void setImagebitmap(byte[] imagebitmap) {
        this.imagebitmap = imagebitmap;
    }

    public Bitmap getNewsbitmap() {
        return newsbitmap;
    }

    public void setNewsbitmap(Bitmap newsbitmap) {
        this.newsbitmap = newsbitmap;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getCattype() {
        return cattype;
    }

    public void setCattype(String cattype) {
        this.cattype = cattype;
    }

    public String getPublished_by() {
        return published_by;
    }

    public void setPublished_by(String published_by) {
        this.published_by = published_by;
    }

    public String getNews_url() {
        return news_url;
    }

    public void setNews_url(String news_url) {
        this.news_url = news_url;
    }

    public String getNews_like() {
        return News_like;
    }

    public void setNews_like(String news_like) {
        News_like = news_like;
    }

    public Boolean getLike_status() {
        return Like_status;
    }

    public void setLike_status(Boolean like_status) {
        Like_status = like_status;
    }

    public String getNews_source() {
        return News_source;
    }

    public void setNews_source(String news_source) {
        News_source = news_source;
    }

    public Bitmap getCatbitmap() {
        return catbitmap;
    }

    public void setCatbitmap(Bitmap catbitmap) {
        this.catbitmap = catbitmap;
    }





    public String getNewsId() {
        return NewsId;
    }

    public void setNewsId(String newsId) {
        NewsId = newsId;
    }

    public String getNewsTitle() {
        return NewsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        NewsTitle = newsTitle;
    }

    public String getNewsSummary() {
        return NewsSummary;
    }

    public void setNewsSummary(String newsSummary) {
        NewsSummary = newsSummary;
    }
    public String getNewsVideo() {
        return NewsVideo;
    }

    public void setNewsVideo(String newsVideo) {
        NewsVideo = newsVideo;
    }

    public String getNewsImage() {
        return NewsImage;
    }

    public void setNewsImage(String newsImage) {
        NewsImage = newsImage;
    }

    public String getNewsDate() {
        return NewsDate;
    }

    public void setNewsDate(String newsDate) {
        NewsDate = newsDate;
    }



    public String getCatid() {
        return Catid;
    }

    public void setCatid(String catid) {
        Catid = catid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
