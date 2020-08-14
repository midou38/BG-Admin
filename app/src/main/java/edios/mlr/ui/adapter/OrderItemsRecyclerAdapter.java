package edios.mlr.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edios.mlr.R;
import edios.mlr.model.CustomerOrderItems;
import edios.mlr.utils.Utils;


public class OrderItemsRecyclerAdapter extends RecyclerView.Adapter<OrderItemsRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<CustomerOrderItems> orderItems;
    private boolean showItemDetail;

    public OrderItemsRecyclerAdapter(Context context, List<CustomerOrderItems> orderItems, boolean showItemDetail) {
        this.context = context;
        this.orderItems = orderItems;
        this.showItemDetail = showItemDetail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_items_recycler_item, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustomerOrderItems item = orderItems.get(position);
        holder.tv_itemQuantity.setText(item.getItemQuantity() + "  x");
        holder.tv_itemName.setText(item.getItemName());
        holder.tv_itemPrice.setText("$" + Utils.decimalFormat.format(item.getItemTotalAmount() > 0 ? item.getItemPrice() : item.getItemTotalAmount()));
        System.out.println(item.getItemName() + " ---> " + item.getItemSpicyLevel());
        if (!TextUtils.isEmpty(item.getItemSpicyLevel())) {
            holder.tv_itemSpiceLevel.setVisibility(View.VISIBLE);
            holder.tv_itemSpiceLevel.setText(item.getItemSpicyLevel());

        }
        if (!TextUtils.isEmpty(item.getItemInstructions())) {
            holder.tv_itemInstructions.setVisibility(View.VISIBLE);
            holder.tv_itemInstructions.setText(item.getItemInstructions());

        }

       /* if (!TextUtils.isEmpty(item.getItemInstructions())) {
            if (holder.tv_itemSpiceLevel.getText().toString().length() != 0)
                holder.tv_itemSpiceLevel.append(", ");
            holder.tv_itemSpiceLevel.append(item.getItemInstructions());
        }
        holder.tv_itemSpiceLevel.setVisibility(holder.tv_itemSpiceLevel.getText().toString().length() == 0?View.GONE:View.VISIBLE);

        holder.tv_itemInstructions.setText(item.getItemInstructions());*/
    }

    @Override
    public int getItemCount() {
        return orderItems != null ? orderItems.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_itemInfo)
        LinearLayout ll_itemInfo;
        @BindView(R.id.tv_itemQuantity)
        TextView tv_itemQuantity;
        @BindView(R.id.tv_itemName)
        TextView tv_itemName;
        @BindView(R.id.tv_itemPrice)
        TextView tv_itemPrice;
        @BindView(R.id.tv_itemSpiceLevel)
        TextView tv_itemSpiceLevel;
        @BindView(R.id.tv_itemInstructions)
        TextView tv_itemInstructions;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ll_itemInfo.setVisibility(showItemDetail ? View.VISIBLE : View.GONE);

        }
    }
}
