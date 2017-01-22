package ru.slayff.starenglish.QuizSystem;

import android.database.Cursor;

/**
 * Created by slayff on 11.04.15.
 */
public class QTypeYN extends Question {
    private String type;
    private String eng_word;
    private String rus_word;
    private String answer;

    public QTypeYN(Cursor cur) {
        int idOfeng_word = cur.getColumnIndex("eng_word");
        int idOfrus_word = cur.getColumnIndex("rus_word");
        int idOfanswer = cur.getColumnIndex("answer");
        type = "YNQ";
        eng_word = cur.getString(idOfeng_word);
        rus_word = cur.getString(idOfrus_word);
        answer = cur.getString(idOfanswer);
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setEng_word(String eng_word) {
        this.eng_word = eng_word;
    }

    public void setRus_word(String rus_word) {
        this.rus_word = rus_word;
    }

    public String getAnswer() {
        return answer;
    }

    public String getEng_word() {
        return eng_word;
    }

    public String getRus_word() {
        return rus_word;
    }

    public boolean isYourAnswersCorrect(String youranswer) {
        if (youranswer.equals(this.answer))
            return true;
        return false;
    }
}
