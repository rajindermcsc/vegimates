package com.websoftquality.vegimate.interfaces;
/**
 * @package com.websoftquality.vegimate
 * @subpackage interfaces
 * @category SignUpActivityListener
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.res.Resources;

import com.websoftquality.vegimate.views.signup.SignUpActivity;

/*****************************************************************
 SignUpActivityListener
 ****************************************************************/


public interface SignUpActivityListener {

    Resources getRes();

    SignUpActivity getInstance();

}
