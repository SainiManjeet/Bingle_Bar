package adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.activities.MainActivity;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;
import com.bingle.ameba.bingle_bar.common_functions.RoundedCornersTransformation;
import com.bingle.ameba.bingle_bar.fragments.ResturantDetailFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import holder.OwerHolder;
import pojo.RestaurantDetailsByLatLong;


public abstract class ResturantAdapter extends RecyclerView.Adapter <OwerHolder> {
    List <RestaurantDetailsByLatLong> items;
    private Context mContext;
    private CommonMethods commonMethods;


    public ResturantAdapter(Context mContext, List <RestaurantDetailsByLatLong> items) {
        this.mContext = mContext;
        this.items = items;
        commonMethods = CommonMethods.getInstance();
    }

    @Override
    public OwerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View subscribeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_restu_list, parent, false);
        OwerHolder holderItem = new OwerHolder(subscribeView, this);
        return holderItem;
    }

    @Override
    public void onBindViewHolder(OwerHolder holder, final int position) {//Instead of current position i pass data of 0th element only that is TNDC

//holder.cardView.

        if (items.get(0).getData().get(position).getName().equalsIgnoreCase("Coming Soon")) {
            holder.openText.setTextColor(Color.parseColor("#878787"));
            holder.txtDistance.setTextColor(Color.parseColor("#878787"));
            holder.lightTextViews.get(0).setTextColor(Color.parseColor("#878787"));
        }

        holder.openText.setText(items.get(0).getData().get(position).getBarStatus());


        holder.txtDistance.setText("Distance: " + items.get(0).getData().get(position).getDistanceInKMToShow());


        holder.lightTextViews.get(0).setText(items.get(0).getData().get(position).getName());

        CommonMethods.getInstance().setFont(holder.lightTextViews.get(0));


        holder.lightTextViews.get(1).setText(items.get(0).getData().get(position).getDisplayAddress());


        CommonMethods.getInstance().setFontGray(holder.lightTextViews.get(1));

        //Set Images
        try {
            int sCorner = 20;
            int sMargin = 2;
            if (items.get(0).getData().get(position).getLogoUrl() != null) {
                Glide.with(mContext).load(items.get(0).getData().get(position).getLogoUrl()).placeholder(R.drawable.default_listing)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, sCorner, sMargin))
                        .into(holder.imgRes);
            } else {
                Glide.with(mContext).load(R.drawable.default_listing)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, sCorner, sMargin))
                        .into(holder.imgRes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Send all data to ResturantDetailFragment

        holder.linMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("hhhhh", "hhhhh" + items.get(0).getData().get(position).getIsActiveStatus());

                if (items.get(0).getData().get(position).getIsActiveStatus()) {
                    try {
                        InputMethodManager im = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

                        im.hideSoftInputFromWindow(v.getWindowToken(), 0);

                        ResturantDetailFragment resturantDetailFragment = new ResturantDetailFragment();
                        //create bundle instance
                        Bundle data = new Bundle();
                        data.putSerializable("restaurant_data", items.get(0).getData().get(position));//ServerAPIManager.getInstance().restaurantsListData.getData().get(position));
                        resturantDetailFragment.setArguments(data);

                        MainActivity.m_activity.swapContentFragment(resturantDetailFragment, "ResturantDetailFragment", true);

                    } catch (Exception e) {
                        Log.e("here===", "here=" + e.toString());
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void setItems(List <RestaurantDetailsByLatLong> items) {
        this.items = items;
        Log.e("here===", "here=" + items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (items.size() > 0) ? items.get(0).getData().size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}