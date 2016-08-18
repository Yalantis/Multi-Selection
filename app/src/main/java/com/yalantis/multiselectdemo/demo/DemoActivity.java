package com.yalantis.multiselectdemo.demo;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.yalantis.multiselectdemo.R;
import com.yalantis.multiselectdemo.demo.model.Track;
import com.yalantis.multiselectdemo.demo.model.TrackList;
import com.yalantis.multiselection.lib.MultiSelectBuilder;
import com.yalantis.multiselection.lib.MultiSelect;

import java.util.List;

/**
 * Created by Artem Kholodnyi on 9/3/16.
 */
public class DemoActivity extends AppCompatActivity {

    private MultiSelect<Track> mMultiSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar((Toolbar) findViewById(R.id.toolbar));

        MultiSelectBuilder<Track> builder = new MultiSelectBuilder<>(Track.class)
                .withContext(this)
                .mountOn((ViewGroup) findViewById(R.id.mount_point))
                .withSidebarWidth(46 + 8 * 2); // ImageView width with paddings

        setUpAdapters(builder);
        mMultiSelect = builder.build();

        setUpDecoration();
    }

    private void setUpDecoration() {
        TracksItemDecorator itemDecorator = new TracksItemDecorator(
                getResources().getDimensionPixelSize(R.dimen.decoration_size));
        mMultiSelect.getRecyclerLeft().addItemDecoration(itemDecorator);
        mMultiSelect.getRecyclerRight().addItemDecoration(itemDecorator);
    }

    private void setUpAdapters(MultiSelectBuilder<Track> builder) {
        LeftAdapter leftAdapter = new LeftAdapter(position -> mMultiSelect.select(position));
        RightAdapter rightAdapter = new RightAdapter(position -> mMultiSelect.deselect(position));

        leftAdapter.addAll(TrackList.TRACKS);

        builder.withLeftAdapter(leftAdapter)
                .withRightAdapter(rightAdapter);
    }

    private void setUpToolbar(final Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.select) {
                List<Track> items = mMultiSelect.getSelectedItems();
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
