package com.bharatapp.sgvuBus.model_class;

public class notice_data {
    String nid,ntitle,nshort_des,nfull_des,img_url,date1;

    public notice_data() {

    }

    public notice_data(String nid, String ntitle, String nshort_des, String nfull_des, String img_url,String date1) {
        this.nid = nid;
        this.ntitle = ntitle;
        this.nshort_des = nshort_des;
        this.nfull_des = nfull_des;
        this.img_url=img_url;
        this.date1=date1;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getNtitle() {
        return ntitle;
    }

    public void setNtitle(String ntitle) {
        this.ntitle = ntitle;
    }

    public String getNshort_des() {
        return nshort_des;
    }

    public void setNshort_des(String nshort_des) {
        this.nshort_des = nshort_des;
    }

    public String getNfull_des() {
        return nfull_des;
    }

    public void setNfull_des(String nfull_des) {
        this.nfull_des = nfull_des;
    }
}
