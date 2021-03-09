package com.websoftquality.vegimate.datamodels.main;
/**
 * @package com.websoftquality.vegimate
 * @subpackage datamodels.main
 * @category SliderModel
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/*****************************************************************
 Slider Model
 ****************************************************************/
public class SliderModel {

    @SerializedName("status_message")
    @Expose
    private String message;
    @SerializedName("status_code")
    @Expose
    private String code;
    @SerializedName("minimum_age")
    @Expose
    private String minimumAge;
    @SerializedName("maximum_age")
    @Expose
    private String maximumAge;

    @SerializedName("divorced")
    @Expose
    private String divorced;
    @SerializedName("never_married")
    @Expose
    private String never_married;

    @SerializedName("race")
    @Expose
    private String race;

    @SerializedName("religious")
    @Expose
    private String religious;

    @SerializedName("kids")
    @Expose
    private String kids;
    @SerializedName("education_level")
    @Expose
    private String education_level;

    public String getDivorced() {
        return divorced;
    }

    public void setDivorced(String divorced) {
        this.divorced = divorced;
    }

    public String getNever_married() {
        return never_married;
    }

    public void setNever_married(String never_married) {
        this.never_married = never_married;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getReligious() {
        return religious;
    }

    public void setReligious(String religious) {
        this.religious = religious;
    }

    public String getKids() {
        return kids;
    }

    public void setKids(String kids) {
        this.kids = kids;
    }

    public String getEducation_level() {
        return education_level;
    }

    public void setEducation_level(String education_level) {
        this.education_level = education_level;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    @SerializedName("qualification")
    @Expose
    private String qualification;
    @SerializedName("login_sliders")
    @Expose
    private ArrayList<ImageListModel> imageList = new ArrayList<>();

    @SerializedName("app_site_data")
    @Expose
    private ArrayList<AppSiteModel> appDataList = new ArrayList<>();

    @SerializedName("igniter_plus_sliders")
    @Expose
    private ArrayList<ImageListModel> igniterPlusImgList = new ArrayList<>();
    @SerializedName("apple_client_id")
    @Expose
    private String clientId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(String minimumAge) {
        this.minimumAge = minimumAge;
    }

    public String getMaximumAge() {
        return maximumAge;
    }

    public void setMaximumAge(String maximumAge) {
        this.maximumAge = maximumAge;
    }

    public ArrayList<ImageListModel> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<ImageListModel> imageList) {
        this.imageList = imageList;
    }

    public ArrayList<AppSiteModel> getAppDataList() {
        return appDataList;
    }

    public void setAppDataList(ArrayList<AppSiteModel> appDataList) {
        this.appDataList = appDataList;
    }

    public ArrayList<ImageListModel> getIgniterPlusImgList() {
        return igniterPlusImgList;
    }

    public void setIgniterPlusImgList(ArrayList<ImageListModel> igniterPlusImgList) {
        this.igniterPlusImgList = igniterPlusImgList;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
