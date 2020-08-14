package edios.mlr.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import edios.mlr.R;
import edios.mlr.interfaces.SelectDeliveryPartner;
import edios.mlr.model.DeliveryPartner;

public class DeliveryPartnerRecyclerAdapter extends RecyclerView.Adapter<DeliveryPartnerRecyclerAdapter.ViewModal> {
    private Context context;
    private List<DeliveryPartner> deliveryPartners;
    private SelectDeliveryPartner sdp;

    public DeliveryPartnerRecyclerAdapter(Context context, List<DeliveryPartner> deliveryPartners, SelectDeliveryPartner sdp) {
        this.context = context;
        this.deliveryPartners = deliveryPartners;
        this.sdp = sdp;
    }

    @NonNull
    @Override
    public ViewModal onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.deliver_partner_recycler_item, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new DeliveryPartnerRecyclerAdapter.ViewModal(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewModal holder, int position) {
        final DeliveryPartner dp = deliveryPartners.get(position);
        Glide.with(context).load(dp.getUserImageUrl()).apply(new RequestOptions().placeholder(R.drawable.ic_person).error(R.drawable.ic_person).fitCenter()).into(holder.civ_pic);
        holder.tv_partnerName.setText(dp.getPartnerName());
        holder.tv_partnerStatus.setText(dp.getPartnerAvailable().equalsIgnoreCase(context.getString(R.string.yes)) ? context.getString(R.string.available) : context.getString(R.string.unavailable));
        holder.tv_partnerStatus.setTextColor(dp.getPartnerAvailable().equalsIgnoreCase(context.getString(R.string.yes))
                ? context.getResources().getColor(R.color.colorGreen) : context.getResources().getColor(R.color.colorRed));
        holder.ib_add.setVisibility(dp.getPartnerAvailable().equalsIgnoreCase(context.getString(R.string.yes)) ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return deliveryPartners != null ? deliveryPartners.size() : 0;
    }

    class ViewModal extends RecyclerView.ViewHolder {
        @BindView(R.id.civ_pic)
        CircleImageView civ_pic;
        @BindView(R.id.tv_partnerName)
        TextView tv_partnerName;
        @BindView(R.id.tv_partnerStatus)
        TextView tv_partnerStatus;
        @BindView(R.id.ib_add)
        ImageButton ib_add;
        @BindView(R.id.ib_call)
        ImageButton ib_call;


        @OnClick({R.id.ib_call, R.id.ib_add})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ib_add:
                    sdp.getSelectedPartner(deliveryPartners.get(getLayoutPosition()));
                    break;
                case R.id.ib_call:
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + deliveryPartners.get(getLayoutPosition()).getUserMobile()));
                    context.startActivity(intent);
                    break;
            }
        }

        ViewModal(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
