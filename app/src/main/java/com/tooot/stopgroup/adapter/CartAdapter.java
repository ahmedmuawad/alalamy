package com.tooot.stopgroup.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tooot.stopgroup.R;
import com.tooot.stopgroup.activity.ProductDetailActivity;
import com.tooot.stopgroup.customview.MaterialRatingBar;
import com.tooot.stopgroup.customview.roundedimageview.RoundedTransformationBuilder;
import com.tooot.stopgroup.customview.swipeview.ViewBinderHelper;
import com.tooot.stopgroup.customview.textview.TextViewBold;
import com.tooot.stopgroup.customview.textview.TextViewLight;
import com.tooot.stopgroup.customview.textview.TextViewMedium;
import com.tooot.stopgroup.customview.textview.TextViewRegular;
import com.tooot.stopgroup.helper.DatabaseHelper;
import com.tooot.stopgroup.interfaces.OnItemClickListner;
import com.tooot.stopgroup.model.Cart;
import com.tooot.stopgroup.utils.BaseActivity;
import com.tooot.stopgroup.utils.Constant;
import com.tooot.stopgroup.utils.RequestParamUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bhumi Shah on 11/7/2017.
 */

public class CartAdapter extends RecyclerView.Adapter {


    private List<Cart> list = new ArrayList<>();
    private Activity activity;
    private OnItemClickListner onItemClickListner;
    private int width = 0, height = 0;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    private DatabaseHelper databaseHelper;
    String value;
    private int isBuynow = 0;
    private final Transformation mTransformation;


    public CartAdapter(Activity activity, OnItemClickListner onItemClickListner) {
        this.activity = activity;
        this.onItemClickListner = onItemClickListner;
        databaseHelper = new DatabaseHelper(activity);
        binderHelper.setOpenOnlyOne(true);
        mTransformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(5)
                .oval(false)
                .build();
    }

    public void addAll(List<Cart> list) {
        this.list = list;
        getWidthAndHeight();
        notifyDataSetChanged();
    }

    public void isFromBuyNow(int isBuynow) {
        this.isBuynow = isBuynow;
    }

