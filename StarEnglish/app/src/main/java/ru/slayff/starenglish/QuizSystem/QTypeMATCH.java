package ru.slayff.starenglish.QuizSystem;

import android.database.Cursor;

/**
 * Created by slayff on 11.04.15.
 */
public class QTypeMATCH extends Question {
    private String type;
    private String words_en;
    private String words_ru;
    private String ans_1;
    private String ans_2;
    private String ans_3;

    public QTypeMATCH(Cursor cur) {
        type = "MATCHQ";
        int idOfwords_en = cur.getColumnIndex("words_en");
        int idOfwords_ru = cur.getColumnIndex("words_ru");
        int idOfans_1 = cur.getColumnIndex("ans_1");
        int idOfans_2 = cur.getColumnIndex("ans_2");
        int idOfans_3 = cur.getColumnIndex("ans_3");

        words_en = cur.getString(idOfwords_en);
        words_ru = cur.getString(idOfwords_ru);
        ans_1 = cur.getString(idOfans_1);
        ans_2 = cur.getString(idOfans_2);
        ans_3 = cur.getString(idOfans_3);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    public void setWords_ru(String words_ru) {
        this.words_ru = words_ru;
    }

    public void setWords_en(String words_en) {
        this.words_en = words_en;
    }

    public String getWords_ru() {
        return words_ru;
    }

    public String getWords_en() {
        return words_en;
    }

    public void setAnswers(String ans1, String ans2, String ans3){
        this.ans_1=ans1;
        this.ans_2=ans2;
        this.ans_3=ans3;
    }

    public String getAns_1() {
        return ans_1;
    }

    public String getAns_2() {
        return ans_2;
    }

    public String getAns_3() {
        return ans_3;
    }
    public boolean isYourAnswersCorrect(String yourans1, String yourans2, String yourans3){
        if ((this.ans_1.equals(yourans1)) && (this.ans_2.equals(yourans2)) && (this.ans_3.equals(yourans3)))
            return true;
        return false;
    }
}
