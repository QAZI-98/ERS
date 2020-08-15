package com.rapidbox.emergencyresponseapplication;


import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;

import java.io.Console;
import java.util.ArrayList;

/**
 * Created by qz on 6/4/2020.
 */

class rnode {

    rnode next;
    int dates;
    int incidents_occured;
    public int xy;
    public int square_x;
    public int square_y;

    public rnode(int _dates,int _incidents_occured,int _xy,int x2,int y2)
    {
        dates=_dates;
        incidents_occured=_incidents_occured;
        xy = _xy;
        square_x = x2;
        square_y = y2;

    }
}


public class regression_list {

    private rnode head=null;
    private rnode tail=null;
    private int total=0;
    private int sumx ;
    private int sumy ;
    private int sumxy ;
    private int sumxsq ;
    private String tag="regress";


    void insert(int date,int count) {
        rnode newnode = new rnode(date,count,date*count,date*date,count*count);
        if (head == null) {
            head = newnode;
            tail = newnode;
        } else {

            tail.next = newnode;
            tail = newnode;
        }
    }



    public ArrayList traverse()
    {
        ArrayList scatterEntries=new ArrayList<>();
        rnode temp=head;

        Log.e(tag,"\tDay#inc#   xy  x^2   y^2");
        while (temp!=null)
        {
            scatterEntries.add(new BarEntry(temp.dates, temp.incidents_occured));
            temp=temp.next;
        }
        return  scatterEntries;

    }

    public void count()
    {
        rnode nono = head;
        while (nono != null)
        {
            total++;
            nono = nono.next;
        }

    }


    public int get_mean(int _x)
    {
        Log.i("xtag",Integer.toString(_x));
        rnode nono = head;
        double mean_x=0;
        double mean_y=0;

        while (nono != null)
        {

            sumx += nono.dates;
            sumy += nono.incidents_occured;
            sumxy += nono.xy;
            sumxsq += nono.square_x;
            nono = nono.next;
        }


        mean_x = sumx;
        mean_y = sumy;
        Log.i("c#","\ntotal x "+sumx+ " total y " +sumy+" sum xy "+sumxy+" sumxsq "+sumxsq);
        mean_x /= total;
        mean_y /= total;
        Log.i("c#","mean "+mean_x+" "+mean_y);


        Double b = sumxy - (total * mean_x * mean_y);
        double temp =mean_x*mean_x;
        temp *= total;
        temp = sumxsq - temp;
        b /= temp;
        //a=ybar -b(xbar)
        double a = mean_y - (b * mean_x);
        //y=ax+b

        double _y = a + (b*_x);
        int retur=(int) Math.round(_y);
        retur=(retur==0) ? 0:retur;
        return retur;
    }





}






