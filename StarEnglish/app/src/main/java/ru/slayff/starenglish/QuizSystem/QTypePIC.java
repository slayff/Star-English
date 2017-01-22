package ru.slayff.starenglish.QuizSystem;

import android.database.Cursor;

/**
 * Created by slayff on 11.04.15.
 */
public class QTypePIC extends Question {
    private String type;
    private String path_pic1;
    private String path_pic2;
    private String path_pic3;
    private String pic_title;
    private String words_ru;
    private String ans_1;
    private String ans_2;
    private String ans_3;

    public QTypePIC (Cursor cur) {
        type = "PICQ";
        int idOfpath_pic1 = cur.getColumnIndex("path_pic1");
        int idOfpath_pic2 = cur.getColumnIndex("path_pic2");
        int idOfpath_pic3 = cur.getColumnIndex("path_pic3");
        int idOfpic_title = cur.getColumnIndex("pic_title");
        int idOfwords_ru = cur.getColumnIndex("words_ru");
        int idOfans_1 = cur.getColumnIndex("ans_1");
        int idOfans_2 = cur.getColumnIndex("ans_2");
        int idOfans_3 = cur.getColumnIndex("ans_3");

        path_pic1 = cur.getString(idOfpath_pic1);
        path_pic2 = cur.getString(idOfpath_pic2);
        path_pic3 = cur.getString(idOfpath_pic3);
        pic_title = cur.getString(idOfpic_title);
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

    public void setPath_pic1(String path_pic1) {
        this.path_pic1 = path_pic1;
    }

    public void setPath_pic2(String path_pic2) {
        this.path_pic2 = path_pic2;
    }

    public void setPath_pic3(String path_pic3) {
        this.path_pic3 = path_pic3;
    }

    public String getPath_pic1() {
        return path_pic1;
    }

    public String getPath_pic2() {
        return path_pic2;
    }

    public String getPath_pic3() {
        return path_pic3;
    }

    public void setWords_ru(String words_ru) {
        this.words_ru = words_ru;
    }

    public String getWords_ru() {
        return words_ru;
    }

    public void setPic_title(String pic_title) {
        this.pic_title = pic_title;
    }

    public String getPic_title() {
        return pic_title;
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
