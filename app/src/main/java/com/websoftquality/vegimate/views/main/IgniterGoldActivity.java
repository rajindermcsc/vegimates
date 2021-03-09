package com.websoftquality.vegimate.views.main;
/**
 * @package com.websoftquality.vegimate
 * @subpackage view.main
 * @category IgniterGoldActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.obs.CustomTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import com.websoftquality.vegimate.R;
import com.websoftquality.vegimate.adapters.main.ViewPagerAdapter;
import com.websoftquality.vegimate.configs.AppController;
import com.websoftquality.vegimate.configs.Constants;
import com.websoftquality.vegimate.configs.SessionManager;
import com.websoftquality.vegimate.datamodels.main.ImageListModel;
import com.websoftquality.vegimate.datamodels.main.JsonResponse;
import com.websoftquality.vegimate.datamodels.main.SliderModel;
import com.websoftquality.vegimate.interfaces.ApiService;
import com.websoftquality.vegimate.interfaces.ServiceListener;
import com.websoftquality.vegimate.utils.CommonMethods;
import com.websoftquality.vegimate.utils.RequestCallback;
import com.websoftquality.vegimate.views.customize.CirclePageIndicator;
import com.websoftquality.vegimate.views.customize.CustomDialog;

/*****************************************************************
 Show Igniter Gold plan dialog page (Now its dynamic in IgniterPlusDialogActivity)
 ****************************************************************/
public class IgniterGoldActivity extends AppCompatActivity implements View.OnClickListener, ServiceListener {

