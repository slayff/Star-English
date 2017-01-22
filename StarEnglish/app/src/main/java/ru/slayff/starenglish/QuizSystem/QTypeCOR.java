package ru.slayff.starenglish.QuizSystem;

import android.database.Cursor;

/**
 * Created by slayff on 11.04.15.
 */
public class QTypeCOR extends Question {

    private String type;
    private String base;
    private String answer;
    private String hint;

    public QTypeCOR(Cursor cur) {
        type = "CORQ";
        int idOfbase = cur.getColumnIndex("base");
        int idOfanswer = cur.getColumnIndex("answer");
        int idOfhint = cur.getColumnIndex("hint");
        base = cur.getString(idOfbase);
        answer = cur.getString(idOfanswer);
        hint = cur.getString(idOfhint);
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

    public String getAnswer() {
        return answer;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getBase() {
        return base;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getHint() {
        return hint;
    }

    public boolean isYourAnswersCorrect(String youranswer) {
        if (youranswer.equals(this.answer)) return true;
        return false;
    }
}
