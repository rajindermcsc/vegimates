package com.websoftquality.vegimate.views.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.websoftquality.vegimate.R;
import com.websoftquality.vegimate.configs.AppController;
import com.websoftquality.vegimate.configs.SessionManager;
import com.websoftquality.vegimate.utils.Apierror_handle;
import com.websoftquality.vegimate.utils.Loading;
import com.websoftquality.vegimate.utils.MessageToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class StripeCardPaymentActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private static final String TAG = "StripeCardPaymentActivity";
    EditText edt_card,edt_expiry,edt_cvv,edt_name;
    JSONObject jsonObject;
    Loading loading;
    String apiurl;
    Apierror_handle apierror_handle;
    TextView tv_continue,tv_header_title,tv_left_arrow;
    Intent intent;
    String amount,plan_id,plan_type,plan_price,plan_name;
    @Inject
    SessionManager sessionManager;
    MessageToast messageToast;
    Handler handler;
    int previousLength;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_card_payment);
        handler=new Handler();
        loading=new Loading(this);
        apierror_handle=new Apierror_handle(this);
        messageToast=new MessageToast(this);
        AppController.getAppComponent().inject(this);
        intent=getIntent();
        amount=intent.getStringExtra("amount");
        plan_id=intent.getStringExtra("plan_id");
        plan_type=intent.getStringExtra("plan_type");
        plan_price=intent.getStringExtra("plan_price");
        plan_name=intent.getStringExtra("plan_name");
        tv_continue=findViewById(R.id.tv_continue);
        tv_header_title=findViewById(R.id.tv_header_title);
        tv_left_arrow=findViewById(R.id.tv_left_arrow);
        tv_left_arrow.setOnClickListener(this);
        tv_header_title.setText("Payment");
        edt_card=findViewById(R.id.edt_card);
        edt_expiry=findViewById(R.id.edt_expiry);
        edt_cvv=findViewById(R.id.edt_cvv);
        edt_name=findViewById(R.id.edt_name);
        tv_continue.setOnClickListener(this);
        edt_expiry.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tv_continue){
            validations();
        }
        else if (v.getId()==R.id.tv_left_arrow){
            onBackPressed();
        }
    }

    private void validations() {
        if (edt_card.getText().toString().length()<16 ){
            Toast.makeText(this, "Card Number is invalid", Toast.LENGTH_SHORT).show();
        }
        else if (edt_card.getText().toString().trim().isEmpty()){

            Toast.makeText(this, "Please Enter Card Number", Toast.LENGTH_SHORT).show();
        }else if (edt_expiry.getText().toString().trim().isEmpty()){

            Toast.makeText(this, "Please Enter Expiry Date", Toast.LENGTH_SHORT).show();
        }else if (edt_expiry.getText().toString().length()<5){

            Toast.makeText(this, "Expiry Date is invalid", Toast.LENGTH_SHORT).show();
        } else if (edt_cvv.getText().toString().trim().isEmpty()){

            Toast.makeText(this, "Please Enter CVV", Toast.LENGTH_SHORT).show();
        }else if (edt_expiry.getText().toString().length()<3){

            Toast.makeText(this, "CVV is invalid", Toast.LENGTH_SHORT).show();
        }
        else {

            savecardDetails(getResources().getString(R.string.base_url).concat("payment"));
        }
    }

    private void savecardDetails(String apiurl) {
        loading.showDialog();
        Log.e("TAG", "savecardDetails: "+sessionManager.getToken());
        String tag_string_req = "req_login";
        Log.e("TAG", "attendance_webservice: "+apiurl);
        StringRequest strReq = new StringRequest(Request.Method.POST, apiurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try{
                    loading.hideDialog();
                    final JSONObject jsonObject = new JSONObject(response);
                    Log.e("TAG", "onResponse: "+jsonObject);
                    if (jsonObject.getString("status").equals("200")) {
                        messageToast.showDialog("Payment Successfully");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent=new Intent(StripeCardPaymentActivity.this,HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },2900);
                    }
                    else
                    {
                        loading.hideDialog();

                    }
                }
                catch(Exception e)
                {
                    Log.e("TAG", "onResponse: "+e.getMessage());
                    loading.hideDialog();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("TAG", "onResponse: "+error.getMessage());
                loading.hideDialog();

                try
                {
                    apierror_handle.get_error(error);
                }catch (Exception e)
                {
                    Log.e("TAG", "onErrorResponse: " + e.getMessage());
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                String name[]=edt_expiry.getText().toString().split("/");

                Map<String, String> params = new HashMap<String, String>();
                Log.e(TAG, "getParams: "+plan_id);
                Log.e(TAG, "getParams: "+plan_price);
                String price[]=plan_price.split(":");
                String price_=price[1].substring(2);
                Log.e(TAG, "getParams: "+price_.length());
                Log.e(TAG, "getParams: "+price_);
                Log.e(TAG, "getParams: "+plan_type);
                Log.e(TAG, "getParams: "+plan_name);
                params.put("plan_id", plan_id);
                params.put("plan_price", price_);
                params.put("plan_type", plan_type);
                params.put("plan_month", plan_name);
                params.put("cvv", edt_cvv.getText().toString());
                params.put("card_number", edt_card.getText().toString());
                params.put("exp_month", name[0]);
                params.put("exp_year", name[1]);
                params.put("name_on_card", edt_name.getText().toString());
                params.put("token", sessionManager.getToken());
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int length = edt_expiry.getText().toString().trim().length();

        if (previousLength <= length && length < 3) {
            int month = Integer.parseInt(edt_expiry.getText().toString());
            if (length == 1 && month >= 2) {
                String autoFixStr = "0" + month + "/";
                edt_expiry.setText(autoFixStr);
                edt_expiry.setSelection(3);
            } else if (length == 2 && month <= 12) {
                String autoFixStr = edt_expiry.getText().toString() + "/";
                edt_expiry.setText(autoFixStr);
                edt_expiry.setSelection(3);
            } else if (length ==2 && month > 12) {
                edt_expiry.setText("1");
                edt_expiry.setSelection(1);
            }
        } else if (length == 5) {
            edt_expiry.requestFocus(); // auto move to next edittext
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}