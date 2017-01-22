package ru.slayff.starenglish.QuizSystem;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by slayff on 12.04.15.
 */
public class QHolder {
    private DataBaseHelper myDBHelper;
    private int lesson;
    List<Question> QList;
    final String typeYN = "YNQ";
    final String typeCOR = "CORQ";
    final String typeMATCH = "MATCHQ";
    final String typePIC = "PICQ";


    /**
     *
     * @param lesson shows the exact lesson to be used in current object
     * @param helper needs to set DataBaseHelper (needs to get Cursor)
     */
    public QHolder(int lesson, DataBaseHelper helper) {
      //  this.myDataBase = database;
        this.lesson = lesson;
        this.myDBHelper = helper;
    }

    /**
     *
     * @param qType shows what table in database should be used
     * @return List of ids of exact lesson
     */
    public List getListOfId(String qType) {
        List<Integer>ListOfId = new ArrayList<Integer>();
        Cursor cur = myDBHelper.getCursor(lesson, qType);
        int idColumnIndex = cur.getColumnIndex("_id");
        if (cur != null) {
            if (cur.moveToFirst()) {
                int newid =  cur.getInt(idColumnIndex);
                ListOfId.add(newid);
                while (cur.moveToNext()) {
                    newid =  cur.getInt(idColumnIndex);
                    ListOfId.add(newid);
                }
            }
        }
        cur.close();
        return ListOfId;
    }

    /**
     *
     * @return List of random Questions in exact lesson
     */
    public List<Question> getQList() {
        List<Integer> idOfYN = getListOfId(typeYN);
        List<Integer> idOfCOR = getListOfId(typeCOR);
        List<Integer> idOfMATCH = getListOfId(typeMATCH);
        List<Integer> idofPIC = getListOfId(typePIC);
        QList = new ArrayList<Question>();
        Collections.shuffle(idOfYN);
        for (int i = 0; i < 3; i++) {
            int curId = idOfYN.get(i);
            Cursor cursor = myDBHelper.getCursorAtId(curId, typeYN);
            cursor.moveToFirst();
            QTypeYN question = new QTypeYN(cursor);
            QList.add(question);
            cursor.close();
        }
        Collections.shuffle(idOfCOR);
        for (int i = 0; i < 3; i++) {
            int curId = idOfCOR.get(i);
            Cursor cursor = myDBHelper.getCursorAtId(curId, typeCOR);
            cursor.moveToFirst();
            QTypeCOR question = new QTypeCOR(cursor);
            QList.add(question);
            cursor.close();
        }
        Collections.shuffle(idOfMATCH);
        for (int i = 0; i < 2; i++) {
            int curId = idOfMATCH.get(i);
            Cursor cursor = myDBHelper.getCursorAtId(curId, typeMATCH);
            cursor.moveToFirst();
            QTypeMATCH question = new QTypeMATCH(cursor);
            QList.add(question);
            cursor.close();
        }
        Collections.shuffle(idofPIC);
        for (int i = 0; i < 2; i++) {
            int curId = idofPIC.get(i);
            Cursor cursor = myDBHelper.getCursorAtId(curId, typePIC);
            cursor.moveToFirst();
            QTypePIC question = new QTypePIC(cursor);
            QList.add(question);
            cursor.close();
        }
        Collections.shuffle(QList);
        return QList;
    }
}
