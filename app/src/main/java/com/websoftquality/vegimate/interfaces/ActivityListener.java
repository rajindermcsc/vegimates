package com.websoftquality.vegimate.interfaces;
/**
 * @package com.websoftquality.vegimate
 * @subpackage interfaces
 * @category ActivityListener
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.res.Resources;

import com.websoftquality.vegimate.views.main.HomeActivity;

/*****************************************************************
 ActivityListener
 ****************************************************************/

public interface ActivityListener {

    Resources getRes();

    HomeActivity getInstance();

}
