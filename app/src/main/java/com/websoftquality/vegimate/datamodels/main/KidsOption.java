
package com.websoftquality.vegimate.datamodels.main;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KidsOption {

    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("answer")
    @Expose
    private List<String> answer = null;

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