    public List<Cart> getList() {
        return list;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);

        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, final int position) {
        final CartViewHolder holder = (CartViewHolder) h;
        if (list != null && 0 <= position && position < list.size()) {
            // bindview start
            final String data = list.get(position).getCartId() + "";

            Drawable tvIncrement = holder.tvIncrement.getBackground();
            Drawable rappedDrawable = DrawableCompat.wrap(tvIncrement);
            DrawableCompat.setTint(rappedDrawable, Color.parseColor(((BaseActivity) activity).getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));


            Drawable tvDecrement = holder.tvDecrement.getBackground();
            Drawable rappedDrawabledes = DrawableCompat.wrap(tvDecrement);
            DrawableCompat.setTint(rappedDrawabledes, Color.parseColor(((BaseActivity) activity).getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));


            //    holder.ivDelete.setColorFilter(activity.getResources().getColor(R.color.red));


            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
            holder.tvPrice.setTextColor(Color.parseColor(((BaseActivity) activity).getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
            holder.tvPrice1.setTextColor(Color.parseColor(((BaseActivity) activity).getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
            holder.txtVariation.setTextColor(Color.parseColor(((BaseActivity) activity).getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
            holder.tvQuantity.setTextColor(Color.parseColor(((BaseActivity) activity).getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));

            binderHelper.closeLayout(position + "");
            if (!list.get(position).getCategoryList().averageRating.equals("")) {
                holder.ratingBar.setRating(Float.parseFloat(list.get(position).getCategoryList().averageRating));
            } else {
                holder.ratingBar.setRating(0);
            }
            if (list.get(position).getCategoryList().images.size() > 0) {
                holder.ivImage.setVisibility(View.VISIBLE);
                holder.ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Picasso.get().load(list.get(position).getCategoryList().appthumbnail)
                        .error(R.drawable.no_image_available)
                        .fit()
                        .transform(mTransformation)
                        .into(holder.ivImage);
            } else {
                holder.ivImage.setVisibility(View.INVISIBLE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            tvProductName.setText(categoryList.name + "");
                holder.tvName.setText(Html.fromHtml(list.get(position).getCategoryList().name + "", Html.FROM_HTML_MODE_LEGACY));
            } else {
//            tvProductName.setText(categoryList.name + "");
                holder.tvName.setText(Html.fromHtml(list.get(position).getCategoryList().name + ""));
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.tvPrice.setText(Html.fromHtml(list.get(position).getCategoryList().priceHtml, Html.FROM_HTML_MODE_COMPACT));

            } else {
                holder.tvPrice.setText(Html.fromHtml(list.get(position).getCategoryList().priceHtml));
            }
            holder.tvPrice.setTextSize(15);

            ((BaseActivity) activity).setPrice(holder.tvPrice, holder.tvPrice1, list.get(position).getCategoryList().priceHtml);

            // holder.tvQuantity.setTextColor(Color.parseColor(((BaseActivity) activity).getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
            holder.tvQuantity.setText(list.get(position).getQuantity() + "");

            holder.tvIncrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quntity = Integer.parseInt(holder.tvQuantity.getText().toString());
                    quntity = quntity + 1;

                    if (list.get(position).isManageStock()) {
                        if (quntity > list.get(position).getCategoryList().stockQuantity) {
                            Toast.makeText(activity, ((BaseActivity) activity).getString(R.string.only) + " " + list.get(position).getCategoryList().stockQuantity + " " + ((BaseActivity) activity).getString(R.string.quntity_is_avilable), Toast.LENGTH_SHORT).show();
                        } else {

                            holder.tvQuantity.setText(quntity + "");
                            databaseHelper.updateQuantity(quntity, list.get(position).getProductid(), list.get(position).getVariationid() + "");
                            list.get(position).setQuantity(quntity);
                            onItemClickListner.onItemClick(position, RequestParamUtils.increment, quntity);
                        }
                    } else {
                        holder.tvQuantity.setText(quntity + "");
                        databaseHelper.updateQuantity(quntity, list.get(position).getProductid(), list.get(position).getVariationid() + "");
                        list.get(position).setQuantity(quntity);
                        onItemClickListner.onItemClick(position, RequestParamUtils.increment, quntity);
                    }

                }
            });

            holder.tvDecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quntity = Integer.parseInt(holder.tvQuantity.getText().toString());
                    quntity = quntity - 1;
                    if (quntity < 1) {
                        quntity = 1;
                    }
                    holder.tvQuantity.setText(quntity + "");
                    databaseHelper.updateQuantity(quntity, list.get(position).getProductid(), list.get(position).getVariationid() + "");
                    list.get(position).setQuantity(quntity);
                    onItemClickListner.onItemClick(position, RequestParamUtils.decrement, quntity);
                }
            });

            holder.llMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isBuynow == 0) {
                        if (list.get(position).getCategoryList().type.equals(RequestParamUtils.external)) {

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getCategoryList().externalUrl));
                            activity.startActivity(browserIntent);
                        } else {
                            Constant.CATEGORYDETAIL = list.get(position).getCategoryList();
                            Intent intent = new Intent(activity, ProductDetailActivity.class);
                            intent.putExtra(RequestParamUtils.ID, list.get(position).getCategoryList().id);
                            activity.startActivity(intent);
                        }

                    }

                }
            });

            try {
                JSONObject jObject = new JSONObject(list.get(position).getVariation());
                Iterator iter = jObject.keys();
                value = "";
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    if (value.length() == 0) {
                        value = value + key + " : " + jObject.getString(key);
                    } else {
                        value = value + ", " + key + " : " + jObject.getString(key);
                    }
                }
            } catch (Exception e) {
                Log.e("exception is ", e.getMessage());
            }

            if (value!=null && !value.isEmpty()) {
                holder.txtVariation.setVisibility(View.VISIBLE);
                holder.txtVariation.setText(value + "");
            } else {
                holder.txtVariation.setVisibility(View.GONE);
            }


            holder.llDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(position).getCategoryList().type.equals(RequestParamUtils.variable)) {
                        databaseHelper.deleteVariationProductFromCart(list.get(position).getProductid(), list.get(position).getVariationid() + "");
                    } else {
                        databaseHelper.deleteFromCart(list.get(position).getProductid());
                    }
                    list.remove(position);
                    onItemClickListner.onItemClick(position, RequestParamUtils.delete, 0);
                    notifyDataSetChanged();
                }
            });
            //bind view over
        }

    }

    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link Activity #onRestoreInstanceState(Bundle)}
     */
    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivImage)
        ImageView ivImage;

        @BindView(R.id.tvName)
        TextViewMedium tvName;

        @BindView(R.id.tvPrice1)
        TextViewRegular tvPrice1;

        @BindView(R.id.tvPrice)
        TextViewRegular tvPrice;

        @BindView(R.id.txtVariation)
        TextViewLight txtVariation;

        @BindView(R.id.ratingBar)
        MaterialRatingBar ratingBar;

        @BindView(R.id.tvDecrement)
        ImageView tvDecrement;

        @BindView(R.id.tvQuantity)
        TextViewBold tvQuantity;

        @BindView(R.id.tvIncrement)
        ImageView tvIncrement;

        @BindView(R.id.ll_Delete)
        LinearLayout llDelete;

        @BindView(R.id.llMain)
        LinearLayout llMain;


        public CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public void getWidthAndHeight() {
        int height_value = activity.getResources().getInteger(R.integer.height);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels / 2 - 20;
        height = width - height_value;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}