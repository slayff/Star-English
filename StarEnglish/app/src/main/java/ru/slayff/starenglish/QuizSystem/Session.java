package ru.slayff.starenglish.QuizSystem;



import java.util.ArrayList;

/**
 * Created by slayff on 09.04.15.
 */
public class Session  {
    private int correctanswers;
    private int wronganswers;
    private int totalanswers;
    private float result;
    private ArrayList<String> information;

    public int getCorrectanswers() {
        return correctanswers;
    }

    public int getWronganswers() {
        return wronganswers;
    }

    public int getTotalanswers() {
        return totalanswers;
    }

    public float getResult() {
        return result;
    }

    public ArrayList<String> getInformation() {
        return information;
    }

    public Session() {
        this.correctanswers=0;
        this.wronganswers=0;
        this.totalanswers=0;
        this.result=0;
        information = new ArrayList<String>();
    }

    /**
     *
     * @return string with one word showing general result
     */
    public String getShortResult() {
        if (result >= 0.8) return "Отлично!";
        if (result >= 0.6) return "Хорошо!";
        if (result >= 0.4) return "Могло быть и лучше!";
        return "Очень плохо! Повторите урок!";
    }

    /**
     *
     * @return string with percentage of correct answers
     */
    public String getFullResult() {
        String fixedresult = String.format("%.0f", (getResult() * 100));
        return "Вы правильно ответили на " + getCorrectanswers() + " вопросов из " + getTotalanswers() +
        ". \nПроцент выполнения: " + fixedresult + "%";

    }

    /**
     * needs to set the field in SharedPreferences which shows that lesson is done successfully
     * @return true if user answers more than 60% questions correctly
     */
    public boolean isSuccessful() {
        if (result>=0.6) return true;
        return false;
    }
    /**
     *
     * inc number of correct answers and total answers
     * calling method to put the string with info
     */
    public void userIsCorrect(Question question, int numberOfQuestion) {
        correctanswers+=1;
        totalanswers+=1;
        result = (float) correctanswers/totalanswers;
        putCorrectAnswer(question, numberOfQuestion);
    }

    /**
     *
     * inc number of incorrect answers and total answers
     * calling method to put the string with info
     */

    public void userIsWrong(Question question, int numberOfQuestion, String userAnswer) {
        wronganswers+=1;
        totalanswers+=1;
        result = (float) correctanswers/totalanswers;
        putWrongAnswer(question, numberOfQuestion, userAnswer);
    }

    /**
     *
     * specifying the exact string to be put into List
     * put string into List
     */
    private void putCorrectAnswer(Question question, int numberOfQuestion) {
        String infostring = "Вопрос № " + numberOfQuestion + "\n" + "ПРАВИЛЬНО!";
        information.add(infostring);
    }

    /**
     *
     * specifying the exact string to be put into List
     * the string depends on type of question
     * put string into List
     */
    private void putWrongAnswer(Question question, int numberOfQuestion, String userAnswer) {
        String infostring;
        switch (question.getType()) {
            case "PICQ":
                 infostring = "Вопрос № "+numberOfQuestion+"\n"+"НЕВЕРНЫЙ ОТВЕТ!"+"\n"+
                                    "Были показаны следующие картинки: \n"+ question.getWords_ru()+
                                    "\n"+ question.getPic_title()+"\nВаши ответы: \n"+userAnswer+
                                    "\nПравильные ответы: \n"+
                                    "1)"+question.getAns_1()+" 2)"+question.getAns_2()+" 3)"+question.getAns_3();
                information.add(infostring);
                break;
            case "YNQ":
                 infostring = "Вопрос № "+numberOfQuestion+"\n"+"НЕВЕРНЫЙ ОТВЕТ!"+"\n"+
                                     "Понятие на английском и перевод: \n"+question.getEng_word()+"\n"+
                                     question.getRus_word()+"\nВаш ответ: \n"+userAnswer+"\nПравильный ответ:\n"+question.getAnswer();
                information.add(infostring);
                break;
            case "MATCHQ":
                infostring = "Вопрос № "+numberOfQuestion+"\n"+"НЕВЕРНЫЙ ОТВЕТ!"+"\n"+
                             "Понятия на английском и перевод: \n"+question.getWords_en()+"\n"+
                             question.getWords_ru()+"\nВаши ответы: \n"+userAnswer+"\nПравильные ответы:\n"+
                             "1)"+question.getAns_1()+" 2)"+question.getAns_2()+" 3)"+question.getAns_3();
                information.add(infostring);
                break;
            case "CORQ":
                infostring = "Вопрос № "+numberOfQuestion+"\n"+"НЕВЕРНЫЙ ОТВЕТ!"+"\n"+
                             "Предложение или слово на английском: \n"+question.getBase()+"\nВаш ответ: \n"+userAnswer+
                             "\nПравильный ответ:\n"+question.getAnswer();
                information.add(infostring);
                break;
        }

    }

}
