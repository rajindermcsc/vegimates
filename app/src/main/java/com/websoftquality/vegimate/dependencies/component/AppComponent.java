package com.websoftquality.vegimate.dependencies.component;
/**
 * @package com.websoftquality.vegimate
 * @subpackage dependencies.component
 * @category AppComponent
 * @author Trioangle Product Team
 * @version 1.0
 **/

import javax.inject.Singleton;

import dagger.Component;
import com.websoftquality.vegimate.adapters.chat.ChatConversationListAdapter;
import com.websoftquality.vegimate.adapters.chat.MessageUserListAdapter;
import com.websoftquality.vegimate.adapters.chat.NewMatchesListAdapter;
import com.websoftquality.vegimate.adapters.chat.UserListAdapter;
import com.websoftquality.vegimate.adapters.main.ProfileSliderAdapter;
import com.websoftquality.vegimate.adapters.main.QuestionsAdapter;
import com.websoftquality.vegimate.adapters.matches.MatchesSwipeAdapter;
import com.websoftquality.vegimate.adapters.chat.UnmatchReasonListAdapter;
import com.websoftquality.vegimate.adapters.profile.EditProfileImageListAdapter;
import com.websoftquality.vegimate.adapters.profile.EnlargeSliderAdapter;
import com.websoftquality.vegimate.adapters.profile.IgniterSliderAdapter;
import com.websoftquality.vegimate.adapters.profile.LocationListAdapter;
import com.websoftquality.vegimate.backgroundtask.ImageCompressAsyncTask;
import com.websoftquality.vegimate.configs.RunTimePermission;
import com.websoftquality.vegimate.configs.SessionManager;
import com.websoftquality.vegimate.dependencies.module.AppContainerModule;
import com.websoftquality.vegimate.dependencies.module.ApplicationModule;
import com.websoftquality.vegimate.dependencies.module.NetworkModule;
import com.websoftquality.vegimate.layoutmanager.SwipeableTouchHelperCallback;
import com.websoftquality.vegimate.likedusers.LikedUserAdapter;
import com.websoftquality.vegimate.likedusers.LikedUsersActivity;
import com.websoftquality.vegimate.pushnotification.MyFirebaseInstanceIDService;
import com.websoftquality.vegimate.pushnotification.MyFirebaseMessagingService;
import com.websoftquality.vegimate.pushnotification.NotificationUtils;
import com.websoftquality.vegimate.swipedeck.Utility.SwipeListener;
import com.websoftquality.vegimate.utils.CommonMethods;
import com.websoftquality.vegimate.utils.DateTimeUtility;
import com.websoftquality.vegimate.utils.ImageUtils;
import com.websoftquality.vegimate.utils.RequestCallback;
import com.websoftquality.vegimate.utils.WebServiceUtils;
import com.websoftquality.vegimate.views.chat.ChatConversationActivity;
import com.websoftquality.vegimate.views.chat.ChatFragment;
import com.websoftquality.vegimate.views.chat.CreateGroupActivity;
import com.websoftquality.vegimate.views.chat.MatchUsersActivity;
import com.websoftquality.vegimate.views.main.BoostDialogActivity;
import com.websoftquality.vegimate.views.main.HomeActivity;
import com.websoftquality.vegimate.views.main.IgniterGoldActivity;
import com.websoftquality.vegimate.views.main.IgniterPageFragment;
import com.websoftquality.vegimate.views.main.IgniterPlusDialogActivity;
import com.websoftquality.vegimate.views.main.IgniterPlusSliderFragment;
import com.websoftquality.vegimate.views.main.LoginActivity;
import com.websoftquality.vegimate.views.main.SplashActivity;
import com.websoftquality.vegimate.views.main.StripeCardPaymentActivity;
import com.websoftquality.vegimate.views.main.TutorialFragment;
import com.websoftquality.vegimate.views.main.UserNameActivity;
import com.websoftquality.vegimate.views.main.VerificationActivity;
import com.websoftquality.vegimate.views.main.AccountKit.FacebookAccountKitActivity;
import com.websoftquality.vegimate.views.main.AccountKit.TwilioAccountKitActivity;
import com.websoftquality.vegimate.views.profile.AddLocationActivity;
import com.websoftquality.vegimate.views.profile.AdvancedSettings;
import com.websoftquality.vegimate.views.profile.EditProfileActivity;
import com.websoftquality.vegimate.views.profile.EnlargeProfileActivity;
import com.websoftquality.vegimate.views.profile.GetIgniterPlusActivity;
import com.websoftquality.vegimate.views.profile.ProfileFragment;
import com.websoftquality.vegimate.views.profile.SettingsActivity;
import com.websoftquality.vegimate.views.signup.BirthdayFragment;
import com.websoftquality.vegimate.views.signup.EmailFragment;
import com.websoftquality.vegimate.views.signup.GenderFragment;
import com.websoftquality.vegimate.views.signup.OneTimePwdFragment;
import com.websoftquality.vegimate.views.signup.PasswordFragment;
import com.websoftquality.vegimate.views.signup.PhoneNumberFragment;
import com.websoftquality.vegimate.views.signup.ProfilePickFragment;
import com.websoftquality.vegimate.views.signup.SignUpActivity;
import com.websoftquality.vegimate.views.signup.VeganFragment;

