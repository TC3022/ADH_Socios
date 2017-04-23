package csf.itesm.mx.adhsocios.models;

import java.util.Date;

/**
 * Created by rubcuadra on 4/22/17.
 */

public class Estudio
{
    private String name;

    private String id;
    private Date start;
    private Date end;
    private Date applied; //Allows Null

    public Estudio(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Estudio(String name, String id, Date start, Date end, Date applied)
    {
        this.name = name;
        this.id = id;
        this.start = start;
        this.end = end;
        this.applied = applied;
    }
    public boolean isDone()
    {
        return applied!=null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getApplied() {
        return applied;
    }

    public void setApplied(Date applied) {
        this.applied = applied;
    }
}
