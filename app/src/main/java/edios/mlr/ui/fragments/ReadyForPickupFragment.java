package edios.mlr.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import edios.mlr.R;
import edios.mlr.model.ResultOutput;
import edios.mlr.ui.adapter.OrdersRecyclerAdapter;
import edios.mlr.utils.AppConstants;
import edios.mlr.utils.Utils;

public class ReadyForPickupFragment extends Fragment {

    View rootView;
    private OrdersRecyclerAdapter ordersRecyclerAdapter;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ResultOutput resultOutput = intent.hasExtra(AppConstants.UPDATE_ORDERS) ? (ResultOutput) intent.getParcelableExtra(AppConstants.UPDATE_ORDERS) : new ResultOutput();
            if (ordersRecyclerAdapter != null) {
                ordersRecyclerAdapter.updateList(new Utils(getActivity()).orderList(AppConstants.READY_FOR_PICKUP_ORDER_STATUS, resultOutput.getCustomerOrders()));
                ordersRecyclerAdapter.notifyDataSetChanged();
                if (ordersRecyclerAdapter.getItemCount() < 1) {
                    rootView.findViewById(R.id.tv_noOrderFound).setVisibility(View.VISIBLE);
                }else{
                    rootView.findViewById(R.id.tv_noOrderFound).setVisibility(View.GONE);
                }

            }
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Objects.requireNonNull(getActivity()).registerReceiver(receiver, new IntentFilter(AppConstants.UPDATE_ORDERS));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Objects.requireNonNull(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ready_for_pickup, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.ordersRecycler);
        ordersRecyclerAdapter = new OrdersRecyclerAdapter(getActivity(), new ArrayList<>(), true);
        recyclerView.setAdapter(ordersRecyclerAdapter);
        return rootView;
    }


}