/*****************************************************************
 App Component
 ****************************************************************/
@Singleton
@Component(modules = {NetworkModule.class, ApplicationModule.class, AppContainerModule.class})
public interface AppComponent {
    // ACTIVITY

    void inject(SplashActivity splashActivity);

    void inject(HomeActivity homeActivity);

    void inject(SettingsActivity settingsActivity);

    void inject(EditProfileActivity editProfileActivity);

    void inject(GetIgniterPlusActivity getIgniterPlusActivity);

    void inject(SignUpActivity signUpActivity);

    void inject(EnlargeProfileActivity enlargeProfileActivity);

    void inject(MatchUsersActivity matchUsersActivity);

    void inject(ChatConversationActivity chatConversationActivity);

    void inject(CreateGroupActivity createGroupActivity);

    void inject(LoginActivity loginActivity);

    void inject(VerificationActivity verificationActivity);

    void inject(UserNameActivity userNameActivity);

    void inject(AddLocationActivity addLocationActivity);

    void inject(FacebookAccountKitActivity facebookAccountKitActivity);

    void inject(TwilioAccountKitActivity facebookAccountKitActivity1);

    void inject(IgniterGoldActivity igniterGoldActivity);

    void inject(LikedUsersActivity likedUsersActivity);


    // Fragments
    void inject(ProfileFragment profileFragment);

    void inject(IgniterPageFragment igniterPageFragment);

    void inject(ChatFragment chatFragment);

    void inject(ProfilePickFragment profilePickFragment);

    void inject(EmailFragment emailFragment);

    void inject(PasswordFragment passwordFragment);

    void inject(BirthdayFragment birthdayFragment);

    void inject(TutorialFragment tutorialFragment);

    void inject(PhoneNumberFragment phoneNumberFragment);

    void inject(OneTimePwdFragment oneTimePwdFragment);

    void inject(GenderFragment genderFragment);

    void inject(IgniterPlusDialogActivity igniterPlusDialogActivity);

    void inject(BoostDialogActivity boostDialogActivity);

    void inject(IgniterPlusSliderFragment igniterPlusSliderFragment);

    // Utilities
    void inject(RunTimePermission runTimePermission);

    void inject(SessionManager sessionManager);

    void inject(ImageUtils imageUtils);

    void inject(CommonMethods commonMethods);

    void inject(ProfileSliderAdapter profileSliderAdapter);

    void inject(RequestCallback requestCallback);

    void inject(DateTimeUtility dateTimeUtility);

    void inject(WebServiceUtils webServiceUtils);

    // Adapters
    void inject(IgniterSliderAdapter igniterSliderAdapter);

    void inject(NewMatchesListAdapter newMatchesListAdapter);

    void inject(MessageUserListAdapter messageUserListAdapter);

    void inject(EnlargeSliderAdapter enlargeSliderAdapter);

    void inject(EditProfileImageListAdapter editProfileImageListAdapter);

    void inject(ChatConversationListAdapter chatConversationListAdapter);

    void inject(UnmatchReasonListAdapter unmatchReasonListAdapter);

    void inject(UserListAdapter chatUserListAdapter);

    void inject(MatchesSwipeAdapter matchesSwipeAdapter);

    void inject(SwipeListener swipeListener);

    void inject(LocationListAdapter locationListAdapter);

    void inject(LikedUserAdapter likedUserAdapter);

    void inject(MyFirebaseMessagingService myFirebaseMessagingService);

    void inject(MyFirebaseInstanceIDService myFirebaseInstanceIDService);


    // AsyncTask
    void inject(ImageCompressAsyncTask imageCompressAsyncTask);

    void inject(NotificationUtils notificationUtils);

    void inject(SwipeableTouchHelperCallback swipeableTouchHelperCallback);


    void inject(StripeCardPaymentActivity stripeCardPaymentActivity);

    void inject(QuestionsAdapter questionsAdapter);

    void inject(AdvancedSettings advancedSettings);

    void inject(VeganFragment veganFragment);
}
