package com.study.slice;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;


import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceProvider;
import androidx.slice.builders.GridRowBuilder;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import androidx.slice.core.SliceHints;

import static com.study.slice.MainActivity.getTemperatureString;
import static com.study.slice.MainActivity.sTemperature;
import static com.study.slice.MyBroadcastReceiver.ACTION_CHANGE_TEMP;
import static com.study.slice.MyBroadcastReceiver.EXTRA_TEMP_VALUE;

public class MySliceProvider extends SliceProvider {
    boolean isConnected;
    PendingIntent wifiTogglePendingIntent;
    Uri wifiUri;
    PendingIntent seeAllNetworksPendingIntent;
    PendingIntent takeNoteIntent;
    PendingIntent voiceNoteIntent;
    PendingIntent cameraNoteIntent;
    PendingIntent pendingIntent;
    PendingIntent openActitypendingIntent;
    PendingIntent intent1;
    PendingIntent intent2;
    PendingIntent intent3;
    PendingIntent intent4;
    IconCompat icon;
    IconCompat image1;
    IconCompat image2;
    IconCompat image3;
    IconCompat image4;
    PendingIntent volumeChangedPendingIntent;
    PendingIntent wifiSettingsPendingIntent;


    private Context context;
    private static int sReqCode = 0;

    @Override
    public boolean onCreateSliceProvider() {
        context = getContext();
        setPendingIntent();
        return true;
    }

    @Override
    public Slice onBindSlice(Uri sliceUri) {
        final String path = sliceUri.getPath();
        Log.d("test", "path : "+path);

        switch (path) {
            case "/temperature":
                return createTemperatureSlice(sliceUri);
            case "/header":
                return createSliceWithHeader(sliceUri);
            case "/headerAction":
                return createSliceWithActionInHeader(sliceUri);
            case "/grid":
                return createGridSlice(sliceUri);
            case "/range"://
                return createSliceWithRange(sliceUri);
            case "/loading":
                return createSliceShowingLoading(sliceUri);
        }
        return null;
    }

    private void setPendingIntent(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        openActitypendingIntent = PendingIntent.getActivity(getContext(), 1,
                intent, 0);
    }

    // [START create_slice_with_header]
    public Slice createSliceWithHeader(Uri sliceUri) {

        if (getContext() == null) {
            return null;
        }

        // Construct the parent.
        ListBuilder listBuilder = new ListBuilder(getContext(), sliceUri, ListBuilder.INFINITY)
                .setAccentColor(0xff0F9D58) // Specify color for tinting icons.
                .setHeader( // Create the header and add to slice.
                        new ListBuilder.HeaderBuilder()
                                .setTitle("Get a ride")
                                .setSubtitle("Ride in 4 min.")
                                .setSummary("Work in 1 hour 45 min | Home in 12 min.")
                ).addRow(new ListBuilder.RowBuilder() // Add a row.
                        .setPrimaryAction(
                                createActivityAction()) // A slice always needs a SliceAction.
                        .setTitle("Home")
                        .setSubtitle("12 miles | 12 min | $9.00")
                        .addEndItem(IconCompat.createWithResource(getContext(), R.drawable.ic_home),
                                SliceHints.ICON_IMAGE)
                ); // Add more rows if needed...
        return listBuilder.build();
    }
    // [END create_slice_with_header]

    // [START create_slice_with_action_in_header]
    public Slice createSliceWithActionInHeader(Uri sliceUri) {
        if (getContext() == null) {
            return null;
        }
        // Construct our slice actions.
        SliceAction noteAction = SliceAction.create(openActitypendingIntent,
                IconCompat.createWithResource(getContext(), R.drawable.ic_pencil),
                ListBuilder.ICON_IMAGE, "Take note");

        SliceAction voiceNoteAction = SliceAction.create(openActitypendingIntent,
                IconCompat.createWithResource(getContext(), R.drawable.ic_mic),
                ListBuilder.ICON_IMAGE,
                "Take voice note");

        SliceAction cameraNoteAction = SliceAction.create(openActitypendingIntent,
                IconCompat.createWithResource(getContext(), R.drawable.ic_camera),
                ListBuilder.ICON_IMAGE,
                "Create photo note");


        // Construct the list.
        ListBuilder listBuilder = new ListBuilder(getContext(), sliceUri, ListBuilder.INFINITY)
                .setAccentColor(0xfff4b400) // Specify color for tinting icons
                .setHeader(new ListBuilder.HeaderBuilder() // Construct the header.
                        .setTitle("Create new note")
                        .setSubtitle("Easily done with this note taking app")
                        .setSummary("summary")
                )
                .addRow(new ListBuilder.RowBuilder()
                        .setTitle("Enter app")
                        .setPrimaryAction(createActivityAction())
                )
                // Add the actions to the ListBuilder.
                .addAction(noteAction)
                .addAction(voiceNoteAction)
                .addAction(cameraNoteAction);
        return listBuilder.build();
    }
    // [END create_slice_with_action_in_header]

    // [START create_slice_with_range]
    public Slice createSliceWithRange(Uri sliceUri) {
        if (getContext() == null) {
            return null;
        }
        // Construct the parent.
        ListBuilder listBuilder = new ListBuilder(getContext(), sliceUri, ListBuilder.INFINITY)
                .addRow(new ListBuilder.RowBuilder() // Every slice needs a row.
                        .setTitle("Enter app")
                        // Every slice needs a primary action.
                        .setPrimaryAction(createActivityAction())
                )
                .addInputRange(new ListBuilder.InputRangeBuilder() // Create the input row.
                        .setTitle("Ring Volume")
                        .setInputAction(openActitypendingIntent)
                        .setMax(100)
                        .setValue(30)
                );
        return listBuilder.build();
    }
    // [END create_slice_with_range]

