package edios.mlr.ui.fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import edios.mlr.R;
import edios.mlr.interfaces.NotifyFetchOrder;
import edios.mlr.model.ResultOutput;
import edios.mlr.ui.adapter.OrdersRecyclerAdapter;
import edios.mlr.utils.AppConstants;
import edios.mlr.utils.SharedPref;
import edios.mlr.utils.StartDatePicker;
import edios.mlr.utils.Utils;

public class OrderHistoryFragment extends Fragment implements
        NotifyFetchOrder, View.OnClickListener, TextWatcher, RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.ordersRecycler)
    RecyclerView recyclerView;
    @BindView(R.id.et_date)
    TextView et_date;
    private Utils utils;
    private View ll_popup;
    private View rootView;
    private Unbinder unbinder;
    private String orderType;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_history, null);
        utils = new Utils(getContext());
        unbinder = ButterKnife.bind(this, rootView);
        initViews(rootView);
        SharedPref.getEditor(getActivity()).putString(AppConstants.ORDER_TYPE, AppConstants.ORDER_TYPE_TAKE_AWAY).apply();
        utils.fetchOrders(OrderHistoryFragment.this, "", "");
        et_date.addTextChangedListener(this);
        et_date.setText(getYesterdayDate());
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void initViews(View view) {
        ll_popup = view.findViewById(R.id.ll_popup);
        view.findViewById(R.id.ib_cancel).setOnClickListener(this);
        view.findViewById(R.id.ib_submit).setOnClickListener(this);
        RadioGroup rg_orderType = view.findViewById(R.id.rg_orderType);
        rg_orderType.setOnCheckedChangeListener(this);

    }

    @Override
    public void orderFetched(ResultOutput orders, boolean isSuccess) {
        recyclerView.setAdapter(new OrdersRecyclerAdapter(getActivity(), orders.getCustomerOrders(), false));
        if (recyclerView.getAdapter().getItemCount() < 1) {
            rootView.findViewById(R.id.tv_noOrderFound).setVisibility(View.VISIBLE);
        } else {
            rootView.findViewById(R.id.tv_noOrderFound).setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.iv_datePicker)
    public void onClick(View view) {
        if (view.getId() == R.id.iv_datePicker) {
            new StartDatePicker(getActivity(), et_date).show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "start_date_picker");
        } else if (view.getId() == R.id.ib_cancel) {
            this.ll_popup.setVisibility(View.GONE);
        } else if (view.getId() == R.id.ib_submit) {
            this.ll_popup.setVisibility(View.GONE);
            SharedPref.getEditor(getActivity()).putString(AppConstants.ORDER_TYPE, orderType).apply();
            utils.fetchOrders(OrderHistoryFragment.this, "", "");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat sdf1 = new SimpleDateFormat("MMM-dd-yyyy", Locale.ENGLISH);
        try {
            Date d = sdf1.parse(et_date.getText().toString());
            System.out.println("d = " + d);
            System.out.println(sdf2.format(d));
            System.out.println("getClass().getSimpleName() = " + getClass().getSimpleName());
            utils.fetchOrders(this, sdf2.format(d), getClass().getSimpleName());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void afterTextChanged(Editable editable) {

    }

    String getYesterdayDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date d = cal.getTime();
        SimpleDateFormat sdf1 = new SimpleDateFormat("MMM-dd-yyyy", Locale.ENGLISH);
        return sdf1.format(d);

    }

    @Override
    public void onDestroyView() {
        SharedPref.getEditor(getActivity()).putString(AppConstants.ORDER_TYPE, orderType).apply();
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem blItem = menu.findItem(R.id.bluetooth);
        blItem.setVisible(false);
        MenuItem item = menu.findItem(R.id.orderFilter);
        item.setVisible(true);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ll_popup.setVisibility(ll_popup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                return true;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_take_away) {
            orderType = AppConstants.ORDER_TYPE_TAKE_AWAY;
        } else if (checkedId == R.id.rb_restaurant) {
            orderType = AppConstants.ORDER_TYPE_RESTAURANT;
        }
    }

}
