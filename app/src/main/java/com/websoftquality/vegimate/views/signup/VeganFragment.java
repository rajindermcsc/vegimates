package com.websoftquality.vegimate.views.signup;
/**
 * @package com.websoftquality.vegimate
 * @subpackage view.signup
 * @category FirstNameFragment
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.obs.CustomButton;
import com.obs.CustomEditText;
import com.obs.CustomTextView;
import com.websoftquality.vegimate.R;
import com.websoftquality.vegimate.configs.AppController;
import com.websoftquality.vegimate.configs.Constants;
import com.websoftquality.vegimate.configs.SessionManager;
import com.websoftquality.vegimate.datamodels.main.JsonResponse;
import com.websoftquality.vegimate.datamodels.main.SignUpModel;
import com.websoftquality.vegimate.interfaces.ApiService;
import com.websoftquality.vegimate.interfaces.SignUpActivityListener;
import com.websoftquality.vegimate.utils.CommonMethods;
import com.websoftquality.vegimate.utils.Enums;
import com.websoftquality.vegimate.utils.RequestCallback;
import com.websoftquality.vegimate.views.customize.CustomDialog;
import com.websoftquality.vegimate.views.main.HomeActivity;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/*****************************************************************
 Signup user first name page
 ****************************************************************/

public class VeganFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    @Inject
    SessionManager sessionManager;
    @Inject
    ApiService apiService;
    @Inject
    Gson gson;
    @Inject
    CustomDialog customDialog;

    @Inject
    CommonMethods commonMethods;

    private AlertDialog dialog;

    private View view;
    private SignUpActivityListener listener;
    private Resources res;
    private SignUpActivity mActivity;

    private CustomTextView tvBackArrow;
    private CustomButton btnContinue;
    private RadioGroup rdg_vegan;
    private String vegan;

    HashMap<String, String> hashMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) parent.removeView(view);
        } else {
            view = inflater.inflate(R.layout.vegan_fragment, container, false);
            initView();
        }
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            hashMap = (HashMap<String, String>) bundle.getSerializable("map");
            Log.e("TAG", "onActivityCreated: "+hashMap.get("auth_id"));
            if (hashMap != null)
                Log.v("HashMapTest", hashMap.get("auth_id"));
        }
    }

    private void initView() {
        dialog = commonMethods.getAlertDialog(mActivity);
        tvBackArrow = (CustomTextView) view.findViewById(R.id.tv_left_arrow);
        btnContinue = (CustomButton) view.findViewById(R.id.btn_continue);
        rdg_vegan = (RadioGroup) view.findViewById(R.id.rdg_vegan);

        tvBackArrow.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        rdg_vegan.setOnCheckedChangeListener(this);

        //gender = res.getString(R.string.men);
        btnContinue.setEnabled(false);

    }

    private void init() {
        AppController.getAppComponent().inject(this);
        if (listener == null) return;
        res = (listener.getRes() != null) ? listener.getRes() : getActivity().getResources();
        mActivity = (listener.getInstance() != null) ? listener.getInstance() : (SignUpActivity) getActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left_arrow:
                mActivity.onBackPressed();
                break;
            case R.id.btn_continue:

                mActivity.putHashMap("vegan", vegan);
                mActivity.changeFragment(Enums.FIRST_NAME, null, false);

                break;
            default:
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (SignUpActivityListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + Constants.listenerSignUpException);
        }
    }

    @Override
    public void onDetach() {
        if (listener != null) listener = null;
        super.onDetach();
    }

    /**
     * onCreateAnimation is used to perform the animation while sliding or
     * automatic Slideshow in the image gallery.
     */
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (Constants.isDisableFragmentAnimations) {
            Animation a = new Animation() {
            };
            a.setDuration(0);
            return a;
        }

        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_vegan:
                vegan = res.getString(R.string.vegan);
                btnContinue.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
                btnContinue.setBackgroundResource(R.drawable.oval_gradient_btn);
                btnContinue.setEnabled(true);
                break;
            case R.id.rb_vegetarian:
                vegan = res.getString(R.string.vegetarian);
                btnContinue.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
                btnContinue.setBackgroundResource(R.drawable.oval_gradient_btn);
                btnContinue.setEnabled(true);
                break;
            case R.id.rb_transitioning:
                vegan = res.getString(R.string.transitioning);
                btnContinue.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
                btnContinue.setBackgroundResource(R.drawable.oval_gradient_btn);
                btnContinue.setEnabled(true);
                break;
            default:
                break;
        }
    }

}