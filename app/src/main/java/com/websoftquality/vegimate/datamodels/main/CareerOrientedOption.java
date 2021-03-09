
package com.websoftquality.vegimate.datamodels.main;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CareerOrientedOption {

    @SerializedName("question")
    @Expose
    private String question;


    @SerializedName("default")
    @Expose
    private String default_value;
    @SerializedName("answer")
    @Expose
    private List<String> answer = null;

    public String getDefault_value() {
        return default_value;
    }

    public void setDefault_value(String default_value) {
        this.default_value = default_value;
    }


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

}
