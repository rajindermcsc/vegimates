package com.websoftquality.vegimate.views.main;
/**
 * @package com.websoftquality.vegimate
 * @subpackage view.main
 * @category SplashActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.websoftquality.vegimate.R;
import com.websoftquality.vegimate.configs.AppController;
import com.websoftquality.vegimate.configs.Constants;
import com.websoftquality.vegimate.configs.RunTimePermission;
import com.websoftquality.vegimate.configs.SessionManager;
import com.websoftquality.vegimate.datamodels.main.AppSiteModel;
import com.websoftquality.vegimate.datamodels.main.ImageListModel;
import com.websoftquality.vegimate.datamodels.main.JsonResponse;
import com.websoftquality.vegimate.datamodels.main.SliderModel;
import com.websoftquality.vegimate.interfaces.ApiService;
import com.websoftquality.vegimate.interfaces.ServiceListener;
import com.websoftquality.vegimate.utils.CommonMethods;
import com.websoftquality.vegimate.utils.RequestCallback;
import com.websoftquality.vegimate.views.chat.ChatConversationActivity;
import com.websoftquality.vegimate.views.customize.CustomDialog;
import com.websoftquality.vegimate.views.signup.SignUpActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static com.websoftquality.vegimate.utils.Enums.REQ_FB_SIGNUP;
import static com.websoftquality.vegimate.utils.Enums.REQ_GET_LOGIN_SLIDER;

/*****************************************************************
 Application splash screen
 ****************************************************************/
public class SplashActivity extends AppCompatActivity implements ServiceListener {

    private static final String TAG = "SplashActivity";
    @Inject
    SessionManager sessionManager;
    private ArrayList<ImageListModel> imageList = new ArrayList<>();
    private ArrayList<AppSiteModel> appdata = new ArrayList<>();
    @Inject
    CommonMethods commonMethods;
    @Inject
    RunTimePermission runTimePermission;
    @Inject
    CustomDialog customDialog;
    @Inject
    ApiService apiService;
    private AlertDialog dialog;
    @Inject
    Gson gson;
    ImageView iv_logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppController.getAppComponent().inject(this);
        iv_logo=findViewById(R.id.iv_logo);
        iv_logo.setVisibility(View.GONE);
        dialog = commonMethods.getAlertDialog(this);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }

        getIntentValues();
//        getSliderImageList();
    }

    private void getSliderImageList() {
        commonMethods.showProgressDialog(SplashActivity.this, customDialog);
        apiService.getTutorialSliderImg().enqueue(new RequestCallback(REQ_GET_LOGIN_SLIDER, this));
    }

    private void getIntentValues() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callActivityIntent();
            }
        }, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            commonMethods.showMessage(SplashActivity.this, dialog, data);
            return;
        }
        String statusCode = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "status_code", String.class);
        if (jsonResp.getRequestCode() == REQ_GET_LOGIN_SLIDER && jsonResp.isSuccess()) {
            onSuccessGetSliderImg(jsonResp);
        } else {
            commonMethods.showMessage(SplashActivity.this, dialog, jsonResp.getStatusMsg());
        }
    }

    private void onSuccessGetSliderImg(JsonResponse jsonResp) {
        SliderModel sliderModel = gson.fromJson(jsonResp.getStrResponse(), SliderModel.class);
        sessionManager.setMinAge(sliderModel.getMinimumAge());
        sessionManager.setMaxAge(sliderModel.getMaximumAge());
        if (sliderModel != null && sliderModel.getImageList() != null && sliderModel.getImageList().size() > 0) {
            imageList.clear();
            imageList.addAll(sliderModel.getImageList());
        }

        if (sliderModel != null && sliderModel.getAppDataList() != null && sliderModel.getAppDataList().size() > 0) {
            appdata.clear();
            appdata.addAll(sliderModel.getAppDataList());
        }

        for (int i=0;i<appdata.size();i++){
            if (appdata.get(i).getName().equalsIgnoreCase("logo_web_1")){
                Log.e(TAG, "onSuccessGetSliderImg: "+"https://vegi-mate.com/dating/public/logos/"+appdata.get(i).getValue());
                Glide.with(this)
                        .load("https://vegi-mate.com/dating/public/logos/"+appdata.get(i).getValue())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(iv_logo);
            }

        }


        Log.e(TAG, "onSuccessGetSliderImg: "+imageList);

    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) commonMethods.showMessage(SplashActivity.this, dialog, data);
    }

    private void callActivityIntent() {
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("MainActivity: ", "Key: " + key + " Value: " + value);
            }
            if (getIntent().hasExtra("custom") && !TextUtils.isEmpty(sessionManager.getToken())) {
                String status = "";
                try {
                    JSONObject custom = new JSONObject(getIntent().getStringExtra("custom"));

                    System.out.println("Json object : String "+custom.toString());

                    if (custom.has("match_status")){
                        System.out.println("Json object : String "+custom.getString("match_status"));
                       goToChatFragment();
                    }else{
                        System.out.println("Json object : No Match status");

                        status = custom.getJSONObject("chat_status").getString("status");
                        getToIntent(status, custom);
                    }


                } catch (JSONException e) {
                    goNormalIntent();
                }
            } else {
                goNormalIntent();
            }
        } else {
            goNormalIntent();
        }
    }

    private void goToChatFragment() {
        Intent notificationIntent = new Intent(this, HomeActivity.class);
        notificationIntent.putExtra("matchStatus", true);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(notificationIntent);
    }

    public void goNormalIntent() {
        if (TextUtils.isEmpty(sessionManager.getToken())) {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("isFromSignUp", false);
            startActivity(intent);
        }
    }

    public void getToIntent(String status, JSONObject jsonObject) {

        Intent notificationIntent;
        notificationIntent = new Intent(getApplicationContext(), ChatConversationActivity.class);
        if (status.equals("New message")) {
            notificationIntent = newMessage(jsonObject);
        }
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(notificationIntent);
        finish();
    }

    public Intent newMessage(JSONObject jsons) {
        try {
            //JSONObject data = new JSONObject(String.valueOf(jsons));
            Intent notificationIntent = new Intent(getApplicationContext(), ChatConversationActivity.class);
            notificationIntent.putExtra("matchId", Integer.valueOf(jsons.getJSONObject("chat_status").getString("match_id")));
            notificationIntent.putExtra("userId", Integer.valueOf(jsons.getJSONObject("chat_status").getString("sender_id")));
            return notificationIntent;
        } catch (JSONException e) {

        }
        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
