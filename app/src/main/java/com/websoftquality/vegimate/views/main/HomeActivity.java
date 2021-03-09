package com.websoftquality.vegimate.views.main;
/**
 * @package com.websoftquality.vegimate
 * @subpackage view.main
 * @category HomeActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.obs.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.websoftquality.vegimate.BaseActivity;
import com.websoftquality.vegimate.PlaceCallActivity;
import com.websoftquality.vegimate.R;
import com.websoftquality.vegimate.SinchService;
import com.websoftquality.vegimate.configs.AppController;
import com.websoftquality.vegimate.configs.RunTimePermission;
import com.websoftquality.vegimate.configs.SessionManager;
import com.websoftquality.vegimate.datamodels.chat.ReceiveDateModel;
import com.websoftquality.vegimate.datamodels.main.JsonResponse;
import com.websoftquality.vegimate.datamodels.main.MyProfileModel;
import com.websoftquality.vegimate.interfaces.ActivityListener;
import com.websoftquality.vegimate.interfaces.ApiService;
import com.websoftquality.vegimate.interfaces.ServiceListener;
import com.websoftquality.vegimate.pushnotification.Config;
import com.websoftquality.vegimate.pushnotification.NotificationUtils;
import com.websoftquality.vegimate.utils.CommonMethods;
import com.websoftquality.vegimate.utils.RequestCallback;
import com.websoftquality.vegimate.views.chat.ChatFragment;
import com.websoftquality.vegimate.views.customize.CustomDialog;
import com.websoftquality.vegimate.views.customize.IgniterViewPager;
import com.websoftquality.vegimate.views.customize.SwipeDirection;
import com.websoftquality.vegimate.views.profile.ProfileFragment;

import static com.websoftquality.vegimate.utils.Enums.REQ_GET_MY_PROFILE;
import static com.websoftquality.vegimate.utils.Enums.REQ_UPDATE_PROFILE;

/*****************************************************************
 Application home page contain Home page fragment (Profile,home,chat)
 ****************************************************************/
public class HomeActivity extends BaseActivity implements ActivityListener, ViewPager.OnPageChangeListener, SinchService.StartFailedListener, ServiceListener {

    /**
     * @Reference Inject Global classes
     **/
    public IgniterViewPager viewPager;
    private AlertDialog dialog;
    @Inject
    SessionManager sessionManager;
    @Inject
    CommonMethods commonMethods;
    @Inject
    RunTimePermission runTimePermission;
    @Inject
    CustomDialog customDialog;
    @Inject
    ApiService apiService;
    @Inject
    Gson gson;
    private MyProfileModel myProfileModel;

    private ProgressDialog mSpinner;

    /**
     * @Reference Declare views
     **/
    private TabLayout tabLayout;
    private IgniterPageFragment igniterPageFragment;
    private int backPressed = 0;    // used by onBackPressed()
    private boolean isPermissionGranted = false;
    private BroadcastReceiver mBroadcastReceiver;
    private int matchStatus;
    private MyAdapter myAdapter;


    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_tab_layout);

        AppController.getAppComponent().inject(this);

        dialog = commonMethods.getAlertDialog(this);
//        android.content.Context context = this.getApplicationContext();
//        SinchClient sinchClient = Sinch.getSinchClientBuilder().context(context)
//                .applicationKey("bb31e6d1-5eb8-48df-9cc4-0855df1769cb")
//                .applicationSecret("COeW3ls670CI86pPd1NhZQ==")
//                .environmentHost("clientapi.sinch.com")
//                .userId("168071")
//                .build();
//
//        sinchClient.setSupportCalling(true);
//        sinchClient.setSupportManagedPush(true);
//// or
//        sinchClient.setSupportActiveConnectionInBackground(true);
//        sinchClient.startListeningOnActiveConnection();
//
//        sinchClient.addSinchClientListener(new SinchClientListener() {
//
//            public void onClientStarted(SinchClient client) { }
//
//            public void onClientStopped(SinchClient client) { }
//
//            public void onClientFailed(SinchClient client, SinchError error) { }
//
//            public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration registrationCallback) { }
//
//            public void onLogMessage(int level, String area, String message) { }
//        });
//
//        sinchClient.start();
//        Log.e("TAG", "onCreate: "+sessionManager.getUserName());

        initViews();
        initMapPlaceAPI();
    }



    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
