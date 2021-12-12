package com.tooot.stopgroup.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.ciyashop.library.apicall.PostApi;
import com.ciyashop.library.apicall.URLS;
import com.ciyashop.library.apicall.interfaces.OnResponseListner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tooot.stopgroup.R;
import com.tooot.stopgroup.activity.ProductDetailActivity;
import com.tooot.stopgroup.customview.MaterialRatingBar;
import com.tooot.stopgroup.customview.like.animation.SparkButton;
import com.tooot.stopgroup.customview.roundedimageview.RoundedTransformationBuilder;
import com.tooot.stopgroup.customview.textview.TextViewRegular;
import com.tooot.stopgroup.javaclasses.AddToCartVariation;
import com.tooot.stopgroup.javaclasses.AddToWishList;
import com.tooot.stopgroup.model.CategoryList;
import com.tooot.stopgroup.model.Home;
import com.tooot.stopgroup.utils.BaseActivity;
import com.tooot.stopgroup.utils.Constant;
import com.tooot.stopgroup.utils.RequestParamUtils;
import com.tooot.stopgroup.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DynamicItemAdapter extends RecyclerView.Adapter<DynamicItemAdapter.MyViewHolder> implements OnResponseListner {

    public static final String TAG = "ChangeLanguageItemAdapter";
    private final LayoutInflater inflater;
    List<Home.Product> list;
    private Activity activity;
    private int width = 0, height = 0;


    public DynamicItemAdapter(Activity activity) {
        inflater = LayoutInflater.from(activity);
        this.activity = activity;
    }

    public void addAll(List<Home.Product> list) {
        this.list = list;
        getWidthAndHeight();
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_product, parent, false);
        MyViewHolder holer = new MyViewHolder(view);
        return holer;
    }

    @Override
    public void onBindViewHolder(final DynamicItemAdapter.MyViewHolder holder, final int position) {
//        holder.llMain.getLayoutParams().width = width;
        holder.llMain.getLayoutParams().height = height;

        if (!list.get(position).type.contains(RequestParamUtils.variable) && list.get(position).onSale == true) {
            ((BaseActivity) activity).showDiscount(holder.tvDiscount, list.get(position).salePrice, list.get(position).regularPrice);
        } else {
            holder.tvDiscount.setVisibility(View.GONE);
        }

        //Add product in cart if add to cart enable from admin panel
        new AddToCartVariation(activity).addToCart(holder.ivAddToCart, new Gson().toJson(list.get(position)));

        //Add product in wishlist and remove product from wishlist and check wishlist enable or not
        new AddToWishList(activity).addToWishList(holder.ivWishList, new Gson().toJson(list.get(position)), holder.tvPrice1);

        if (Constant.IS_ADD_TO_CART_ACTIVE) {
            holder.main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String productDetail = new Gson().toJson(list.get(position));
                    CategoryList categoryListRider = new Gson().fromJson(
                            productDetail, new TypeToken<CategoryList>() {
                            }.getType());
                    Constant.CATEGORYDETAIL = categoryListRider;
                    if (categoryListRider.type.equals("external")) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(categoryListRider.externalUrl));
                        activity.startActivity(browserIntent);
                    } else {
                        Intent intent = new Intent(activity, ProductDetailActivity.class);
                        activity.startActivity(intent);
                    }
                }
            });

        } else {
            holder.main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getProductDetail(String.valueOf(list.get(position).id));
                }
            });
        }

        if (list.get(position).image != null) {
            holder.ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(activity)
                    .asBitmap().format(DecodeFormat.PREFER_ARGB_8888)
                    .error(R.drawable.no_image_available)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(5)))
                    .load(list.get(position).image)
                    .into(holder.ivImage);
        } else {
            holder.ivImage.setImageResource(R.drawable.no_image_available);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            holder.tvName.setText(Html.fromHtml(list.get(position).title + "", Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.tvName.setText(Html.fromHtml(list.get(position).title + ""));
        }

        holder.tvPrice.setTextSize(15);
        if (list.get(position).priceHtml != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.tvPrice.setText(Html.fromHtml(list.get(position).priceHtml + "", Html.FROM_HTML_MODE_COMPACT));

            } else {
                holder.tvPrice.setText(Html.fromHtml(list.get(position).priceHtml) + "");
            }

        ((BaseActivity) activity).setPrice(holder.tvPrice, holder.tvPrice1, list.get(position).priceHtml);

        if (!list.get(position).rating.equals("") && list.get(position).rating != null) {
            holder.ratingBar.setRating(Float.parseFloat(list.get(position).rating));
        } else {
            holder.ratingBar.setRating(0);
        }
    }


    public void getWidthAndHeight() {
        int height_value = activity.getResources().getInteger(R.integer.height);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels / 2 - 10;
        height = width;
    }

    public void getProductDetail(String groupid) {
        if (Utils.isInternetConnected(activity)) {
            ((BaseActivity) activity).showProgress("");
            PostApi postApi = new PostApi(activity, RequestParamUtils.getProductDetail, this, ((BaseActivity) activity).getlanuage());
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(RequestParamUtils.INCLUDE, groupid);
                postApi.callPostApi(new URLS().PRODUCT_URL + ((BaseActivity) activity).getPreferences().getString(RequestParamUtils.CurrencyText, ""), jsonObject.toString());
            } catch (Exception e) {
                Log.e("Json Exception", e.getMessage());
            }
        } else {
            Toast.makeText(activity, R.string.internet_not_working, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onResponse(String response, String methodName) {
        if (methodName.equals(RequestParamUtils.getProductDetail)) {
            if (response != null && response.length() > 0) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    CategoryList categoryListRider = new Gson().fromJson(
                            jsonArray.get(0).toString(), new TypeToken<CategoryList>() {
                            }.getType());
                    Constant.CATEGORYDETAIL = categoryListRider;

                    if (categoryListRider.type.equals(RequestParamUtils.external)) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(categoryListRider.externalUrl));
                        activity.startActivity(browserIntent);
                    } else {
                        Intent intent = new Intent(activity, ProductDetailActivity.class);
                        activity.startActivity(intent);
                    }
                } catch (Exception e) {
                    Log.e(methodName + "Gson Exception is ", e.getMessage());
                }
                ((BaseActivity) activity).dismissProgress();
            }
        }


    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llMain)
        LinearLayout llMain;

        @BindView(R.id.ivAddToCart)
        ImageView ivAddToCart;

        @BindView(R.id.ll_content)
        LinearLayout ll_content;

        @BindView(R.id.ivImage)
        ImageView ivImage;

        @BindView(R.id.tvPrice)
        TextViewRegular tvPrice;

        @BindView(R.id.tvName)
        TextViewRegular tvName;

        @BindView(R.id.ratingBar)
        MaterialRatingBar ratingBar;

        @BindView(R.id.tvPrice1)
        TextViewRegular tvPrice1;


        @BindView(R.id.tvDiscount)
        TextViewRegular tvDiscount;

        @BindView(R.id.ivWishList)
        SparkButton ivWishList;

        @BindView(R.id.main)
        LinearLayout main;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
