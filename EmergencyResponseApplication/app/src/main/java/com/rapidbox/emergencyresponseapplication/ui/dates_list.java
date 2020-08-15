package com.rapidbox.emergencyresponseapplication.ui;


import android.util.Log;
import android.widget.Toast;

/**
 * Created by qz on 6/3/2020.
 */

public class dates_list {

    dnode head = null;
    dnode tail = null;

    public boolean insert(String date) {
        dnode newnode = new dnode(date);
        if (head == null) {
            head = newnode;
            tail = newnode;
        } else {
            dnode temp=head;
            while (temp != null) {
                if (temp.date.equals(date)) {
                    return false;
                }
                temp = temp.next;
            }
            tail.next = newnode;
            tail = newnode;
        }
        return true;
    }


    public Boolean duplicate(String date) {
        dnode temp = head;

        while (temp != null) {
            if (temp.date.equals(date)) {
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    public void traverse() {
        dnode temp = head;
        while (temp != null) {
            temp = temp.next;
        }
    }

}


class dnode{

    public dnode next;
    String date;

    public dnode(String _date)
    {
        date=_date;
    }
}


