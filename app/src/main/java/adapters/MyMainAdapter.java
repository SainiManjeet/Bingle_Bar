package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.activities.MainActivity;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;
import com.bingle.ameba.bingle_bar.fragments.Photos;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import pojo.RestaurantPhotos;

/**
 * Created by ameba on 13/4/18.
 */

public class MyMainAdapter extends RecyclerView.Adapter <MyMainAdapter.MyHolder> {
    private static final String TAG = "MyMainAdapter";
    Context applicationContext;

    List <RestaurantPhotos> img;

    //
    private CommonMethods commonMethods;

    public MyMainAdapter(Context applicationContext, List <RestaurantPhotos> img) {
        this.applicationContext = applicationContext;
        this.img = img;
        commonMethods = CommonMethods.getInstance();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_restuant_detail, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Log.e("MyMainAdapter", "MyMainAdapter:");

        try {

            if(img.get(position).getImageUrl()!=null){
                Glide.with(applicationContext).load(img.get(position).getImageUrl())
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imgRes);}
                        else{
                Glide.with(applicationContext).load(R.drawable.default_listing)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imgRes);
            }


            holder. imgRes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Photos photos = new Photos();
                    MainActivity.m_activity.swapContentFragment(photos, "Photos", true);
                }
            });


        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        //return (img != null) ? img.get(0).getLogoUrl() : 0;
        return img.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        ImageView imgRes;

        public MyHolder(View itemView) {
            super(itemView);
            imgRes = itemView.findViewById(R.id.img1);
        }
    }
}

