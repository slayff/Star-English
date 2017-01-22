package ru.slayff.starenglish.QuizSystem;

/**
 * Created by slayff on 09.04.15.
 */
public abstract class Question {

    public abstract void setType(String type);

    public abstract String getType();

    /**
     * All these methods are overriden in subclasses
     * @return null or false
     */
    public String getAnswer() {
        return null;
    }
    public String getAns_1() {
        return null;
    }
    public String getAns_2() {
        return null;
    }
    public String getAns_3() {
        return null;
    }
    public String getEng_word()
    {
        return null;
    }
    public String getRus_word() {
        return null;
    }
    public boolean isYourAnswersCorrect(String youranswer) {
       return false;
    }
    public boolean isYourAnswersCorrect(String ans1, String ans2, String ans3) {
        return false;
    }
    public String getBase() {
        return null;
    }
    public String getHint() {
        return null;
    }
    public String getPath_pic1() {
        return null;
    }
    public String getPath_pic2() {
        return null;
    }
    public String getPath_pic3() {
        return null;
    }
    public String getWords_ru() {
        return null;
    }
    public String getPic_title() {
        return null;
    }
    public String getWords_en() {
        return null;
    }

}
