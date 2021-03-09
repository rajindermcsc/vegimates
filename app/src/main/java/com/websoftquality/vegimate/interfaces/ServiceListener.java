package com.websoftquality.vegimate.interfaces;

/**
 * @package com.websoftquality.vegimate
 * @subpackage interfaces
 * @category ServiceListener
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.websoftquality.vegimate.datamodels.main.JsonResponse;

/*****************************************************************
 ServiceListener
 ****************************************************************/
public interface ServiceListener {

    void onSuccess(JsonResponse jsonResp, String data);

    void onFailure(JsonResponse jsonResp, String data);
}
