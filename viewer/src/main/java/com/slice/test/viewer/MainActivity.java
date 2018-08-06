package com.slice.test.viewer;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.slice.Slice;
import androidx.slice.SliceViewManager;
import androidx.slice.widget.SliceLiveData;
import androidx.slice.widget.SliceView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SliceViewManager sliceManager = SliceViewManager.getInstance(this);
        SliceView sliceView = findViewById(R.id.sliceView);

        //Uri temperatureUri = getUri(this, "temperature");
        //Uri temperatureUri = getUri(this, "header");
        Uri temperatureUri = getUri(this, "headerAction");
        //Uri temperatureUri = getUri(this, "grid");
        //Uri temperatureUri = getUri(this, "range");
        //Uri temperatureUri = getUri(this, "loading");

        Slice slice = sliceManager.bindSlice(temperatureUri);
        sliceView.setSlice(slice);

        SliceLiveData.fromUri(this, temperatureUri);
    }

    public Uri getUri(Context context, String path) {
        return new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                //.authority(context.getPackageName())
                .authority("com.example.android.slice.my")
                .appendPath(path)
                .build();
    }
}
