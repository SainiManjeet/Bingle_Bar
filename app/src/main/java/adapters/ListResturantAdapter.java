
package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;

import java.util.List;

import holder.ListHolder;
import pojo.RestuDetailBean;


public class ListResturantAdapter  extends RecyclerView.Adapter<ListHolder> {
    List<RestuDetailBean> items;
    private Context mContext;
    private CommonMethods commonMethods;


    public ListResturantAdapter(Context mContext, List<RestuDetailBean> items) {
        this.mContext = mContext;
        this.items = items;
        commonMethods = CommonMethods.getInstance();
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View subscribeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_restuant_detail, parent, false);

        ListHolder holderItem = new ListHolder(subscribeView, this);
        return holderItem;
    }

    @Override
    public void onBindViewHolder(ListHolder holder, final int position) {
       holder.txtName.setText(items.get(position).getName());

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