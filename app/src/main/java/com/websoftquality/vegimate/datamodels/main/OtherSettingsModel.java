package com.websoftquality.vegimate.datamodels.main;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtherSettingsModel {
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("answer")
    @Expose
    private List<String> answer = null;
    @SerializedName("default")
    @Expose
    private String _default;

    public String get_default() {
        return _default;
    }

    public void set_default(String _default) {
        this._default = _default;
    }

    public String getCurrent_value() {
        return current_value;
    }

    public void setCurrent_value(String current_value) {
        this.current_value = current_value;
    }

    @SerializedName("current_value")
    @Expose
    private String current_value;

    @SerializedName("name")
    @Expose
    private String name;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public String getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default = _default;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}