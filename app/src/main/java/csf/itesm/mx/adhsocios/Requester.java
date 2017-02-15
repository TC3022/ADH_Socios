package csf.itesm.mx.adhsocios;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by rubcuadra on 2/15/17.
 */

public class Requester extends Application
{
    private RequestQueue mRequestQ;
    public static final String TAG = "VolleyPatterns";
    private static Requester sInstance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        sInstance = this;

        //Configure REALM
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
    public static synchronized Requester getInstance()
    {
        return sInstance;
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQ == null)
        {
            mRequestQ = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQ;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag)
    {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        VolleyLog.d("Adding request to queue: %s", req.getUrl());
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req)
    {
        Log.d("Voll", "adding");
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
    public void cancelPendingRequests(Object tag)
    {
        if (mRequestQ != null)
        {
            mRequestQ.cancelAll(tag);
        }
    }
    public void cancelPendingRequests()
    {
        if (mRequestQ != null)
        {
            mRequestQ.cancelAll(TAG);
        }
    }
}
