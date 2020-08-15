package com.rapidbox.emergencyresponseapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

class node{

    public node next;
    public String lat;
    public String longe;

    node(String _lat,String _longe)
    {lat=_lat;longe=_longe;}

}

public class list {

    private node head=null;
    private node tail=null;


    void add(String lat,String longe) {
        node newnode = new node(lat,longe);
        if (head == null) {
            head = newnode;
            tail = newnode;
        } else {

            tail.next = newnode;
            tail = newnode;
        }
    }

    boolean search(String _lat,String _longe)
    {
        if (head!=null){
            node temp=head;
            while (temp!=null) {
                if (temp.lat.equals(_lat) && temp.longe.equals(_longe))
                    return true;

                temp = temp.next;

            }
            return  false;
        }
        return false;
    }

    boolean check_distance_betwen_points(double lat,double lng) {

        LatLng tocheck=new LatLng(lat,lng);
        LatLng comp;
        if (head != null) {
            node temp = head;
            while (temp != null) {


                comp=new LatLng(Double.parseDouble(temp.lat),Double.parseDouble(temp.longe));
                Double  distance = SphericalUtil.computeDistanceBetween(tocheck,comp);
                if (distance<10)
                {
                    return false;
                }
                temp = temp.next;
            }
        }
        return true;

    }


    void traverse()
    {
        if (head!=null){
            node temp=head;
            while (temp!=null)
            {
                //     Log.i("print", "location " + temp.location);
                temp=temp.next;
            }
        }

    }


}
