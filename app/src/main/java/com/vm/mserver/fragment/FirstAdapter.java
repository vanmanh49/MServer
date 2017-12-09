package com.vm.mserver.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vm.mserver.R;
import com.vm.mserver.model.Contact;

import java.util.List;

/**
 * Created by VanManh on 09-Dec-17.
 */

public class FirstAdapter extends BaseAdapter {
    private List<Contact> contacts;

    public FirstAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contacts.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent,false);
        TextView tvName = v.findViewById(R.id.tv_c_name);
        TextView tvPhoneNum = v.findViewById(R.id.tv_c_phone_num);
        tvName.setText(contacts.get(position).getName());
        tvPhoneNum.setText(contacts.get(position).getPhoneNum());
        return v;
    }
}