    @Inject
    ApiService apiService;
    @Inject
    CommonMethods commonMethods;
    @Inject
    CustomDialog customDialog;
    @Inject
    SessionManager sessionManager;
    @Inject
    Gson gson;
    private ViewPagerAdapter PagerAdapter;
    private ViewPager viewPager;
    private CirclePageIndicator pageIndicator;
    private RelativeLayout rlt_tutorial, lltTwelveMonth, lltSixMonth, lltOneMonth;
    private CustomTextView tvSixMonthSave, tvTwelveMonthSave;
    private CustomTextView tvSixMonthBook, tvTwelveMonthBook;
    private CustomTextView tvTwelveMonth, tvSixMonth, tvOneMonth;
    private CustomTextView tvPerYear, tvPerSixMonth, tvPerOneMonth;
    private CustomTextView tvYearPrice, tvSixMonthPrice, tvOneMonthPrice;
    private RelativeLayout rltTwelveMonth, rltSixMonth, rltOneMonth;
    String plan_name,plan_type,plan_price;
    Button btn_continue;
    private AlertDialog dialog;
    private ArrayList<ImageListModel> imageList = new ArrayList<>();
    private Handler handler;
    private Runnable runnable;
    private int delay = 3000; //milliseconds
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.getAppComponent().inject(this);
        setContentView(R.layout.activity_igniter_gold);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);

        initView();
        initClickListener();
        setViewPagerAdapter();
        initPageIndicator();
        initViewPagerListener();
        getSliderImages();
    }

    private void initView() {
        viewPager = findViewById(R.id.vp_igniter_plus);
        pageIndicator = findViewById(R.id.indicator);
        btn_continue = findViewById(R.id.btn_continue);

        dialog = commonMethods.getAlertDialog(this);

        rlt_tutorial = findViewById(R.id.rlt_tutorial);

        lltOneMonth = findViewById(R.id.llt_one_month);
        lltSixMonth = findViewById(R.id.llt_six_month);
        lltTwelveMonth = findViewById(R.id.llt_twelve_month);

        rltOneMonth = findViewById(R.id.rlt_one_month);
        rltSixMonth = findViewById(R.id.rlt_six_month);
        rltTwelveMonth = findViewById(R.id.rlt_twelve_month);

        tvOneMonth = findViewById(R.id.tv_one_month);
        tvSixMonth = findViewById(R.id.tv_six_month);
        tvTwelveMonth = findViewById(R.id.tv_twelve_month);

        tvOneMonthPrice = findViewById(R.id.tv_month_price);
        tvSixMonthPrice = findViewById(R.id.tv_six_month_price);
        tvYearPrice = findViewById(R.id.tv_year_price);

        tvPerOneMonth = findViewById(R.id.tv_per_month);
        tvPerSixMonth = findViewById(R.id.tv_per_six_month);
        tvPerYear = findViewById(R.id.tv_per_year);

        tvSixMonthSave = findViewById(R.id.tv_six_month_save);
        tvTwelveMonthSave = findViewById(R.id.tv_one_year_save);
        tvSixMonthBook = findViewById(R.id.tv_six_month_book);
        tvTwelveMonthBook = findViewById(R.id.tv_one_year_book);
        btn_continue.setOnClickListener(this);
    }

    private void getSliderImages() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.igniterPlusSlider(sessionManager.getToken()).enqueue(new RequestCallback(this));
    }

    private void setViewPagerAdapter() {

        handler = new Handler();
        PagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Constants.VP_GET_GOLD_SLIDER, 9, imageList);
        viewPager.setAdapter(PagerAdapter);
        pageIndicator.setViewPager(viewPager);

        runnable = new Runnable() {
            public void run() {
                page = viewPager.getCurrentItem();

                if (PagerAdapter.getCount() - 1 == page) {
                    page = 0;
                } else {
                    page++;
                }
                viewPager.setCurrentItem(page, true);
                handler.postDelayed(this, delay);
            }
        };
    }

    private void initClickListener() {
        lltOneMonth.setOnClickListener(this);
        lltSixMonth.setOnClickListener(this);
        lltTwelveMonth.setOnClickListener(this);
    }

    /**
     * Method called for make circle page indicator setup.
     */
    private void initPageIndicator() {
        final float density = getResources().getDisplayMetrics().density;
        pageIndicator.setRadius(5 * density);
        pageIndicator.setPageColor(ContextCompat.getColor(this, R.color.transparent));
        pageIndicator.setFillColor(ContextCompat.getColor(this, R.color.gold3));
        pageIndicator.setStrokeColor(ContextCompat.getColor(this, R.color.light_gray1));
        pageIndicator.setStrokeWidth(1);
        viewPager.setCurrentItem(0);
        pageIndicator.setOnClickListener(null);
    }

    /**
     * Method called for initiate listener which triggered get tutorial page
     * navigation.
     */
    private void initViewPagerListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        viewPager.setCurrentItem(0);
                        pageIndicator.setCurrentItem(0);
                        //rlt_tutorial.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.gradient_rewind));
                        break;
                    case 1:
                        viewPager.setCurrentItem(1);
                        pageIndicator.setCurrentItem(1);
                        //rlt_tutorial.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.gradient_boost));
                        break;
                    case 2:
                        viewPager.setCurrentItem(2);
                        pageIndicator.setCurrentItem(2);
                        //rlt_tutorial.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.gradient_choose));
                        break;
                    case 3:
                        viewPager.setCurrentItem(3);
                        pageIndicator.setCurrentItem(3);
                        //rlt_tutorial.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.gradient_control_profile));
                        break;
                    case 4:
                        viewPager.setCurrentItem(4);
                        pageIndicator.setCurrentItem(4);
                        //rlt_tutorial.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.gradient_superlike));
                        break;
                    case 5:
                        viewPager.setCurrentItem(5);
                        pageIndicator.setCurrentItem(5);
                        //rlt_tutorial.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.gradient_swipearround));
                        break;
                    case 6:
                        viewPager.setCurrentItem(6);
                        pageIndicator.setCurrentItem(6);
                        //rlt_tutorial.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.gradient_likes));
                        break;
                    case 7:
                        viewPager.setCurrentItem(7);
                        pageIndicator.setCurrentItem(7);
                        //rlt_tutorial.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.gradient_turnoffads));
                        break;
                    case 8:
                        viewPager.setCurrentItem(8);
                        pageIndicator.setCurrentItem(8);
                        //rlt_tutorial.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.gradient_turnoffads));
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        Animation animZoomOutIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out_in);
        switch (v.getId()) {
            case R.id.btn_continue:
//                Intent intent=new Intent(IgniterGoldActivity.this,StripeCardPaymentActivity.class);
//                intent.putExtra("plan_id",currentPlanId);
//                intent.putExtra("plan_name",plan_name);
//                intent.putExtra("plan_price",plan_price);
//                intent.putExtra("plan_type",type);
//                startActivity(intent);
                break;
            case R.id.llt_one_month:
                changeViewBg(1);
                changeViewColor(1);
                lltOneMonth.startAnimation(animZoomOutIn);
                plan_price = tvOneMonthPrice.getText().toString();
                plan_name = tvOneMonth.getText().toString();
                break;
            case R.id.llt_six_month:
                changeViewBg(6);
                changeViewColor(6);
                lltSixMonth.startAnimation(animZoomOutIn);
                plan_price = tvSixMonthPrice.getText().toString();
                plan_name = tvSixMonth.getText().toString();
                break;
            case R.id.llt_twelve_month:
                changeViewBg(12);
                changeViewColor(12);
                lltTwelveMonth.startAnimation(animZoomOutIn);
                plan_price = tvYearPrice.getText().toString();
                plan_name = tvTwelveMonth.getText().toString();
                break;
            default:
                break;
        }
    }

    private void changeViewColor(int value) {
        tvOneMonth.setTextColor(getTextColor(value, 1));
        tvOneMonthPrice.setTextColor(getTextColor(value, 1));
        tvPerOneMonth.setTextColor(getTextColor(value, 1));
        tvSixMonth.setTextColor(getTextColor(value, 6));
        tvSixMonthPrice.setTextColor(getTextColor(value, 6));
        tvPerSixMonth.setTextColor(getTextColor(value, 6));
        tvSixMonthSave.setTextColor(getTextColor(value, 6));
        tvTwelveMonth.setTextColor(getTextColor(value, 12));
        tvYearPrice.setTextColor(getTextColor(value, 12));
        tvPerYear.setTextColor(getTextColor(value, 12));
        tvTwelveMonthSave.setTextColor(getTextColor(value, 12));
    }

    private int getTextColor(int value1, int value2) {
        return (value1 == value2) ? ContextCompat.getColor(this, R.color.gold1) : ContextCompat.getColor(this, R.color.black);
    }

    private void changeViewBg(int value) {
        rltOneMonth.setBackgroundResource((value == 1) ? R.drawable.rect_gold_border : R.drawable.bottom_line);
        rltSixMonth.setBackgroundResource((value == 6) ? R.drawable.rect_gold_border : R.drawable.bottom_line);
        rltTwelveMonth.setBackgroundResource((value == 12) ? R.drawable.rect_gold_border : R.drawable.bottom_line);
        tvSixMonthSave.setVisibility((value == 6) ? View.VISIBLE : View.INVISIBLE);
        tvTwelveMonthSave.setVisibility((value == 12) ? View.VISIBLE : View.INVISIBLE);
        tvSixMonthBook.setVisibility((value == 6) ? View.VISIBLE : View.INVISIBLE);
        tvTwelveMonthBook.setVisibility((value == 12) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            commonMethods.showMessage(this, dialog, data);
            return;
        }

        if (jsonResp.isSuccess()) {
            onSuccessIgniterPlus(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) commonMethods.showMessage(this, dialog, data);
    }

    private void onSuccessIgniterPlus(JsonResponse jsonResp) {
        SliderModel sliderModel = gson.fromJson(jsonResp.getStrResponse(), SliderModel.class);
        if (sliderModel != null && sliderModel.getIgniterPlusImgList() != null && sliderModel.getIgniterPlusImgList().size() > 0) {
            imageList.clear();
            imageList.addAll(sliderModel.getIgniterPlusImgList());
        }
        //setViewPagerAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}

