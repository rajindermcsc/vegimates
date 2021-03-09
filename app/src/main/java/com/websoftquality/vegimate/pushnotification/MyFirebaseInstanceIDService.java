package com.websoftquality.vegimate.pushnotification;
/**
 * @package com.websoftquality.vegimate
 * @subpackage MyFirebaseInstanceIDService
 * @category Firebase instance service
 * @author Trioangle Product Team
 * @version 1.5
 */

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;

import javax.inject.Inject;

import com.websoftquality.vegimate.configs.AppController;
import com.websoftquality.vegimate.configs.RunTimePermission;
import com.websoftquality.vegimate.configs.SessionManager;
import com.websoftquality.vegimate.datamodels.main.JsonResponse;
import com.websoftquality.vegimate.interfaces.ApiService;
import com.websoftquality.vegimate.interfaces.ServiceListener;
import com.websoftquality.vegimate.utils.CommonMethods;
import com.websoftquality.vegimate.utils.RequestCallback;
import com.websoftquality.vegimate.views.customize.CustomDialog;

import static com.websoftquality.vegimate.utils.Enums.REQ_UPDATE_DEVICE_ID;

/*************************************************************
 Firebase instance service to get device ID
 *************************************************************** */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements ServiceListener {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
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
    @Inject
    RunTimePermission runTimePermission;

    //private AlertDialog dialog;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        AppController.getAppComponent().inject(this);
        //dialog = commonMethods.getAlertDialog(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);


        /*// Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);*/
    }

    private void sendRegistrationToServer(final String token) {
        // sending FCM token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);

        sessionManager.setDeviceId(token);

        if (!sessionManager.getToken().equals("")) {
            apiService.updateDeviceId(sessionManager.getToken(), "2", sessionManager.getDeviceId()).enqueue(new RequestCallback(REQ_UPDATE_DEVICE_ID, this));
        }
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        //commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            // commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case REQ_UPDATE_DEVICE_ID:
                if (jsonResp.isSuccess()) {
                    // onSuccessGetMyHome(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    // commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        //commonMethods.hideProgressDialog();
        //if (!jsonResp.isOnline()) commonMethods.showMessage(this, dialog, data);
    }
}


