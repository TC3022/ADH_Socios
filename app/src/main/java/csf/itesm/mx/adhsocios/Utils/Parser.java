package csf.itesm.mx.adhsocios.Utils;

import android.os.Bundle;

import csf.itesm.mx.adhsocios.models.User;

/**
 * Created by rubcuadra on 2/20/17.
 */

public class Parser
{
    public static Bundle UserToBundle(User u )
    {
        Bundle b = new Bundle();
        b.putString("fname",u.getFirtname());
        b.putString("lname",u.getLastname());
        b.putString("gender",u.getGender());
        b.putString("assoc_img",u.getAssociateimage());
        b.putString("assoc_id",u.getAssociateId());
        b.putString("nmcomp",u.getNmComplete());
        b.putLong("compId",u.getCompanyid());
        b.putBoolean("logged",u.isLogged());
        b.putDouble("estat",u.getEstatura());
        return b;
    }
    public static User UserFromBundle(Bundle b)
    {
        User u = new User();
        u.setFirtname( b.getString("fname") );
        u.setLastname( b.getString("lname") );
        u.setGender( b.getString("gender") );
        u.setAssociateimage( b.getString("assoc_img") );
        u.setAssociateId( b.getString("assoc_id") );
        u.setNmComplete( b.getString("nmcomp") );
        u.setCompanyid( b.getLong("compId") );
        u.setLogged( b.getBoolean("logged") );
        u.setEstatura( b.getDouble("estat") );
        return u;
    }
}
