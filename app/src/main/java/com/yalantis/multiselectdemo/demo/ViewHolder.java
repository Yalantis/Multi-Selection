package com.yalantis.multiselectdemo.demo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yalantis.multiselectdemo.R;
import com.yalantis.multiselectdemo.demo.model.Contact;

/**
 * Created by Artem Kholodnyi on 9/6/16.
 */
class ViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    ImageView avatar;

    public ViewHolder(View view) {
        super(view);
        name = (TextView) view.findViewById(R.id.name);
        avatar = (ImageView) view.findViewById(R.id.yal_ms_avatar);
    }

    public static void bind(ViewHolder viewHolder, Contact contact) {
        viewHolder.name.setText(contact.getName());
        viewHolder.avatar.setImageURI(contact.getPhotoUri());
    }
}
