package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import pojo.RestaurantPhotos;

/**
 * Created by vineet on 27/4/18.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.MyHolder>{
    private static final String TAG ="MyMainAdapter" ;
    Context applicationContext;
    List<RestaurantPhotos> img;
    private CommonMethods commonMethods;


    public PhotosAdapter(Context applicationContext, List<RestaurantPhotos> img) {
        this.applicationContext=applicationContext;
        this.img=img;
        commonMethods = CommonMethods.getInstance();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_gallery,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        try {

            if(img.get(position).getImageUrl()!=null)

               Glide.with(applicationContext).load(img.get(position).getImageUrl())
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imgRes);


        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {

        return  img.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView imgRes;
        public MyHolder(View itemView) {
            super(itemView);
            imgRes=itemView.findViewById(R.id.img1);
        }
    }
}
