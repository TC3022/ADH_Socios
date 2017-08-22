package csf.itesm.mx.adhsocios.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rubcuadra on 2/20/17.
 */

public class UserResults
{
    private List<ResultPackage> Weight;
    private List<ResultPackage> Bmi;
    private List<ResultPackage> Fat;
    private List<ResultPackage> Muscle;

    public void addWeight(ResultPackage w) {Weight.add(w);}
    public void addBmi(ResultPackage w) {Bmi.add(w);}
    public void addFat(ResultPackage w) {Fat.add(w);}
    public void addMuscle(ResultPackage w) {Muscle.add(w);}

    public UserResults()
    {
        Weight = new ArrayList<>();
        Bmi = new ArrayList<>();
        Fat = new ArrayList<>();
        Muscle = new ArrayList<>();
    }
    public static int getAmountOfResults()
    {
        return 4; //Hardcodeado pero equis
    }

    public UserResults(List<ResultPackage> weight, List<ResultPackage> bmi, List<ResultPackage> fat, List<ResultPackage> muscle)
    {
        Weight = weight;
        Bmi = bmi;
        Fat = fat;
        Muscle = muscle;
    }

    public List<ResultPackage> getMuscle() {
        return Muscle;
    }

    public void setMuscle(List<ResultPackage> muscle) {
        Muscle = muscle;
    }

    public List<ResultPackage> getFat() {
        return Fat;
    }

    public void setFat(List<ResultPackage> fat) {
        Fat = fat;
    }

    public List<ResultPackage> getBmi() {
        return Bmi;
    }

    public void setBmi(List<ResultPackage> bmi) {
        Bmi = bmi;
    }

    public List<ResultPackage> getWeight() {
        return Weight;
    }

    public void setWeight(List<ResultPackage> weight) {
        Weight = weight;
    }

    public static String listToString(List<ResultPackage> l)
    {
        String result = "";
        for (int i = 0; i < l.size(); i++)
            result += "\n\t\t"+l.get(i).toString();
        return result;
    }

    @Override
    public String toString()
    {
        return "UserResults{\n" +
                "Weight=" + listToString(Weight)+
                ",\n Bmi=" + listToString(Bmi) +
                ",\n Fat=" + listToString(Fat) +
                ",\n Muscle=" + listToString(Muscle) +
                '}';
    }
}
