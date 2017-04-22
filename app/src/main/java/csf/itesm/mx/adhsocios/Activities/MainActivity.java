package csf.itesm.mx.adhsocios.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import csf.itesm.mx.adhsocios.Fragments.MiSaludFragment;
import csf.itesm.mx.adhsocios.Fragments.MisEstudiosFragment;
import csf.itesm.mx.adhsocios.Fragments.MisResultadosFragment;
import csf.itesm.mx.adhsocios.R;
import csf.itesm.mx.adhsocios.models.User;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements MisResultadosFragment.onMisResultadosInteractionListener,MiSaludFragment.OnMiSaludInteractionListener, MisEstudiosFragment.OnMisEstudiosInteractionListener
{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static final String TAG = "Main";

    private User user;
    private Realm mRealm;
    /**
     * The {@link ViewPager} that will host the section contents.
     */

    @BindView(R.id.container) ViewPager mViewPager;
    @BindView(R.id.fab) FloatingActionButton fab;


    @OnClick
    public void fabClick(View view)
    {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRealm = Realm.getDefaultInstance();
        user = mRealm.where(User.class).findFirst();

        if ( user == null ) //Not Logged, enviar al login
        {
            askToLogin();
            return;
        }

        Log.d(TAG, String.format("%s %s %sm",user.getAssociateId(),user.getNmComplete(),user.getEstatura()) );


        setAdapter();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
            @Override
            public void onPageSelected(final int position)
            {
                switch (position)
                {
                    case 0:
                    case 2:
                        fab.show();
                        break;
                    case 1:
                        fab.hide();
                        break;
                }
            }
        });
    }

    void setAdapter()
    {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //Layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }
    void askToLogin()
    {
        startActivity(new Intent().setClass(MainActivity.this, LoginActivity.class)); //Llamar Login
        finish();                                                                     //Y matar main
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {
            case R.id.action_logout:
                mRealm.beginTransaction();
                    mRealm.clear(User.class); //Quitar registro del ususuario loggeado
                mRealm.commitTransaction();
                user = null;                     //Asegurar que lo dejamos nulo
                askToLogin();                    //Matar esta actividad y abrir login
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMisResultadosInteraction(Uri uri) {}

    @Override
    public void OnMiSaludInteraction(Uri uri) {}

    @Override
    public void OnMisEstudiosInteraction() {}

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0:
                    return MiSaludFragment.newInstance(user);
                case 1:
                    return MisResultadosFragment.newInstance(user);
                default:
                    return MisEstudiosFragment.newInstance(user);
            }

        }

        @Override
        public int getCount() {return 3;} //Total fragments

        @Override
        public CharSequence getPageTitle(int position)
        {
            String section01 = getResources().getString(R.string.section01);
            String section02 = getResources().getString(R.string.section02);
            String section03 = getResources().getString(R.string.section03);
            switch (position)
            {
                case 0:
                    return section01;
                case 1:
                    return section02;
                case 2:
                    return section03;
            }
            return null;
        }
    }
}
