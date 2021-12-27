package com.bharatapp.sgvuBus.model_class;

public class links_url {
    String id,url_title,url,image_url;
    public links_url() {

    }
    public links_url(String id, String url_title, String url,String image_url) {
        this.id = id;
        this.url_title = url_title;
        this.url = url;
        this.image_url=image_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUrl_title() {
        return url_title;
    }

    public void setUrl_title(String url_title) {
        this.url_title = url_title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
