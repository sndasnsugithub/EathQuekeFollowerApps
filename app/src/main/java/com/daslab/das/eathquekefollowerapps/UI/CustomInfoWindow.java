package com.daslab.das.eathquekefollowerapps.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.daslab.das.eathquekefollowerapps.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by User on 12/19/2017.
 */

public class CustomInfoWindow  implements GoogleMap.InfoWindowAdapter{

    private View view;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomInfoWindow(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     //  view = layoutInflater.inflate(android.support.compat.R.layout.custom_info_window,null);
        view = layoutInflater.inflate(R.layout.custom_info_window,null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        TextView tittle = view.findViewById(R.id.winTittle);
        tittle.setText(marker.getTitle());
        TextView magnitude = view.findViewById(R.id.magnitude);

        magnitude.setText(marker.getSnippet());


        return view;
    }
}