    // [START create_slice_showing_loading]
    public Slice createSliceShowingLoading(Uri sliceUri) {
        if (getContext() == null) {
            return null;
        }
        // Construct the parent.
        ListBuilder listBuilder = new ListBuilder(getContext(), sliceUri, ListBuilder.INFINITY)
                // Construct the row.
                .addRow(new ListBuilder.RowBuilder()
                        .setPrimaryAction(createActivityAction())
                        .setTitle("Ride to work")
                        // We’re waiting to load the time to work so indicate that on the slice by
                        // setting the subtitle with the overloaded method and indicate true.
                        .setSubtitle(null, true)
                        .addEndItem(IconCompat.createWithResource(getContext(), R.drawable.ic_work),
                                ListBuilder.ICON_IMAGE)
                );
        return listBuilder.build();
    }

    private SliceAction createActivityAction() {
        return SliceAction.create(
                PendingIntent.getActivity(
                        getContext(),
                        0,
                        new Intent(getContext(), MainActivity.class),
                        0
                ),
                IconCompat.createWithResource(getContext(), R.drawable.ic_work),
                ListBuilder.ICON_IMAGE,
                "Enter app"
        );
    }


    private Slice createTemperatureSlice(Uri sliceUri) {
        // Define the actions used in this slice
        SliceAction tempUp = new SliceAction(getChangeTempIntent(sTemperature + 1),
                IconCompat.createWithResource(context, R.drawable.ic_temp_up).toIcon(),
                "Increase temperature");
        SliceAction tempDown = new SliceAction(getChangeTempIntent(sTemperature - 1),
                IconCompat.createWithResource(context, R.drawable.ic_temp_down).toIcon(),
                "Decrease temperature");

        // Construct our parent builder
        ListBuilder listBuilder = new ListBuilder(context, sliceUri);

        // Construct the builder for the row
        ListBuilder.RowBuilder temperatureRow = new ListBuilder.RowBuilder(listBuilder);

        // Set title
        temperatureRow.setTitle(getTemperatureString(context));

        // Add the actions to appear at the end of the row
        temperatureRow.addEndItem(tempDown);
        temperatureRow.addEndItem(tempUp);

        // Set the primary action; this will activate when the row is tapped

        SliceAction openTempActivity = new SliceAction(openActitypendingIntent,
                IconCompat.createWithResource(context, R.drawable.ic_home).toIcon(),
                "Temperature controls");
        temperatureRow.setPrimaryAction(openTempActivity);

        // Add the row to the parent builder
        listBuilder.addRow(temperatureRow);

        // Build the slice
        return listBuilder.build();
    }

    private Slice createGridSlice(Uri sliceUri) {
        // Set the primary action; this will activate when the row is tapped
        Intent intent = new Intent(getContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), sliceUri.hashCode(),
                intent, 0);
        SliceAction openTempActivity = new SliceAction(pendingIntent,
                IconCompat.createWithResource(context, R.drawable.ic_home).toIcon(),
                "Temperature controls");
        // Create the parent builder.
        ListBuilder listBuilder = new ListBuilder(getContext(), sliceUri, ListBuilder.INFINITY)
                .setHeader(
                        // Create the header.
                        new ListBuilder.HeaderBuilder()
                                .setTitle("Famous restaurants")
                                .setPrimaryAction(SliceAction
                                        .create(pendingIntent, icon, ListBuilder.ICON_IMAGE,
                                                "Famous restaurants"))
                )
                // Add a grid row to the list.
                .addGridRow(new GridRowBuilder()
                        // Add cells to the grid row.
                        .addCell(new GridRowBuilder.CellBuilder()
                                        .addImage(IconCompat.createWithResource(getContext(), R.drawable.ic_android), ListBuilder.ICON_IMAGE)
                                        .addTitleText("Image Icon")
                                        .addText("Icon")
                                //.setContentIntent(intent1)
                        ).addCell(new GridRowBuilder.CellBuilder()
                                        .addImage(IconCompat.createWithResource(getContext(), R.drawable.dianella), ListBuilder.SMALL_IMAGE)
                                        .addTitleText("Image Small")
                                        .addText("Small")
                                //.setContentIntent(intent2)
                        )
                        .addCell(new GridRowBuilder.CellBuilder()
                                        .addImage(IconCompat.createWithResource(getContext(), R.drawable.yucca), ListBuilder.LARGE_IMAGE)
                                        .addTitleText("Image Large")
                                        .addText("Large")
                                //.setContentIntent(intent3)
                        )
                        // Every slice needs a primary action.
                        .setPrimaryAction(openTempActivity)
                );


        // Build the slice
        return listBuilder.build();
    }

    private PendingIntent getChangeTempIntent(int value) {
        Intent intent = new Intent(ACTION_CHANGE_TEMP);
        intent.setClass(context, MyBroadcastReceiver.class);
        intent.putExtra(EXTRA_TEMP_VALUE, value);
        return PendingIntent.getBroadcast(getContext(), sReqCode++, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public static Uri getUri(Context context, String path) {
        Log.d("Test","getUri packageName : "+context.getPackageName());
        return new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                //.authority(context.getPackageName())
                .authority("com.example.android.slice.my")
                .appendPath(path)
                .build();
    }
}
