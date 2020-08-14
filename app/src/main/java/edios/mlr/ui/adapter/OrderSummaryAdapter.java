package edios.mlr.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edios.mlr.R;
import edios.mlr.model.OrderSummaryItem;
import edios.mlr.utils.AppConstants;
import edios.mlr.utils.Utils;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.SummaryViewHolder> {
    private Context context;
    private List<OrderSummaryItem> summaryItemList;

    public OrderSummaryAdapter(Context context, List<OrderSummaryItem> summaryItemList) {
        this.context = context;
        this.summaryItemList = summaryItemList;
    }

    @NonNull
    @Override
    public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_summary_recycler_item, null, false);
        return new SummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder holder, int position) {
        OrderSummaryItem item = summaryItemList.get(position);
        holder.summaryCount.setText(String.valueOf(item.getTotalOrders()));
        holder.summaryDate.setText(item.getOrderDate());
        holder.summaryAmount.setText(AppConstants.CURRENCY+ Utils.decimalFormat.format(item.getTotalAmount()));


    }

    @Override
    public int getItemCount() {
        return summaryItemList.size();
    }

    class SummaryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_summary_count)
        TextView summaryCount;
        @BindView(R.id.tv_summary_amount)
        TextView summaryAmount;
        @BindView(R.id.tv_summary_date)
        TextView summaryDate;

        public SummaryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
