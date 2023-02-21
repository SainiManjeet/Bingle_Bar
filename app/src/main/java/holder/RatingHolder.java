package holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;

import java.util.List;

import adapters.RatingAdapter;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class RatingHolder extends RecyclerView.ViewHolder {

    public
    @BindViews({R.id.txt_user_name, R.id.txt_time, R.id.txt_comment})
    List <TextView> lightTextViews;
    public
    @BindView(R.id.img_user)
    ImageView imgRes;

    public
    @BindView(R.id.one_txt)
    TextView btnRating;
    private CommonMethods commonMethods;

    public RatingHolder(View itemView, RatingAdapter adapter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        commonMethods = CommonMethods.getInstance();
    }

}