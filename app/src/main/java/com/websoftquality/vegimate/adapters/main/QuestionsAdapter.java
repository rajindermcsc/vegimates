package com.websoftquality.vegimate.adapters.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.websoftquality.vegimate.R;
import com.websoftquality.vegimate.configs.AppController;
import com.websoftquality.vegimate.datamodels.main.JsonResponse;
import com.websoftquality.vegimate.datamodels.main.SettingsModel;
import com.websoftquality.vegimate.interfaces.ApiService;
import com.websoftquality.vegimate.interfaces.ServiceListener;
import com.websoftquality.vegimate.utils.CustomSpinnerAdapter;
import com.websoftquality.vegimate.utils.RequestCallback;
import com.websoftquality.vegimate.views.profile.SettingsActivity;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import static com.websoftquality.vegimate.utils.Enums.REQ_UPDATE_SETTINGS;

public class QuestionsAdapter extends RecyclerView.Adapter implements ServiceListener {
    private static final String TAG = "QuestionsAdapter";
    Context context;
    SettingsModel settingsModel;
    int pos;
    @Inject
    ApiService apiService;
    int number=0;
//    @Inject
//    CommonMethods commonMethods;
//    @Inject
//    CustomDialog customDialog;
//    @Inject
//    SessionManager sessionManager;
    String token, matchingProfile, maxDistance,minAge, maxAge, showMe, distanceType, newMatch, message,msgLikes,superLike,hobbies;
    public QuestionsAdapter(Context context, SettingsModel settingsModel, String token, String matchingProfile, String maxDistance,
                            String minAge, String maxAge, String showMe, String distanceType, String newMatch, String message,
                            String msgLikes, String superLike, String hobbies) {
        this.context=context;
        this.settingsModel=settingsModel;
        this.token=token;
        this.matchingProfile=matchingProfile;
        this.maxDistance=maxDistance;
        this.minAge=minAge;
        this.maxAge=maxAge;
        this.showMe=showMe;
        this.distanceType=distanceType;
        this.newMatch=newMatch;
        this.message=message;
        this.msgLikes=msgLikes;
        this.superLike=superLike;
        this.hobbies=hobbies;
        Log.e(TAG, "QuestionsAdapter: "+showMe);
        AppController.getAppComponent().inject(this);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.questions_adapter_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder)holder).tv_questions.setText(settingsModel.getOther_settings().get(position).getQuestion());
        ((MyViewHolder)holder).customSpinnerAdapter=new CustomSpinnerAdapter(context, (ArrayList<String>) settingsModel.getOther_settings().get(position).getAnswer());
        ((MyViewHolder)holder).spinner_questions.setAdapter(((MyViewHolder)holder).customSpinnerAdapter);

        for (int i=0;i<settingsModel.getOther_settings().get(position).getAnswer().size();i++){

            if (settingsModel.getOther_settings().get(position).getCurrent_value() !=null){
                Log.e(TAG, "onBindViewHolder@: "+settingsModel.getOther_settings().get(position).getAnswer().get(i));
                Log.e(TAG, "onBindViewHolder@@: "+settingsModel.getOther_settings().get(position).getCurrent_value());
                if (settingsModel.getOther_settings().get(position).getAnswer().get(i).equalsIgnoreCase(
                        settingsModel.getOther_settings().get(position).getCurrent_value())){
                    pos=i;
                }
            }
            else {

                if (settingsModel.getOther_settings().get(position).getAnswer().get(i).equalsIgnoreCase(
                        settingsModel.getOther_settings().get(position).getDefault())){
                    pos=i;
                }

            }

            Log.e(TAG, "onBindViewHolder@@@: "+pos);
        }


        ((MyViewHolder)holder).spinner_questions.setSelection(pos);

        ((MyViewHolder)holder).spinner_questions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                Log.e(TAG, "onItemSelected: "+settingsModel.getOther_settings().get(position).getAnswer().get(i));
                getSettingsDetails(settingsModel.getOther_settings().get(position).getName(),
                        settingsModel.getOther_settings().get(position).getAnswer().get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void getSettingsDetails(String name, String s) {
//        commonMethods.showProgressDialog((AppCompatActivity) context, customDialog);
        apiService.updateSettings(getParams(name,s)).enqueue(new RequestCallback(REQ_UPDATE_SETTINGS, this));
    }

    private HashMap<String, String> getParams(String name, String s) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", token);
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
        hashMap.put("hobbies", hobbies);
        hashMap.put(name, s);
        /*hashMap.put("latitude", "100");
        hashMap.put("longitude", "97");*/

        return hashMap;
    }

    @Override
    public int getItemCount() {
        return settingsModel.getOther_settings().size();
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
//        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
//            commonMethods.showMessage(context, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case REQ_UPDATE_SETTINGS:
                if (jsonResp.isSuccess()) {

                }
                }
//        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
//                    commonMethods.showMessage(context, dialog, jsonResp.getStatusMsg());
//                }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_questions;
        Spinner spinner_questions;
        CustomSpinnerAdapter customSpinnerAdapter;
        public MyViewHolder(View v) {
            super(v);
            tv_questions=v.findViewById(R.id.tv_questions);
            spinner_questions=v.findViewById(R.id.spinner_questions);
        }
    }
}
