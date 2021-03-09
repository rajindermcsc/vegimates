package com.websoftquality.vegimate.views.profile;
/**
 * @package com.websoftquality.vegimate
 * @subpackage view.profile
 * @category EditProfileActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.obs.CustomTextView;
import com.websoftquality.vegimate.R;
import com.websoftquality.vegimate.adapters.main.QuestionsAdapter;
import com.websoftquality.vegimate.configs.AppController;
import com.websoftquality.vegimate.configs.SessionManager;
import com.websoftquality.vegimate.datamodels.main.JsonResponse;
import com.websoftquality.vegimate.datamodels.main.SettingsModel;
import com.websoftquality.vegimate.iaputils.IabBroadcastReceiver;
import com.websoftquality.vegimate.interfaces.ApiService;
import com.websoftquality.vegimate.interfaces.ServiceListener;
import com.websoftquality.vegimate.utils.Apierror_handle;
import com.websoftquality.vegimate.utils.CommonMethods;
import com.websoftquality.vegimate.utils.Loading;
import com.websoftquality.vegimate.utils.MessageToast;
import com.websoftquality.vegimate.utils.RequestCallback;
import com.websoftquality.vegimate.views.customize.CustomDialog;
import com.websoftquality.vegimate.views.main.UserNameActivity;

import java.util.HashMap;

import javax.inject.Inject;

import static com.websoftquality.vegimate.utils.Enums.REQ_GET_SETTINGS;
import static com.websoftquality.vegimate.utils.Enums.REQ_UPDATE_SETTINGS;

/*****************************************************************
 User match profile search setting pageNSLocalizedString
 ****************************************************************/
public class AdvancedSettings extends AppCompatActivity implements  View.OnClickListener, ServiceListener{


    static final String TAG = "Boost In App Purchase";

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
    IabBroadcastReceiver mBroadcastReceiver;

    private CustomTextView  tvBackArrow;
    private AlertDialog dialog;
    private SettingsModel settingsModel;
    private String location = "", showMe = "", userName = "", newMatch = "", receivingMsg = "", msgLikes = "", superLike = "", adminDistanceType = "", distanceType = "", showMen = "", showWomen = "", matchingProfile = "";
    private String profileUrl = "", helpUrl = "", licenseUrl = "", privacyPolicyUrl = "", termsOfServiceUrl = "", communityUrl = "", safetyUrl = "", message = "";
    private String maxDistance, minAge, maxAge;
    RecyclerView rv_questions;
    LinearLayoutManager linearLayoutManager1;
    QuestionsAdapter questionsAdapter;
    Loading loading;
    Apierror_handle apierror_handle;
    MessageToast messageToast;
    private CustomTextView tvHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_settings);
        AppController.getAppComponent().inject(this);
        loading=new Loading(this);
        apierror_handle=new Apierror_handle(this);
        tvHeader = findViewById(R.id.tv_header_title);
        tvHeader.setText("Advanced Settings");
        messageToast=new MessageToast(this);

        rv_questions=findViewById(R.id.rv_questions);

        initView();
        initClickListeners();
        getSettingsDetails();
       
    }

    

    private void initView() {

        helpUrl = getResources().getString(R.string.redirect_url) + "" + getResources().getString(R.string.help_url);
        privacyPolicyUrl = getResources().getString(R.string.redirect_url) + "" + getResources().getString(R.string.privacy_url);
        termsOfServiceUrl = getResources().getString(R.string.redirect_url) + "" + getResources().getString(R.string.terms_url);

        System.out.println("Help url " + helpUrl);
        System.out.println("privacyPolicy url " + privacyPolicyUrl);
        System.out.println("termsOfService url " + termsOfServiceUrl);

        tvBackArrow = findViewById(R.id.tv_left_arrow);




        dialog = commonMethods.getAlertDialog(this);

        initRecyclerView();

    }

    private void initRecyclerView() {
        
    }

    private void initClickListeners() {
        tvBackArrow.setOnClickListener(this);



      
    }

    private void getSettingsDetails() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.getUserSettings(sessionManager.getToken()).enqueue(new RequestCallback(REQ_GET_SETTINGS, this));
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tv_left_arrow:
                

                commonMethods.showProgressDialog(this, customDialog);
                apiService.updateSettings(getParams()).enqueue(new RequestCallback(REQ_UPDATE_SETTINGS, this));
                break;

            case R.id.tv_user_name:
                intent = new Intent(AdvancedSettings.this, UserNameActivity.class);
                startActivityForResult(intent, 100);
                break;

            default:
                break;
        }
    }






    private HashMap<String, String> getParams() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", sessionManager.getToken());
        hashMap.put("matching_profile", settingsModel.getMatchingProfile());
        hashMap.put("distance", String.valueOf(settingsModel.getMaxDistance()));
        hashMap.put("min_age", String.valueOf(settingsModel.getMinAge()));
        hashMap.put("max_age", String.valueOf(settingsModel.getMaxAge()));
        hashMap.put("show_me", settingsModel.getShowMe());
        hashMap.put("distance_type", settingsModel.getDistanceType());
        hashMap.put("new_matches", settingsModel.getNewMatch());
        hashMap.put("messages", "yes");
        hashMap.put("message_likes", settingsModel.getMessageLikes());
        hashMap.put("super_likes", settingsModel.getSuperLikes());
        hashMap.put("hobbies", "");
        /*hashMap.put("latitude", "100");
        hashMap.put("longitude", "97");*/

        return hashMap;
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case REQ_GET_SETTINGS:
                if (jsonResp.isSuccess()) onSuccessGetSettings(jsonResp);
                break;
            case REQ_UPDATE_SETTINGS:
                if (jsonResp.isSuccess()) {
                    onBackPressed();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) commonMethods.showMessage(this, dialog, data);
    }

    private void onSuccessGetSettings(JsonResponse jsonResp) {

        settingsModel = gson.fromJson(jsonResp.getStrResponse(), SettingsModel.class);
        linearLayoutManager1=new LinearLayoutManager(this);
        questionsAdapter=new QuestionsAdapter(getApplicationContext(),settingsModel,sessionManager.getToken(),matchingProfile,maxDistance,minAge,
                maxAge,showMe,distanceType,newMatch,message,msgLikes,superLike,"");
        rv_questions.setLayoutManager(linearLayoutManager1);
        rv_questions.setAdapter(questionsAdapter);
        Log.e(TAG, "onSuccessGetSettings: "+settingsModel.getHobbies());


    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        Log.d(TAG, "Destroying helper.");
//        if (mHelper != null) {
//            mHelper.disposeWhenFinished();
//            mHelper = null;
//        }

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

