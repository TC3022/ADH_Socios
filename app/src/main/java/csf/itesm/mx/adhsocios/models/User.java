package csf.itesm.mx.adhsocios.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rubcuadra on 2/15/17.
 */

public class User extends RealmObject //Se llamaba Datos_Model en la otra actividad
{
    @PrimaryKey
    private int id;

    private String Firtname;
    private String Lastname;
    private double Estatura;
    private String Gender;
    private long Companyid;
    private String Associateimage;
    private String AssociateId;
    private String NmComplete;
    private boolean Logged;

    private String host;

    public User(){};

    public User(int id, String firtname, String lastname, double estatura, String gender, long companyid, String associateimage, String associateId, String nmComplete, boolean logged,String host)
    {
        this.id = id;
        Firtname = firtname;
        Lastname = lastname;
        Estatura = estatura;
        Gender = gender;
        Companyid = companyid;
        Associateimage = associateimage;
        AssociateId = associateId;
        NmComplete = nmComplete;
        Logged = logged;
        this.host = host;
    }

    public boolean isLogged() {
        return Logged;
    }

    public void setLogged(boolean logged) {
        Logged = logged;
    }

    public String getNmComplete() {
        return NmComplete;
    }

    public void setNmComplete(String nmComplete) {
        NmComplete = nmComplete;
    }

    public String getAssociateId() {
        return AssociateId;
    }

    public void setAssociateId(String associateId) {
        AssociateId = associateId;
    }

    public String getAssociateimage() {
        return Associateimage;
    }

    public void setAssociateimage(String associateimage) {
        Associateimage = associateimage;
    }

    public long getCompanyid() {
        return Companyid;
    }

    public void setCompanyid(long companyid) {
        Companyid = companyid;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public double getEstatura() {
        return Estatura;
    }

    public void setEstatura(double estatura) {
        Estatura = estatura;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getFirtname() {
        return Firtname;
    }

    public void setFirtname(String firtname) {
        Firtname = firtname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
