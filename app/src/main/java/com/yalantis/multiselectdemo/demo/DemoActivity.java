package com.yalantis.multiselectdemo.demo;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.yalantis.multiselectdemo.R;
import com.yalantis.multiselectdemo.demo.model.Contact;
import com.yalantis.multiselection.lib.MultiSelectBuilder;
import com.yalantis.multiselection.lib.MultiSelect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artem Kholodnyi on 9/3/16.
 */
public class DemoActivity extends AppCompatActivity {

    private MultiSelect<Contact> mMultiSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar((Toolbar) findViewById(R.id.toolbar));

        askPermissions();
    }

    private void askPermissions() {
        RxPermissions.getInstance(this)
                .request(Manifest.permission.READ_CONTACTS)
                .subscribe(granted -> {
                    if (granted) {
                        loadContacts();
                    } else {
                        View content = findViewById(android.R.id.content);
                        Snackbar.make(content, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.allow, v -> askPermissions())
                                .show();
                    }
                });
    }

    private void loadContacts() {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] {
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.PHOTO_URI,
                        ContactsContract.Contacts.TIMES_CONTACTED
                },
                null,
                null,
                null);

        List<Contact> contacts = null;
        if (cursor != null && cursor.moveToFirst()) {
            contacts = new ArrayList<>(cursor.getCount());
            do {
                String name = cursor.getString(0);
                String photoUrl = cursor.getString(1);
                int timesContatcted = cursor.getInt(2);
                contacts.add(new Contact(name,
                        photoUrl == null ? null : Uri.parse(photoUrl),
                        timesContatcted));
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }


        MultiSelectBuilder<Contact> builder = new MultiSelectBuilder<>(Contact.class)
                .withContext(this)
                .mountOn((ViewGroup) findViewById(R.id.mount_point))
                .withSidebarWidth(46 + 8 * 2); // ImageView width with paddings

        setUpAdapters(builder, contacts);
        mMultiSelect = builder.build();

        setUpDecoration();
    }

    private void setUpDecoration() {
        TracksItemDecorator itemDecorator = new TracksItemDecorator(
                getResources().getDimensionPixelSize(R.dimen.decoration_size));
        mMultiSelect.getRecyclerLeft().addItemDecoration(itemDecorator);
        mMultiSelect.getRecyclerRight().addItemDecoration(itemDecorator);
    }

    private void setUpAdapters(MultiSelectBuilder<Contact> builder, List<Contact> contacts) {
        LeftAdapter leftAdapter = new LeftAdapter(position -> mMultiSelect.select(position));
        RightAdapter rightAdapter = new RightAdapter(position -> mMultiSelect.deselect(position));

        leftAdapter.addAll(contacts);

        builder.withLeftAdapter(leftAdapter)
                .withRightAdapter(rightAdapter);
    }

    private void setUpToolbar(final Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.select) {
                List<Contact> items = mMultiSelect.getSelectedItems();
                final int selectedCount = items.size();
                final String msg;
                if (selectedCount == 0) {
                    msg = getString(R.string.nothing_selected);
                    mMultiSelect.showNotSelectedPage();
                } else {
                    msg = getResources().getQuantityString(R.plurals.you_selected_x_songs,
                            selectedCount, selectedCount);
                    mMultiSelect.showSelectedPage();
                }
                Snackbar.make(toolbar, msg, Snackbar.LENGTH_LONG).show();
                return true;
            } else {
                return false;
            }
        });
    }

}
