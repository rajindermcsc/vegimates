package com.websoftquality.vegimate.dependencies.module;
/**
 * @package com.websoftquality.vegimate
 * @subpackage dependencies.module
 * @category AppContainerModule
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import com.websoftquality.vegimate.configs.Constants;
import com.websoftquality.vegimate.configs.RunTimePermission;
import com.websoftquality.vegimate.configs.SessionManager;
import com.websoftquality.vegimate.datamodels.main.JsonResponse;
import com.websoftquality.vegimate.utils.CalendarDatePickerDialog;
import com.websoftquality.vegimate.utils.CalendarTimePickerDialog;
import com.websoftquality.vegimate.utils.CommonMethods;
import com.websoftquality.vegimate.utils.DateTimeUtility;
import com.websoftquality.vegimate.utils.ImageUtils;
import com.websoftquality.vegimate.utils.Validator;
import com.websoftquality.vegimate.views.customize.CustomDialog;

/*****************************************************************
 App Container Module
 ****************************************************************/
@Module(includes = com.websoftquality.vegimate.dependencies.module.ApplicationModule.class)
public class AppContainerModule {
    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return application.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    CommonMethods providesCommonMethods() {
        return new CommonMethods();
    }

    @Provides
    @Singleton
    SessionManager providesSessionManager() {
        return new SessionManager();
    }


    @Provides
    @Singleton
    Validator providesValidator() {
        return new Validator();
    }

    @Provides
    @Singleton
    RunTimePermission providesRunTimePermission() {
        return new RunTimePermission();
    }

    @Provides
    @Singleton
    Context providesContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    ArrayList<String> providesStringArrayList() {
        return new ArrayList<>();
    }

    @Provides
    @Singleton
    JsonResponse providesJsonResponse() {
        return new JsonResponse();
    }

    @Provides
    @Singleton
    DateTimeUtility providesDateTimeUtility() {
        return new DateTimeUtility();
    }

    @Provides
    @Singleton
    ImageUtils providesImageUtils() {
        return new ImageUtils();
    }

    @Provides
    @Singleton
    CalendarDatePickerDialog providesCalendarDatePickerDialog() {
        return new CalendarDatePickerDialog();
    }

    @Provides
    @Singleton
    CalendarTimePickerDialog providesCalendarTimePickerDialog() {
        return new CalendarTimePickerDialog();
    }

    @Provides
    @Singleton
    CustomDialog providesCustomDialog() {
        return new CustomDialog();
    }
}
