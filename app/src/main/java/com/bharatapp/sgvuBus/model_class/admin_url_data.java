package com.bharatapp.sgvuBus.model_class;

public class admin_url_data {
    String id,title,aclass,image_url;

    public admin_url_data() {

    }

    public admin_url_data(String id, String title, String aclass, String image_url) {
        this.id = id;
        this.title = title;
        this.aclass = aclass;
        this.image_url = image_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAclass() {
        return aclass;
    }

    public void setAclass(String aclass) {
        this.aclass = aclass;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
