package csf.itesm.mx.adhsocios.Realm;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import csf.itesm.mx.adhsocios.models.Datos_Model;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by rubcuadra on 2/15/17.
 */

public class RealmController
{
    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application)
    {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment)
    {
        if (instance == null)
        {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application)
    {
        if (instance == null)
        {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    public void clearAll()
    {
        realm.beginTransaction();
        realm.clear(Datos_Model.class);
        realm.commitTransaction();
    }

    public RealmResults<Datos_Model> getDatos_Model()
    {
        return realm.where(Datos_Model.class).findAll();
    }

    //query a single item with the given id
    public Datos_Model getDatos_Model(int id)
    {
        return realm.where(Datos_Model.class).equalTo("id", id).findFirst();
    }

    public boolean hasDatos_Models()
    {
        return !realm.allObjects(Datos_Model.class).isEmpty();
    }
}
