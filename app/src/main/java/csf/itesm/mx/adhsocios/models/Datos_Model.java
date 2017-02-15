package csf.itesm.mx.adhsocios.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rubcuadra on 2/15/17.
 */

public class Datos_Model extends RealmObject
{
    @PrimaryKey
    private int id;

    private String Firtname;
    private String Lastname;
    private String Estatura;
    private String Gender;
    private String Companyid;
    private String Associateimage;
    private String AssociateId;
    private String NmComplete;
    private boolean Logged;

    public Datos_Model(int id,String firtname, String lastname, String estatura, String gender, String companyid, String associateimage, String associateId, String nmComplete, boolean logged)
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

    public String getCompanyid() {
        return Companyid;
    }

    public void setCompanyid(String companyid) {
        Companyid = companyid;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getEstatura() {
        return Estatura;
    }

    public void setEstatura(String estatura) {
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
}
