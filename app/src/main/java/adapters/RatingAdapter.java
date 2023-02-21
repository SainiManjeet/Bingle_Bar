package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import holder.RatingHolder;
import pojo.OwerBean;

/**
 * Created by Er.Manjeet Kaur Saini on 1/5/18.
 */

public class RatingAdapter extends RecyclerView.Adapter <RatingHolder> {
    List <OwerBean> items;
    private Context mContext;
    private CommonMethods commonMethods;

    public RatingAdapter(Context mContext, List <OwerBean> items) {
        this.mContext = mContext;
        this.items = items;
        commonMethods = CommonMethods.getInstance();
    }

    @Override
    public RatingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View subscribeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_custom, parent, false);
        RatingHolder holderItem = new RatingHolder(subscribeView, this);
        return holderItem;
    }

    @Override
    public void onBindViewHolder(RatingHolder holder, final int position) {


        holder.btnRating.setText(items.get(position).getRatingRestu());
        holder.lightTextViews.get(0).setText(items.get(position).getNameRestu());
        holder.lightTextViews.get(1).setText(items.get(position).getTime());

        if (items.get(position).getComment().equalsIgnoreCase("NULL")) {
            holder.lightTextViews.get(2).setText("");
        } else {
            holder.lightTextViews.get(2).setText(items.get(position).getComment());
        }
        //Set User Image
        if (items.get(position).getProfilePic() != null && !items.get(position).getProfilePic().equalsIgnoreCase("null")) {
            Glide.with(mContext).load(items.get(position).getProfilePic())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgRes);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
