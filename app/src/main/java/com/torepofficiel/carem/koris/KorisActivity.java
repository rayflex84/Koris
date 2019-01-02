package com.torepofficiel.carem.koris;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.torepofficiel.carem.koris.com.torepofficiel.carem.async_task_implementations.DatabaseAsyncTask;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Utilisateur;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.dialog.DialogActivity;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.ClasseUtilitaire;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.chrisjenx.calligraphy.CalligraphyUtils;

import static com.torepofficiel.carem.koris.TabFragment1.CANCEL_TAG;

public class KorisActivity extends DialogActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String NOM_PRENOMS = "nom_prenoms";
    public static final String SOLDE_LOCAL = "solde_local";
    public static final String SOLDE_EPARGNE_LOCAL = "solde_epargne_local";
    public static final String SOLDE_LIGNE = "solde_ligne";
    public static final String SOLDE_EPARGNE_LIGNE = "solde_epargne_ligne";
    public static final String NUMERO = "numero";
    public static final String WHICH_FRAGMENT = "which_fragment";
    public static final int REQUESTCODE = 1;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private long mBackPressed;
    private static long onStoppingTime;
    private Toast exit_toast;

    private FloatingActionButton fab,
                                 fab_recevoir;

    private static WeakReference<KorisActivity> mActivity;
    public static Bundle utilisateur_bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_koris);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/andrfont.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        initialiseViews();
        setListeners();

    }

    public static KorisActivity getInstance(){
        return (mActivity == null || mActivity.get() == null) ? null : mActivity.get();
    }


    private void initialiseViews(){

        Utilisateur utilisateur = new Utilisateur();
        try {
            utilisateur = DatabaseAsyncTask.selectionnerUtilisateur(1L);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //utilisateur.setSolde_compte_local(500L);

        utilisateur_bundle = new Bundle();
        utilisateur_bundle.putString(NOM_PRENOMS, utilisateur.getNom() + " " + utilisateur.getPrenom());
        utilisateur_bundle.putString(NUMERO, utilisateur.getNumero());
        utilisateur_bundle.putLong(SOLDE_LOCAL, utilisateur.getSolde_compte_local());
        utilisateur_bundle.putLong(SOLDE_EPARGNE_LOCAL, utilisateur.getSolde_compte_epargne_local());
        utilisateur_bundle.putLong(SOLDE_LIGNE, utilisateur.getSolde_compte_ligne());
        utilisateur_bundle.putLong(SOLDE_EPARGNE_LIGNE, utilisateur.getSolde_compte_epargne_ligne());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                animateFab(tab.getPosition());

                switch (tab.getPosition()){
                    case 0:
                        fab.setVisibility(View.VISIBLE);
                        fab.setImageResource(R.drawable.ic_arrow_send);
                        fab_recevoir.setVisibility(View.VISIBLE);
                        if(TabFragment1.getInstance() != null) {
                            if(TabFragment1.getInstance().mRequestQueue != null) {
                                TabFragment1.getInstance().mRequestQueue.stop();
                                TabFragment1.getInstance().mRequestQueue.cancelAll(CANCEL_TAG);
                            }
                        }
                        break;
                    case 1:
                        fab.setVisibility(View.VISIBLE);
                        fab.setImageResource(R.drawable.ic_arrow_send_online);
                        fab_recevoir.setVisibility(View.GONE);
                        if(TabFragment1.getInstance() != null)
                            TabFragment1.getInstance().fetchDataOnline();
                        break;
                    case 2:
                        fab.setVisibility(View.GONE);
                        fab_recevoir.setVisibility(View.GONE);
                        if(TabFragment1.getInstance() != null){
                            if(TabFragment1.getInstance().mRequestQueue != null) {
                                TabFragment1.getInstance().mRequestQueue.stop();
                                TabFragment1.getInstance().mRequestQueue.cancelAll(CANCEL_TAG);
                            }
                        }
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        changeFontInViewGroup(tabLayout,"fonts/andrfont.ttf");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_recevoir = (FloatingActionButton) findViewById(R.id.fab_recevoir);
        TextView nav_nom_prenom = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_nom_prenom);
        nav_nom_prenom.setText(utilisateur_bundle.getString(NOM_PRENOMS));
        TextView nav_korisid = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_korisid);
        nav_korisid.setText(getString(R.string.nav_korisid_text, utilisateur_bundle.getString(NUMERO)));
    }

    private void setListeners(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void animateFab(final int position){

        //floating action button animation

        final ScaleAnimation shrink = new ScaleAnimation(1f, .2f, 1f, .2f, Animation.RELATIVE_TO_SELF, .5f,
                Animation.RELATIVE_TO_SELF, .5f);

        shrink.setDuration(150);
        shrink.setInterpolator(new DecelerateInterpolator());
        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //Scale up animation
                ScaleAnimation expand = new ScaleAnimation(.2f, 1f, .2f, 1f, Animation.RELATIVE_TO_SELF, .5f,
                        Animation.RELATIVE_TO_SELF, .5f);
                expand.setDuration(100);
                expand.setInterpolator(new AccelerateInterpolator());
                fab.startAnimation(expand);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Animation translate = new TranslateAnimation(0, 0, fab.getY(), 0 );
        translate.setDuration(300);
        translate.setInterpolator(new AccelerateInterpolator());

        if(position == 0) {
            fab_recevoir.startAnimation(translate);

            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fab.startAnimation(shrink);
                }
            }, 300);
        } else {
            fab.startAnimation(shrink);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (onStoppingTime + 300000L < System.currentTimeMillis()) {
            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);
            finish();
        }

        if(TabFragment1.getInstance() != null){
            TabFragment1.getInstance().attachIntanceToRequestQueue();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        ErrorActivity.currentActivity = new WeakReference<>((Activity) this);
        mActivity = new WeakReference<>(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        onStoppingTime = System.currentTimeMillis();
        if(TabFragment2.getInstance() != null) {
            if (TabFragment2.getInstance().myAlertDialog != null) {
                TabFragment2.getInstance().myAlertDialog.dismiss();
                TabFragment2.getInstance().myAlertDialog = null;
            }
        }
        if(TabFragment1.getInstance() != null) {
            if (TabFragment1.getInstance().mRequestQueue != null){
                TabFragment1.getInstance().mRequestQueue.stop();
                TabFragment1.getInstance().mRequestQueue.cancelAll(TabFragment1.CANCEL_TAG);
                TabFragment1.getInstance().mRequestQueue = null;
            }
        }
    }

    public void changeFontInViewGroup(ViewGroup viewGroup, String fontPath){
        for(int i = 0; i < viewGroup.getChildCount(); i++){
            View child = viewGroup.getChildAt(i);
            if(TextView.class.isAssignableFrom(child.getClass())){
                CalligraphyUtils.applyFontToTextView(child.getContext(), (TextView) child, fontPath);
            } else if(ViewGroup.class.isAssignableFrom(child.getClass())){
                changeFontInViewGroup((ViewGroup) viewGroup.getChildAt(i), fontPath);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mBackPressed + ClasseUtilitaire.TIME_INTERVAL > System.currentTimeMillis()) {
                exit_toast.cancel();
                finish();
                return;
            }
            mBackPressed = System.currentTimeMillis();
            exit_toast = Toast.makeText(this, getString(R.string.exit_toast_msg), Toast.LENGTH_LONG);
            exit_toast.show();
        }
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_recevoir) {
            // Handle the camera action
        } else if (id == R.id.nav_envoyer) {

        } else if (id == R.id.nav_historique_local) {

        } else if (id == R.id.nav_transfert) {

        } else if (id == R.id.nav_historique_ligne) {

        } else if (id == R.id.nav_abonnement) {

            mViewPager.setCurrentItem(2, true);

        } else if (id == R.id.nav_modifier_account) {

        } else if (id == R.id.nav_modifier_mdp) {

        } else if (id == R.id.nav_sauvegarder_donnees) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void doPositiveClick(DialogInterface dialog, int id) {
        Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void doNegativeClick() {

    }


}
