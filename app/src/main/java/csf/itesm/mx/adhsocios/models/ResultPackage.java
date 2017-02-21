package csf.itesm.mx.adhsocios.models;

/**
 * Created by rubcuadra on 2/20/17.
 */

public class ResultPackage
{
    private double value;
    private String date;

    public ResultPackage(double value, String date) {
        this.value = value;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
