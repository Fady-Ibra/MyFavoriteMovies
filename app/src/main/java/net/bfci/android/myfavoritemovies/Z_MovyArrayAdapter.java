package net.bfci.android.myfavoritemovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by fady on 5/28/16.
 */

public class Z_MovyArrayAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<String> imageUrls;
    private LayoutInflater inflater;

    public Z_MovyArrayAdapter(Context context, ArrayList<String> imageUrls) {
        super(context, R.layout.z_grid_item, imageUrls);
        this.context = context;
        this.imageUrls = imageUrls;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.z_grid_item, parent, false);
        }
        Picasso.with(context)
                .load(imageUrls.get(position).toString())
                .fit()
                .into((ImageView) convertView);
        return convertView;
    }

}