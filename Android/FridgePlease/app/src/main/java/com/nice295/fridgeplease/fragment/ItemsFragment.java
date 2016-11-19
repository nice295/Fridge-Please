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

package com.nice295.fridgeplease.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nice295.fridgeplease.R;
import com.nice295.fridgeplease.model.Item;
import com.nice295.fridgeplease.model.MyItem;

import java.util.ArrayList;
import java.util.HashMap;

import io.paperdb.Paper;

public class ItemsFragment extends Fragment {
    private static final String TAG = "ItemsFragment";

    private ListView mLvMyItems;
    private ListViewAdapter mAdapter = null;
    private ArrayList<MyItem> mMyItemArray;

    private HashMap<String, String> mItems;

    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference mDatabase;

    private String mServerItems[][] = {
            {"Bacon", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fbacon.png?alt=media&token=47a19799-7cc8-489c-b4b6-ba9dfc0ae545"},
            {"Apple", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fapple.png?alt=media&token=72b7ae17-c5d8-4a4e-8207-5c309cb4e7fd"},
            {"Potato", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fpotato.png?alt=media&token=34b8286d-278c-462b-bfbc-8ca07a8a0555"},
            {"Paprika", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fpaprika.png?alt=media&token=d23c6e10-e361-4945-a18a-18ef7de0c8a3"},
            {"Broccoli", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fbroccoli.png?alt=media&token=7bf52b79-8557-401a-babc-2294ceecc8db"},
            {"Mushroom", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fmushroom.png?alt=media&token=ed9e5b81-7f33-416c-870a-21b7b6db1cd8"},
            {"Fish", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Ffish.png?alt=media&token=73400e69-8b76-4034-99b0-e80bab42173b"},
            {"Bread", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fbread.png?alt=media&token=aa254ce9-629a-4606-b53f-8a8f44139022"},

            {"Milk", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fmilk.png?alt=media&token=71baf6dc-2979-4af7-9f3c-c95d590bf590"},

            {"Meat", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fmeat.png?alt=media&token=31ea467b-3680-4961-a42b-eed182fbc604"},
            {"Cheese", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fcheese.png?alt=media&token=8de657de-941c-4a6a-bd9b-60ac424088ef"},
            {"Chicken", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fchicken.png?alt=media&token=56d43408-95fd-4c62-9794-3045fc202f0a"},
            {"Egg", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fegg.png?alt=media&token=b9e12746-cfe2-45aa-8c2c-731295552e97"},
            {"Carrot", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fcarrot.png?alt=media&token=6105531f-6a6b-4115-8d62-f0cc3354921e"}
            
            /*
            {"Pork", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fmilk.png?alt=media&token=41089b2e-9ffe-4d6d-8dd0-9ff836eb6436"},
            {"Lemon", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fmilk.png?alt=media&token=41089b2e-9ffe-4d6d-8dd0-9ff836eb6436"},
            {"Onion", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fmilk.png?alt=media&token=41089b2e-9ffe-4d6d-8dd0-9ff836eb6436"},

            "Shrimp", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fmilk.png?alt=media&token=41089b2e-9ffe-4d6d-8dd0-9ff836eb6436"
            "Steak", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fmilk.png?alt=media&token=41089b2e-9ffe-4d6d-8dd0-9ff836eb6436"
            "Tomato", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fmilk.png?alt=media&token=41089b2e-9ffe-4d6d-8dd0-9ff836eb6436"
            "Wine", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fmilk.png?alt=media&token=41089b2e-9ffe-4d6d-8dd0-9ff836eb6436"
            "Juice", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fmilk.png?alt=media&token=41089b2e-9ffe-4d6d-8dd0-9ff836eb6436"
            "Cucumber", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fmilk.png?alt=media&token=41089b2e-9ffe-4d6d-8dd0-9ff836eb6436"
            "Banana", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fmilk.png?alt=media&token=41089b2e-9ffe-4d6d-8dd0-9ff836eb6436"
            "Grape", "https://firebasestorage.googleapis.com/v0/b/fridgeplease.appspot.com/o/items%2Fmilk.png?alt=media&token=41089b2e-9ffe-4d6d-8dd0-9ff836eb6436"
            */
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout ll = (RelativeLayout) inflater.inflate(
                R.layout.fragment_items, container, false);

        mMyItemArray = new ArrayList<MyItem>();
        mLvMyItems = (ListView) ll.findViewById(R.id.lvMembers);
        mAdapter = new ListViewAdapter(getActivity(), R.layout.layout_item_list_item, mMyItemArray);
        mLvMyItems.setAdapter(mAdapter);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Paper.init(getActivity());

        mItems = Paper.book().read("items", new HashMap<String, String>());


        mDatabase.child("items").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "Total items count: " + dataSnapshot.getChildrenCount());

                        if (dataSnapshot.getChildrenCount() == 0) {
                            initItems();
                        } else {
                            mItems.clear();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Item item = postSnapshot.getValue(Item.class);
                                Log.d(TAG, "Name: " + item.getName());
                                Log.d(TAG, "URL: " + item.getImageUrl());
                                mItems.put(item.getName().toLowerCase(), item.getImageUrl());
                            }

                            Paper.book().write("items", mItems);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "items:onCancelled", databaseError.toException());
                    }
                }

        );

        Query myTopPostsQuery = mDatabase.child("my-items").orderByChild("time");
        myTopPostsQuery.addValueEventListener(
                //mDatabase.child("my-items").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mMyItemArray.clear();

                        if (dataSnapshot.getChildrenCount() == 0) {
                            //initMyItems();
                        } else {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                MyItem item = postSnapshot.getValue(MyItem.class);
                                Log.d(TAG, "Name: " + item.getName());
                                Log.d(TAG, "Time: " + item.getTime());
                                Log.d(TAG, "URL: " + item.getImageUrl());

                                mMyItemArray.add(item);
                            }


                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "my-items:onCancelled", databaseError.toException());
                    }
                }
        );


        return ll;
    }

    private void initItems() {
        for (int idx = 0; idx < mServerItems.length; idx++) {
            addNewItem(mServerItems[idx][0], mServerItems[idx][1]);
        }
    }

    private void addNewItem(String name, String url) {
        String key = mDatabase.child("items").push().getKey();
        Item item = new Item(name, url);
        mDatabase.child("items").child(key).setValue(item);
    }

    private void addNewMyItem(String name, int time, String url) {
        String key = mDatabase.child("my-items").push().getKey();
        MyItem item = new MyItem(name, time, url);
        mDatabase.child("my-items").child(key).setValue(item);
    }

    private class ListViewAdapter extends ArrayAdapter<MyItem> {
        private ArrayList<MyItem> items;

        public ListViewAdapter(Context context, int textViewResourceId, ArrayList<MyItem> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        public class ViewHolder {
            public ImageView ivPic;
            public TextView tvName;
            public TextView tvTime;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListViewAdapter.ViewHolder viewHolder;

            Log.d(TAG, "position: " + position);

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.layout_item_list_item, parent, false);

                viewHolder = new ListViewAdapter.ViewHolder();
                viewHolder.ivPic = (ImageView) convertView.findViewById(R.id.ivPic);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
                viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ListViewAdapter.ViewHolder) convertView.getTag();
            }

            MyItem item = items.get(position);
            Log.d(TAG, "Name: " + item.getName());
            Log.d(TAG, "Time: " + item.getTime());
            Log.d(TAG, "URL: " + item.getImageUrl());
            if (item != null) {
                Glide.with(getActivity())
                        .load(item.getImageUrl())
                        .into(viewHolder.ivPic);

                if (item.getTime() == 0) {
                    viewHolder.tvName.setTextColor(Color.parseColor("#FFA000"));
                    viewHolder.tvName.setText(item.getName());
                } else {
                    viewHolder.tvName.setTextColor(Color.parseColor("#000000"));
                    viewHolder.tvName.setText(item.getName());
                }
                viewHolder.tvName.setText(item.getName());
                viewHolder.tvTime.setText(String.valueOf(item.getTime()) + " " + getString(R.string.days));
            }
            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
