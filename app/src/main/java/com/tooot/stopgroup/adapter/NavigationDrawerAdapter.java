package com.tooot.stopgroup.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.graphics.drawable.DrawableCompat;

import com.tooot.stopgroup.R;
import com.tooot.stopgroup.customview.textview.TextViewRegular;
import com.tooot.stopgroup.helper.DatabaseHelper;
import com.tooot.stopgroup.model.Home;
import com.tooot.stopgroup.model.NavigationList;
import com.tooot.stopgroup.utils.BaseActivity;
import com.tooot.stopgroup.utils.Config;
import com.tooot.stopgroup.utils.Constant;
import com.tooot.stopgroup.utils.RequestParamUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UV on 29-Nov-16.
 */
public class NavigationDrawerAdapter extends BaseAdapter {
    private Activity                context;
    private List<Home.MainCategory> list = new ArrayList<>();
    private LayoutInflater          inflater;
    private int                     seprater;
    private DatabaseHelper          databaseHelper;

    public NavigationDrawerAdapter(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        databaseHelper = new DatabaseHelper(context);
    }

    public void addAll(List<Home.MainCategory> list) {
        this.list = list;

        for(int i=0;i<list.size();i++) {
            if(list.get(i).mainCatName.contains(context.getResources().getString(R.string.order))) {
                if (Config.IS_CATALOG_MODE_OPTION) {
                     list.remove(i);
                }
            }
        }
        notifyDataSetChanged();

    }


    public List<Home.MainCategory> getDrawerList() {
        return list;
    }

    public void setSepreter(int seprater) {
        this.seprater = seprater;
    }

    public int getSeprater() {
        return this.seprater;
    }

    public List<Home.MainCategory> getList() {
        return this.list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        NavigationDrawerViewHolder listViewHolder;
        listViewHolder = new NavigationDrawerViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_nav, viewGroup, false);

            listViewHolder.tvName = (TextViewRegular) convertView.findViewById(R.id.tvName);
            listViewHolder.tvCart = convertView.findViewById(R.id.tvToolCart);
            listViewHolder.llMain = convertView.findViewById(R.id.llMain);
            listViewHolder.tvDivider = (TextViewRegular) convertView.findViewById(R.id.tvDivider);
            listViewHolder.tvDividerGray = (TextViewRegular) convertView.findViewById(R.id.tvDividerGray);
            listViewHolder.ivLeft = (ImageView) convertView.findViewById(R.id.ivLeft);
            convertView.setTag(listViewHolder);

        } else {
            listViewHolder = (NavigationDrawerViewHolder) convertView.getTag();
        }

        if (i != seprater) {
            listViewHolder.tvDivider.setVisibility(View.VISIBLE);
            listViewHolder.tvDividerGray.setVisibility(View.GONE);
        } else {
            listViewHolder.tvDivider.setVisibility(View.GONE);
            listViewHolder.tvDividerGray.setVisibility(View.VISIBLE);
        }
        if (list.get(i).mainCatName != null && !list.get(i).mainCatName.equals("")) {
            listViewHolder.tvName.setText(list.get(i).mainCatName);
        }
        if (i == seprater) {
            listViewHolder.ivLeft.setImageResource(R.drawable.ic_more_white);

        } else if (i < seprater) {
            if (list.get(i).mainCatImage != null && !list.get(i).mainCatImage.equals("")) {
                Picasso.get().load(list.get(i).mainCatImage).into(listViewHolder.ivLeft);
            }
            listViewHolder.ivLeft.getLayoutParams().width = ((BaseActivity)context).dpToPx(40);
            listViewHolder.ivLeft.getLayoutParams().height = ((BaseActivity)context).dpToPx(40);
        } else {
            listViewHolder.ivLeft.setColorFilter(Color.parseColor(((BaseActivity)context).getPreferences().getString(Constant.SECOND_COLOR,Constant.SECONDARY_COLOR)));
            listViewHolder.ivLeft.setImageResource(NavigationList.getInstance(context).getImageList().get(Integer.parseInt(list.get(i).mainCatId)));
            listViewHolder.ivLeft.getLayoutParams().width = ((BaseActivity)context).dpToPx(30);
            listViewHolder.ivLeft.getLayoutParams().height = ((BaseActivity)context).dpToPx(30);
        }

        if (list.get(i).mainCatName.equals(RequestParamUtils.myCart)) {

            if (databaseHelper.getFromCart(0).size() > 0) {
                listViewHolder.tvCart.setText(databaseHelper.getFromCart(0).size() + "");
                listViewHolder.tvCart.setVisibility(View.VISIBLE);
            } else {
                listViewHolder.tvCart.setVisibility(View.GONE);
            }

        } else {
            listViewHolder.tvCart.setVisibility(View.GONE);
        }

        Drawable unwrappedDrawable =  listViewHolder.tvCart.getBackground();
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, Color.parseColor(((BaseActivity)context).getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));

//        Log.e("Value of navigation ", "is " + i);

//        if (i < seprater) {
//            listViewHolder.llMain.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, CategoryListActivity.class);
//                    intent.putExtra(RequestParamUtils.CATEGORY, list.get(i).mainCatId);
//                    intent.putExtra(RequestParamUtils.IS_WISHLIST_ACTIVE, Constant.IS_WISH_LIST_ACTIVE);
//                    context.startActivity(intent);
//                }
//            });
//
//        } else if (i == seprater) {
//            listViewHolder.llMain.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, SearchCategoryListActivity.class);
//                    context.startActivity(intent);
//
//                }
//            });
//
//        }
        return convertView;
    }
}

class NavigationDrawerViewHolder {

    TextViewRegular tvName, tvDivider, tvDividerGray;
    ImageView       ivLeft;
    LinearLayout    llMain;
    TextViewRegular tvCart;
}