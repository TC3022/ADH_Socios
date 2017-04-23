package csf.itesm.mx.adhsocios.models;

import java.util.Date;

/**
 * Created by rubcuadra on 2/20/17.
 */

public class ResultPackage
{
    private double value;
    private Date date;

    public ResultPackage(double value, Date date) {
        this.value = value;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ResultPackage{" +
                "value=" + value +
                ", date='" + date.toString() + '\'' +
                '}';
    }
}
