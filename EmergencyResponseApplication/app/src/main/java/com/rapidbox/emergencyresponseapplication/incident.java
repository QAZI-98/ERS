package com.rapidbox.emergencyresponseapplication;

public class incident {

    private String type;
    private String name;
    private String text;
    private  String id;
    private String email;
    private  String latitude;
    private  String longitude;
    private String imguri;
    private String date;
    private String time;
    private String subtype;


    public incident() {
    }

    public incident(String _text ,String _imguri ,String _name, String _type, String _id, String _email,String lat,String longe,String _time,String _date,String _subtype) {
        id = _id;
        text= _text;
        name = _name;
        type = _type;
        email = _email;
        imguri=_imguri;
        latitude=lat;
        longitude=longe;
        time=_time;
        date=_date;
        subtype = _subtype;
    }

    public incident(String _text,String _name, String _type,String _imguri,String lat,String longe) {
        text= _text;
        name = _name;
        type = _type;
        imguri=_imguri;
        latitude=lat;
        longitude=longe;

    }



    public String getiid() {
        return id;
    }
    public String getname() {
        return name;
    }
    public String gettype() {return type;}
    public String gettext() {
        return text;
    }
    public String getemail() {
        return email;
    }
    public String getimguri(){return imguri;}
    public String getlatitude(){return latitude;}
    public String getlongitude(){return longitude;}
    public String getSubtype(){return subtype;}


    public void setiid(String _id) {
        id=_id;
    }
    public void setname(String _name) {
        name=_name;
    }
    public void settype(String _type) { type=_type;}
    public void settext(String _type) {text=_type;}
    public void setemail(String _email) {
        email=_email;
    }
    public void setimguri(String _imguri){ imguri=_imguri;}
    public void setlatitude(String _lat){ latitude=_lat;}
    public void setlongitude(String _longe){ longitude=_longe;}
    public void setSubtype(String sub){ subtype=sub;}



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}