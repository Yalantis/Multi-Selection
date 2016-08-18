package com.yalantis.multiselectdemo.demo.model;

import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by Artem Kholodnyi on 9/6/16.
 */
public class Contact implements Comparable<Contact>, Serializable {

    private String name;
    @Nullable
    private Uri photoUri;
    private final int timesContacted;

    public Contact(String name, @Nullable Uri photoUri, int timesContacted) {
        this.name = name;
        this.photoUri = photoUri;
        this.timesContacted = timesContacted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(@Nullable Uri photoUri) {
        this.photoUri = photoUri;
    }

    public int getTimesContacted() {
        return timesContacted;
    }

    @Override
    public int compareTo(@NonNull Contact contact) {
        return contact.getName().compareTo(this.getName());
    }
}
