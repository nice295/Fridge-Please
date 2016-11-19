/*
 * Copyright (C) The Android Open Source Project
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

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.TextBlock;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nice295.fridgeplease.camera.GraphicOverlay;
import com.nice295.fridgeplease.model.MyItem;

import java.util.HashMap;
import java.util.List;

/**
 * A very simple Processor which receives detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */
public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {

    private static final String TAG = "OcrDetectorProcessor";
    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    private HashMap<String, String> mItemList; //khlee
    OcrCaptureActivity mOcrCaptureActivity;


    OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay) {
        mGraphicOverlay = ocrGraphicOverlay;

    }

    //khlee
    OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay, HashMap<String, String> itemList, OcrCaptureActivity ocrCaptureActivity) {
        mGraphicOverlay = ocrGraphicOverlay;
        mItemList = itemList;
        mOcrCaptureActivity = ocrCaptureActivity;
        Log.d(TAG, "All items" + mItemList.toString());


    }

    /**
     * Called by the detector to deliver detection results.
     * If your application called for it, this could be a place to check for
     * equivalent detections by tracking TextBlocks that are similar in location and content from
     * previous frames, or reduce noise by eliminating TextBlocks that have not persisted through
     * multiple detections.
     */
    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        mGraphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);

            Log.d("Coming TextBlock", item.getValue());

            if (mItemList.containsKey( item.getValue().toLowerCase()) ) {
                Log.d("Matched TextBlock", item.getValue());
                OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);
                mGraphicOverlay.add(graphic);
            }

            List<Line> lines = (List<Line>) item.getComponents();
            for(Line elements : lines){
                Log.i("current lines ", ": " + elements.getValue());

                if (mItemList.containsKey( elements.getValue().toLowerCase()) ) {
                    Log.d("TextBlock", elements.getValue());
                    OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, elements);
                    mGraphicOverlay.add(graphic);

                    mOcrCaptureActivity.addItem(elements.getValue());

                    //Log.d(TAG, "Add temp item: " + elements.getValue() +", " + mItemList.get( elements.getValue().toLowerCase()));
                    //addTempNewItem(elements.getValue(), 0, mItemList.get( elements.getValue().toLowerCase()));
                }
            }

            /*
            //khlee
            for(Word word : mWordList){
                if(word.getWord().contains(item.getValue().toLowerCase())){
                    Log.d("TextBlock", item.getValue());
                    OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);
                    mGraphicOverlay.add(graphic);
                }
            }
            */

            /*
            if (mWordList.contains(item.getValue().toLowerCase())) {
                Log.d("TextBlock", item.getValue());
                OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);
                mGraphicOverlay.add(graphic);
            }
            */
        }
    }

    /**
     * Frees the resources associated with this detection processor.
     */
    @Override
    public void release() {
        mGraphicOverlay.clear();
    }

}
