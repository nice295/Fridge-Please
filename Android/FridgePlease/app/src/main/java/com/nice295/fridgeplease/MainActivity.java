/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nice295.fridgeplease;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nice295.fridgeplease.fragment.FoodsFragment;
import com.nice295.fridgeplease.fragment.ItemsFragment;
import com.nice295.fridgeplease.model.MyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    private FloatingActionsMenu mFloatingActionsMenu;

    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference mDatabase;

    private static final int RC_OCR_CAPTURE = 9003;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Change font for tab titles
        /*
        Typeface mTypeface = Typeface.createFromAsset(getAssets(), "BMDOHYEON_ttf.ttf");
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(mTypeface, Typeface.NORMAL);
                }
            }
        }
        */

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Start the    background Firebase activity
        //startService(new Intent(this, NotificationService.class));

        mFloatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_camera);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch Ocr capture activity.
                Intent intent = new Intent(getApplicationContext(), OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                intent.putExtra(OcrCaptureActivity.UseFlash, false);

                startActivityForResult(intent, RC_OCR_CAPTURE);
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */
            }
        });

        FloatingActionButton fabGallery = (FloatingActionButton) findViewById(R.id.fab_gallery);
        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), R.string.preparing, Toast.LENGTH_SHORT).show();
                mFloatingActionsMenu.collapse();
            }
        });

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
                mFloatingActionsMenu.collapse();
            }
        });
    }

    private void showAddDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add, null);
        dialogBuilder.setView(dialogView);

        final EditText etNewString = (EditText) dialogView.findViewById(R.id.edit);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        dialogBuilder.setTitle(getString(R.string.add_new_string));
        //dialogBuilder.setMessage("");
        dialogBuilder.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (etNewString.getText() != null) {
                    addNewMyItem(etNewString.getText().toString(), 0, "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fgeneral.png?alt=media&token=b47ab1e6-1d1f-4c9d-b8f2-d2ac9e3f8374");
                    etNewString.clearFocus();
                    etNewString.setText("");

                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                } else {
                    Log.e(TAG, "Input Text!");
                }
            }
        });

        dialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void addNewMyItem(String name, int time, String url) {
        String key = mDatabase.child("my-items").push().getKey();
        MyItem item = new MyItem(name, time, url);
        mDatabase.child("my-items").child(key).setValue(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mFloatingActionsMenu.collapseImmediately();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {
            mDatabase.child("my-items").removeValue();
            initMyItems();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ItemsFragment(), getString(R.string.items));
        adapter.addFragment(new FoodsFragment(), getString(R.string.foods));
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {

                ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
                viewPager.setCurrentItem(0);

                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    String url = data.getStringExtra(OcrCaptureActivity.Url);
                    Log.d(TAG, "Text read: " + text);
                    //mMyWords.add(new Word(text, url));
                    //Paper.book().write("myWords", mMyWords);
                } else {
                    //statusMessage.setText(R.string.ocr_failure);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                //statusMessage.setText(String.format(getString(R.string.ocr_error),
                //        CommonStatusCodes.getStatusCodeString(resultCode)));
                Log.d(TAG, "Text read: " + String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initMyItems() {
        addNewMyItem("Chicken", 1, "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fchicken.png?alt=media&token=56d43408-95fd-4c62-9794-3045fc202f0a");
        addNewMyItem("Egg", 3, "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fegg.png?alt=media&token=b9e12746-cfe2-45aa-8c2c-731295552e97");
        addNewMyItem("Cheese", 5, "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fcheese.png?alt=media&token=8de657de-941c-4a6a-bd9b-60ac424088ef");
        addNewMyItem("Meat", 7, "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fmeat.png?alt=media&token=31ea467b-3680-4961-a42b-eed182fbc604");
        addNewMyItem("Carrot", 11, "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fcarrot.png?alt=media&token=6105531f-6a6b-4115-8d62-f0cc3354921e");
    }
}
