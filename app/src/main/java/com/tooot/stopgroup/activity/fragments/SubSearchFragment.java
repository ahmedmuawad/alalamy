package com.tooot.stopgroup.activity.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tooot.stopgroup.R;
import com.tooot.stopgroup.activity.CategoryListActivity;
import com.tooot.stopgroup.activity.SearchCategoryListActivity;
import com.tooot.stopgroup.adapter.SearchInnerCategoryAdapter;
import com.tooot.stopgroup.interfaces.OnItemClickListner;
import com.tooot.stopgroup.model.Home;
import com.tooot.stopgroup.utils.Constant;
import com.tooot.stopgroup.utils.RequestParamUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SubSearchFragment extends Fragment implements OnItemClickListner {


    RecyclerView rvSearchCategory;
    private List<Home.AllCategory> list = new ArrayList<>();
    private Map<Integer, List<Home.AllCategory>> childList = new HashMap<>();
    private int cat_id;
    private Bundle bundle;
    private SearchInnerCategoryAdapter searchInnerCategoryAdapter;
    View v ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_sub_search, container, false);
        //setToolbarTheme();
        //setScreenLayoutDirection();
        //settvTitle(getResources().getString(R.string.all_category));
        rvSearchCategory = v.findViewById(R.id.rvSearchCategory);
        getIntentData();
        getList(cat_id);
        if (list.size() == 0) {
            //finish();
            Intent intent = new Intent(getActivity(), CategoryListActivity.class);
            intent.putExtra(RequestParamUtils.CATEGORY, cat_id + "");
            intent.putExtra(RequestParamUtils.ORDER_BY, SearchCategoryListActivity.sortBy);
            intent.putExtra(RequestParamUtils.POSITION, SearchCategoryListActivity.sortPosition);
            startActivity(intent);
        }
        //showSearch();
        //showCart();
        //showBackButton();
        setSerachAdapter();


        return v;
    }

    public void getIntentData() {
        bundle = getArguments();
        if (bundle != null) {
            cat_id = bundle.getInt(RequestParamUtils.CATEGORY);
        }
    }

    public void setSerachAdapter() {
        searchInnerCategoryAdapter = new SearchInnerCategoryAdapter(getActivity(), this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvSearchCategory.setLayoutManager(mLayoutManager);
        rvSearchCategory.setAdapter(searchInnerCategoryAdapter);
        rvSearchCategory.setNestedScrollingEnabled(false);
        searchInnerCategoryAdapter.addAll(list, childList);
    }



    public void getList(int id) {
        for (int i = 0; i < Constant.MAINCATEGORYLIST.size(); i++) {
            if (Constant.MAINCATEGORYLIST.get(i).parent == id) {

                list.add(Constant.MAINCATEGORYLIST.get(i));
            }
        }

        for (int j = 0; j < list.size(); j++) {
            List<Home.AllCategory> tempList = new ArrayList<>();
            for (int k = 0; k < Constant.MAINCATEGORYLIST.size(); k++) {
                if (list.get(j).id.intValue() == Constant.MAINCATEGORYLIST.get(k).parent.intValue()) {
                    tempList.add(Constant.MAINCATEGORYLIST.get(k));
                }
            }
            childList.put(list.get(j).id, tempList);
        }
    }


    @Override
    public void onItemClick(int position, String value, int outerpos) {

    }
}