//        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
    }

    @Override
    public void onStartFailed(SinchError error) {
//        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
//        if (mSpinner != null) {
//            mSpinner.dismiss();
//        }
    }

    @Override
    protected void onServiceConnected() {
//        mLoginButton.setEnabled(true);
        getSinchServiceInterface().setStartListener(this);
//        mSpinner.dismiss();
    }


    @Override
    public void onStarted() {
        Log.e("TAG", "onStarted: ");


//        mSpinner.dismiss();
//        openPlaceCallActivity();
    }



    private void initMapPlaceAPI() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }
    }

    /**
     * Declare variable for layout views
     */
    private void initViews() {
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        myAdapter=new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myAdapter);
        viewPager.setAllowedSwipeDirection(SwipeDirection.all);

        Intent intent = getIntent();

        if(intent.hasExtra("matchStatus")){
            sessionManager.setRefreshChatFragment(true);
            viewPager.setCurrentItem(2);
        }
        else
            viewPager.setCurrentItem(1);



        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                setupIcons();
            }
        });

        receivePushNotification();
    }

    /**
     * setup tab icons using font
     */
    public void setupIcons() {
        try {
            tabLayout.getTabAt(0).setCustomView(getTabView(R.string.ic_user_black_bg, 0));
            tabLayout.getTabAt(1).setCustomView(getTabView(R.string.ic_hot_or_burn, 1));
            tabLayout.getTabAt(2).setCustomView(getTabView(R.string.ic_chat_1, 0));

            viewPager.addOnPageChangeListener(this);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get tabs using custom text
     */
    private View getTabView(int title, int type) {
        RelativeLayout tabView = (RelativeLayout) LayoutInflater.from(HomeActivity.this).inflate(R.layout.igniter_tab_view, null);
        CustomTextView tabtext = tabView.findViewById(R.id.tv_igniter_tab_view);
        ImageView tabImage = tabView.findViewById(R.id.iv_igniter_tab_indicator);
        RelativeLayout tablay = tabView.findViewById(R.id.rlt_igniter_tab_indicator);
        RelativeLayout rltLogo = tabView.findViewById(R.id.rlt_logo);
        ImageView ivLogo = tabView.findViewById(R.id.iv_logo);
        tabtext.setText(title);

        if (type == 1) {
            tabtext.setVisibility(View.INVISIBLE);
            ivLogo.setVisibility(View.VISIBLE);
            rltLogo.setVisibility(View.INVISIBLE);
        } else {
            tabtext.setVisibility(View.VISIBLE);
            ivLogo.setVisibility(View.INVISIBLE);
            rltLogo.setVisibility(View.VISIBLE);
        }
        tabImage.setVisibility(View.GONE);
        tablay.setVisibility(View.GONE);

        return tabView;
    }

    /**
     * @Refernce Get resource from fragments
     */
    @Override
    public Resources getRes() {
        return HomeActivity.this.getResources();
    }

    /**
     * @Refernce Get Instance from fragments
     */
    @Override
    public HomeActivity getInstance() {
        return HomeActivity.this;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 1) {
        } else {
            viewPager.setAllowedSwipeDirection(SwipeDirection.all);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d("", "");
    }

    /**
     * Check all permission
     */
    private void checkAllPermission(String[] permission) {
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(HomeActivity.this, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(HomeActivity.this, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showEnablePermissionDailog();
                    }
                });
            } else {
                ActivityCompat.requestPermissions(HomeActivity.this, permission, 150);
            }
        } else {
            isPermissionGranted = true;
        }
    }

    /**
     * Request permission result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println("Never Ask again : ");

        ArrayList<String> permission = runTimePermission.onRequestPermissionsResult(permissions, grantResults);
        if (permission != null && !permission.isEmpty()) {
            runTimePermission.setFirstTimePermission(true);
            String[] dsf = new String[permission.size()];
            permission.toArray(dsf);
            //checkAllPermission(dsf);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(igniterPageFragment!=null)
            igniterPageFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * Show enable permission dialog
     */
    private void showEnablePermissionDailog() {
        if (!customDialog.isVisible()) {
            customDialog = new CustomDialog(getString(R.string.please_enable_permissions), getString(R.string.okay), new CustomDialog.btnAllowClick() {
                @Override
                public void clicked() {
                    callPermissionSettings();
                }
            });
            customDialog.show(getSupportFragmentManager(), "");
        }
    }

    /**
     * While block (Deny) call settings page
     */
    private void callPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", HomeActivity.this.getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
    }

    /**
     * Get view pager from other fragment
     */
    public IgniterViewPager getViewPager() {
        return viewPager;
    }


    public void setViewPager(IgniterViewPager viewPager) {
        this.viewPager = viewPager;
    }

    /**
     * Home on Back press function
     */
    @Override
    public void onBackPressed() {
        if (backPressed >= 1) {
            finishAffinity();
            super.onBackPressed();


        } else {
            // clean up
            backPressed = backPressed + 1;
            Toast.makeText(this, getString(R.string.press_back_exit),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * View pager direction change
     */
    public void changeDirection(String type) {
        if (viewPager.getCurrentItem() == 1) {
            if (type.equals("all"))
                viewPager.setAllowedSwipeDirection(SwipeDirection.all);
            else
                viewPager.setAllowedSwipeDirection(SwipeDirection.none);
        }
    }

    /**
     * Change chat tab layout icon
     */
    public void changeChatIcon(int type) {
        TabLayout.Tab tab = tabLayout.getTabAt(2);
        View view = tab.getCustomView();
        RelativeLayout tablay = view.findViewById(R.id.rlt_igniter_tab_indicator);
        ImageView tabImage = view.findViewById(R.id.iv_igniter_tab_indicator);
        TextView tabText = view.findViewById(R.id.tv_igniter_tab_view);
        if (type == 1) {
            tabText.setText("b");
            tabImage.setVisibility(View.VISIBLE);
            tablay.setVisibility(View.VISIBLE);
        } else {
            tabText.setText("b");
            tabImage.setVisibility(View.GONE);
            tablay.setVisibility(View.GONE);
        }
        viewPager.addOnPageChangeListener(this);
    }

    /**
     * Get notification from Fire base broadcast
     */
    public void receivePushNotification() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // FCM successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    String JSON_DATA = sessionManager.getPushNotification();

                    try {
                        JSONObject jsonObject = new JSONObject(JSON_DATA);

                        if (jsonObject.getJSONObject("custom").has("chat_status") && jsonObject.getJSONObject("custom").getJSONObject("chat_status").getString("status").equals("New message")) {

                            //getChatConversationList();
                            String cumessage = jsonObject.getJSONObject("custom").getJSONObject("chat_status").getString("message");
                            ReceiveDateModel receiveDateModel;//=new ReceiveDateModel();
                            String dateResp = String.valueOf(jsonObject.getJSONObject("custom").getJSONObject("chat_status").getJSONObject("received_date_time"));
                            changeChatIcon(1);
                            //Resumes the Fragement and Gets the data from Api
                            Fragment fragment = myAdapter.getFragment(2);
                            if (fragment != null) {
                                //fragment.onResume();
                                fragment.setUserVisibleHint(true);
                            }
                        }
                        if (jsonObject.getJSONObject("custom").has("match_status") && jsonObject.getJSONObject("custom").getJSONObject("match_status").getString("match_status").equals("Yes")){
                            changeChatIcon(1);
                            //Resumes the Fragement and Gets the data from Api
                            Fragment fragment = myAdapter.getFragment(2);
                            if (fragment != null) {
                                //fragment.onResume();
                                fragment.setUserVisibleHint(true);
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    /**
     * @Refernce Call while activity pause
     */
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        hideKeyboard(this);
    }

    /**
     * @Refernce Call while activity resume
     */
    @Override
    public void onResume() {
        super.onResume();
        hideKeyboard(this);
        getProfileDetails();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //check for runtime permission
        if (!isPermissionGranted) {
            //checkAllPermission(Constants.PERMISSIONS_STORAGE);
        }

        // register FCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    private void getProfileDetails() {
        apiService.getMyProfileDetail(sessionManager.getToken()).enqueue(new RequestCallback(REQ_GET_MY_PROFILE, this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case REQ_UPDATE_PROFILE:
                break;
            case REQ_GET_MY_PROFILE:
                if (jsonResp.isSuccess()) {
                    onSuccessGetMyProfile(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    /*if(jsonResp.getStatusMsg().equalsIgnoreCase("Token Expired")){
                        getProfileDetails();
                    }else {*/
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                    //}
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }

    private void onSuccessGetMyProfile(JsonResponse jsonResp) {
        myProfileModel = gson.fromJson(jsonResp.getStrResponse(), MyProfileModel.class);

        if (myProfileModel != null) {
            updateView();
        }
    }

    private void updateView() {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(myProfileModel.getName())) {
            sb.append(myProfileModel.getName());
            sb.append(", ");
            sb.append(myProfileModel.getAge());
//            tvUserNameAge.setText(sb.toString());
            sessionManager.setUserName(myProfileModel.getName());
            Log.e("TAG", "updateView: "+myProfileModel.getName());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!sessionManager.getUserName().equals(getSinchServiceInterface().getUserName())) {
                        getSinchServiceInterface().stopClient();
                    }

                    if (!getSinchServiceInterface().isStarted()) {
                        getSinchServiceInterface().startClient(sessionManager.getUserName());
//                        showSpinner();
                    } else {

                    }
                }
            },5000);


        }
    }


    /**
     * Adapter for tabs
     */
    private class MyAdapter extends FragmentPagerAdapter {

        private Map<Integer, String> mFragmentTags;
        private FragmentManager mFragmentManager;

        private MyAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
            mFragmentTags = new HashMap<Integer, String>();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object object = super.instantiateItem(container, position);
            if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                String tag = fragment.getTag();
                mFragmentTags.put(position, tag);
            }
            return object;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ProfileFragment();
                case 1:

                    igniterPageFragment = new IgniterPageFragment();
                    return igniterPageFragment;
                case 2:
                    return new ChatFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        public Fragment getFragment(int position) {
            Fragment fragment = null;
            String tag = mFragmentTags.get(position);
            if (tag != null) {
                fragment = mFragmentManager.findFragmentByTag(tag);
            }
            return fragment;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    /**
     * This method is To get the PutExtra value form the notificationUtils Class
     * Because In notificationutils we call HomeActivity--->ChatFragment
     * In Normal Intent We can Satisfy the Condition Because the Activity is Already Opened
     * @param intent latest Intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println("Has Match Status : One ");
        if (intent.hasExtra("matchStatus")){
            System.out.println("Has Match Status : Two ");
            sessionManager.setRefreshChatFragment(true);
            viewPager.setCurrentItem(2,true);
        }
    }
}
