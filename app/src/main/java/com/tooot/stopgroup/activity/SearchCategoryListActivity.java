package com.tooot.stopgroup.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tooot.stopgroup.R;
import com.tooot.stopgroup.activity.fragments.SearchLiesnter;
import com.tooot.stopgroup.activity.fragments.SubSearchFragment;
import com.tooot.stopgroup.adapter.SearchCategoryAdapter;
import com.tooot.stopgroup.customview.GridSpacingItemDecoration;
import com.tooot.stopgroup.interfaces.OnItemClickListner;
import com.tooot.stopgroup.model.Home;
import com.tooot.stopgroup.utils.BaseActivity;
import com.tooot.stopgroup.utils.Constant;
import com.tooot.stopgroup.utils.RequestParamUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchCategoryListActivity extends BaseActivity implements OnItemClickListner , SearchLiesnter {

    @BindView(R.id.rvSearchCategory)
    RecyclerView rvSearchCategory;

    @BindView(R.id.linFragContainer)
    LinearLayout container ;

    @BindView(R.id.svHome)
    NestedScrollView svHome;

    @BindView(R.id.svList)
    NestedScrollView svList;

    private       SearchCategoryAdapter  searchCategoryAdapter;
    private       Bundle                 bundle;
    private       String                 from;
    public static int                    sortPosition;
    private       List<Home.AllCategory> list = new ArrayList<>();
    public static String                 search, sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_category_list);
        ButterKnife.bind(this);
        setToolbarTheme();
        setScreenLayoutDirection();
        settvTitle(getResources().getString(R.string.all_category));
        getIntentData();
        showSearch();
        showCart();
        showBackButton();
        setSerachAdapter();
        setBottomBar("search", svHome);
        setBottomBar("search", svList);
        setFragScreen();
    }

    private void setFragScreen() {
        Fragment subSearchFragment = new SubSearchFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putInt(RequestParamUtils.CATEGORY , 210);
        subSearchFragment.setArguments(bundle);
        transaction.replace(R.id.linFragContainer, subSearchFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void getIntentData() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            from = bundle.getString(RequestParamUtils.from);
            search = bundle.getString(RequestParamUtils.SEARCH);
            sortBy = bundle.getString(RequestParamUtils.ORDER_BY);
            sortPosition = bundle.getInt(RequestParamUtils.POSITION);
        }
    }

// intent.putExtra(RequestParamUtils.ORDER_BY, Constant.getSortList().get(sortAdapter.getSelectedPosition()).getSyntext());
//            intent.putExtra(RequestParamUtils.POSITION,sortPosition);

    public void setSerachAdapter() {
        searchCategoryAdapter = new SearchCategoryAdapter(this, this , this);

        //final GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
        rvSearchCategory.setLayoutManager(new LinearLayoutManager(this));
        rvSearchCategory.setAdapter(searchCategoryAdapter);
        rvSearchCategory.setNestedScrollingEnabled(false);
        searchCategoryAdapter.setFrom(from);
        rvSearchCategory.setNestedScrollingEnabled(false);
        rvSearchCategory.setHasFixedSize(true);
        rvSearchCategory.setItemViewCacheSize(20);
        //rvSearchCategory.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10), true));
        for (int i = 0; i < Constant.MAINCATEGORYLIST.size(); i++) {
            if (Constant.MAINCATEGORYLIST.get(i).parent == 0) {
                list.add(Constant.MAINCATEGORYLIST.get(i));
            }
        }
        searchCategoryAdapter.addAll(list);

    }

    @Override
    public void onItemClick(int position, String value, int outerPos) {

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        showCart();
    }

    @Override
    public void onItemClicked(Integer id) {
        Fragment subSearchFragment = new SubSearchFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putInt(RequestParamUtils.CATEGORY , id);
        subSearchFragment.setArguments(bundle);
        transaction.replace(R.id.linFragContainer, subSearchFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
