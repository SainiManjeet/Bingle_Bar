package com.bingle.ameba.bingle_bar.fragments;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;

/**
 * Created by Master Gaurav Singla on 12/4/18.
 */

class MyListItemArtist extends BaseAdapter {
    CommonMethods commonMethods=new CommonMethods();
    private Activity nav_fragment;
    String [] maintitle;
    Integer[] imgid;
    private static LayoutInflater inflater=null;
    public MyListItemArtist(Activity nav_fragment, String[] maintitle, Integer[] imgid) {
        this.nav_fragment=nav_fragment;
        this.maintitle=maintitle;
        this.imgid=imgid;
        inflater = (LayoutInflater)nav_fragment.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return maintitle.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi=viewGroup;

        vi = inflater.inflate(R.layout.list_items_artist, null);

        TextView title1 = (TextView)vi.findViewById(R.id.list_item_artist_name); // title

        commonMethods = CommonMethods.getInstance();
        commonMethods.setFont(title1);

        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_item_img); // thumb image

        title1.setText(maintitle[i]);
        thumb_image.setImageResource(imgid[i]);

        return vi;
    }

}
