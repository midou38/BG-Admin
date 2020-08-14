package edios.mlr.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edios.mlr.R;
import edios.mlr.interfaces.NotifyFetchOrder;
import edios.mlr.model.ResultOutput;
import edios.mlr.service.FetchOrderService;
import edios.mlr.utils.AppConstants;
import edios.mlr.utils.SharedPref;
import edios.mlr.utils.Utils;


public class OrdersFragment extends Fragment implements
        NotifyFetchOrder, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Utils utils;
    private Intent intent;
    private View ll_popup;
    private String orderType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, null);
        initViews(view);
        utils = new Utils(getActivity());
        intent = new Intent(getActivity(), FetchOrderService.class);
        setupViewPager();
        setHasOptionsMenu(true);
        viewPager.setOffscreenPageLimit(4);
        orderType = AppConstants.ORDER_TYPE_TAKE_AWAY;
        SharedPref.getEditor(getActivity()).putString(AppConstants.ORDER_TYPE, AppConstants.ORDER_TYPE_TAKE_AWAY).apply();
//        utils.fetchOrders(this, "", "");
        return view;
    }


    private void initViews(View view) {
        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.viewpager);
        ll_popup = view.findViewById(R.id.ll_popup);
        view.findViewById(R.id.ib_cancel).setOnClickListener(this);
        view.findViewById(R.id.ib_submit).setOnClickListener(this);
        RadioGroup rg_orderType = view.findViewById(R.id.rg_orderType);
        rg_orderType.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.refresh).setVisible(true);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) utils.fetchOrders(OrdersFragment.this, "", "");
        return super.onOptionsItemSelected(item);
    }


    private void setupViewPager() {
        System.out.println("OrdersFragment.setupViewPager");
        if (viewPager != null) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
            adapter.addFragment(new PendingOrderFragment(), getString(R.string.pending_tab));
            adapter.addFragment(new InKitchenOrderFragment(), getString(R.string.in_kitchen_tab));
            adapter.addFragment(new ReadyForPickupFragment(), getString(R.string.ready_for_pickup_tab));
            adapter.addFragment(new CompletedOrderFragment(), getString(R.string.completed_tab));
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            TextView tv1 = (TextView) (((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(0)).getChildAt(1));
            tv1.setScaleY(-1);
            TextView tv2 = (TextView) (((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(1)).getChildAt(1));
            tv2.setScaleY(-1);
            TextView tv3 = (TextView) (((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(2)).getChildAt(1));
            tv3.setScaleY(-1);
            TextView tv4 = (TextView) (((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(3)).getChildAt(1));
            tv4.setScaleY(-1);
        }
    }

    @Override
    public void orderFetched(ResultOutput orders, boolean isSuccess) {
        if (isSuccess) {
            Activity activity = getActivity();
            if (activity != null) {
                activity.sendBroadcast(new Intent(AppConstants.UPDATE_ORDERS).putExtra(AppConstants.UPDATE_ORDERS, orders));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).startService(intent);
        utils.fetchOrders(this, "", "");

    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(getActivity()).stopService(intent);
        System.out.println("OrdersFragment.onStop");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_cancel:
                this.ll_popup.setVisibility(View.GONE);
                break;
            case R.id.ib_submit:
                this.ll_popup.setVisibility(View.GONE);
                SharedPref.getEditor(getActivity()).putString(AppConstants.ORDER_TYPE, orderType).apply();
                utils.fetchOrders(OrdersFragment.this, "", "");
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_take_away) {
            orderType = AppConstants.ORDER_TYPE_TAKE_AWAY;
        } else if (checkedId == R.id.rb_restaurant) {
            orderType = AppConstants.ORDER_TYPE_RESTAURANT;
        }
    }


    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
