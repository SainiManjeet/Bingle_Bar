package holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;

import java.util.List;

import adapters.ResturantAdapter;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;


public class OwerHolder extends RecyclerView.ViewHolder {

    public
    @BindViews({R.id.txt_name, R.id.add})
    List <TextView> lightTextViews;
    public
    @BindView(R.id.listview_image)
    ImageView imgRes;
    public
    @BindView(R.id.lin)
    LinearLayout linMain;

    public
    @BindView(R.id.listview_item_title)
    TextView openText;

    public
    @BindView(R.id.txt_distance)
    TextView txtDistance;


    public
    @BindView(R.id.card_view)
    CardView cardView;
    private CommonMethods commonMethods;

    public OwerHolder(View itemView, ResturantAdapter adapter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        commonMethods = CommonMethods.getInstance();
    }

}