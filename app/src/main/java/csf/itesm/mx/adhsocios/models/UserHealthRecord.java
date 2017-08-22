package csf.itesm.mx.adhsocios.models;

import java.util.Date;

/**
 * Created by rubcuadra on 2/20/17.
 */

public class UserHealthRecord
{
    private String description;
    private Date date;
    private String title;

    public UserHealthRecord(){}

    public Date getDate() {return date;}

    public void setDate(Date date) {this.date = date;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}

    public void setDescription(String description) {
        this.description = description;
    }
}
