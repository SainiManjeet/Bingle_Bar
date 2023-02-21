package holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;

import adapters.ListResturantAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ListHolder extends RecyclerView.ViewHolder {


    public
    @BindView(R.id.txt)
    TextView txtName;

    public
    @BindView(R.id.img1)
    ImageView imgRes;

    private CommonMethods commonMethods;

    public ListHolder(View itemView, ListResturantAdapter adapter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        commonMethods = CommonMethods.getInstance();
    }

}
