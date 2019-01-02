package com.torepofficiel.carem.koris;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.async_task_implementations.ConnexionCheckingAsyncTask;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Utilisateur;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.dialog.MyAlertDialog;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.enums.Type_Account;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.exceptions.ValeurChampsIncorrecteException;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.Calendrier;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.ClasseUtilitaire;

import java.lang.ref.WeakReference;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyUtils;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;
import static android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
import static com.torepofficiel.carem.koris.R.id.mdp;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static WeakReference<PlaceholderFragment> mPlaceholderFragment;

    private EditText dateNaissance_editText,
            mMdp,
            mNom,
            mPrenoms,
            mPurseId,
            mEmail,
            mVille,
            mId_card;

    private TextView dateNaissance_error,
            mMdp_error,
            mNom_error,
            mPrenoms_error,
            mPurseId_error,
            mEmail_error,
            mVille_error,
            mSpinner_label,
            mToggle_mdp,
            mId_card_error;

    private RelativeLayout no_connection_layout;

    private AppCompatCheckBox mLegal_text;

    private TextView mMdp_label,
            mFill_all,
            refresh_connexion;

    private ProgressBar refresh_progress,
                        korisId_progress,
                        id_card_progress;

    private ImageView korisId_img_valide,
                      korisId_img_disable,
                      name_img_valide,
                      name_img_disable,
                      firstname_img_valide,
                      firstname_img_disable,
                      email_img_valide,
                      email_img_disable,
                      mdp_img_valide,
                      mdp_img_disable,
                      id_card_img_valide,
                      id_card_img_disable,
                      date_naissance_img_valide,
                      date_naissance_img_disable,
                      ville_img_valide,
                      ville_img_disable;

    private ScrollView mScrollView;

    private Spinner list_account_type;
    private static  boolean[] are_empties = new boolean[10];
    private static  boolean[] view_errors = new boolean[7];
    private static boolean connection_error;

    private boolean is_phone_not_unique = false;
    private boolean is_id_card_not_unique = false;
    private boolean invalid_phone_format = false;
    private static String MSG_PB_CONNEXION;
    private static final int NOM_PRENOM_MIN_LENGTH = 3;
    private static final int MDP_MIN_LENGTH = 7;

    private boolean toggle_mdp_visible = false;
    public boolean showingRefreshProgress;

    private Timestamp date_naissance;
    String account_type;

    private static WeakReference<MyAlertDialog> alert;

    public PlaceholderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;

            mPlaceholderFragment = new WeakReference<>(this);
            rootView = inflater.inflate(R.layout.first_registration, container, false);
            initializeViewFields(rootView);
            viewFieldsListenersSetting();

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(RegistrationActivity.getInstance(),
                    R.array.list_account_type, R.layout.spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            list_account_type.setAdapter(adapter);

        return rootView;
    }

    public static PlaceholderFragment getInstance(){
        return (mPlaceholderFragment != null) ? mPlaceholderFragment.get() : null;
    }

    public static boolean[] getAre_empties(){
        return are_empties;
    }

    private void initializeViewFields(View view) {

        for (int i = 0; i < 10 ; i++){
            are_empties[i] = true;
        }

        for (int i = 0; i < 7 ; i++){
            view_errors[i] = true;
        }
        connection_error = true;

        MSG_PB_CONNEXION = getString(R.string.prob_connexion);

        mFill_all = (TextView) view.findViewById(R.id.fill_all);
        dateNaissance_editText = (EditText) view.findViewById(R.id.date_naissance);
        mMdp = (EditText) view.findViewById(mdp);
        mMdp_label = (TextView) view.findViewById(R.id.mdp_label);
        mNom = (EditText) view.findViewById(R.id.nom);
        mPrenoms = (EditText) view.findViewById(R.id.prenom);
        mEmail = (EditText) view.findViewById(R.id.email);
        mPurseId = (EditText) view.findViewById(R.id.korisId);
        mVille = (EditText) view.findViewById(R.id.ville);
        mLegal_text = (AppCompatCheckBox) view.findViewById(R.id.legal_text);
        list_account_type = (Spinner) view.findViewById(R.id.account_type);
        mScrollView = (ScrollView) view.findViewById(R.id.myscrollview);
        dateNaissance_error = (TextView) view.findViewById(R.id.date_naissance_error);
        mMdp_error = (TextView) view.findViewById(R.id.mdp_error);
        mNom_error = (TextView) view.findViewById(R.id.nom_error);
        mPrenoms_error = (TextView) view.findViewById(R.id.prenom_error);
        mEmail_error = (TextView) view.findViewById(R.id.email_error);
        mPurseId_error = (TextView) view.findViewById(R.id.korisId_error);
        mVille_error = (TextView) view.findViewById(R.id.ville_error);
        mSpinner_label = (TextView) view.findViewById(R.id.spinner_label);
        mToggle_mdp = (TextView) view.findViewById(R.id.toggle_button_mdp_registration);
        mId_card = (EditText) view.findViewById(R.id.id_card);
        mId_card_error = (TextView) view.findViewById(R.id.id_card_error);
        no_connection_layout = (RelativeLayout) view.findViewById(R.id.noConnexion_layout);
        refresh_connexion = (TextView) view.findViewById(R.id.refresh_connexion);
        refresh_progress = (ProgressBar) view.findViewById(R.id.refresh_progress);
        refresh_progress.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(RegistrationActivity.getInstance().getApplicationContext(),R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        korisId_progress = view.findViewById(R.id.korisId_progress);
        korisId_progress.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(RegistrationActivity.getInstance().getApplicationContext(),R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        korisId_img_valide = view.findViewById(R.id.korisId_img_valide);
        korisId_img_disable = view.findViewById(R.id.korisId_img_disable);
        name_img_valide = view.findViewById(R.id.name_img_valide);
        name_img_disable = view.findViewById(R.id.name_img_disable);
        firstname_img_valide = view.findViewById(R.id.firstname_img_valide);
        firstname_img_disable = view.findViewById(R.id.firstname_img_disable);
        email_img_valide = view.findViewById(R.id.email_img_valide);
        email_img_disable = view.findViewById(R.id.email_img_disable);
        mdp_img_valide = view.findViewById(R.id.mdp_img_valide);
        mdp_img_disable = view.findViewById(R.id.mdp_img_disable);
        id_card_progress = view.findViewById(R.id.id_card_progress);
        id_card_progress.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(RegistrationActivity.getInstance().getApplicationContext(),R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        id_card_img_valide = view.findViewById(R.id.id_card_img_valide);
        id_card_img_disable = view.findViewById(R.id.id_card_img_disable);
        date_naissance_img_valide = view.findViewById(R.id.date_naissance_img_valide);
        date_naissance_img_disable = view.findViewById(R.id.date_naissance_img_disable);
        ville_img_valide = view.findViewById(R.id.ville_img_valide);
        ville_img_disable = view.findViewById(R.id.ville_img_disable);

    }

    public void checkingConnexionState(boolean state){
        no_connection_layout.setVisibility( (state) ? View.GONE : View.VISIBLE );
        connection_error = !state;

        viewValidity();
    }

    public Utilisateur getNewUtilisateur(){

        Utilisateur newUtilisateur = new Utilisateur();
        boolean empty = false;
        for(boolean is_empty : are_empties){
            empty = empty || is_empty;
        }

        boolean error = false;
        for(boolean view_error : view_errors){
            error = error || view_error;
        }

        if(!empty && !error && !connection_error)
            newUtilisateur = createUtilisateur();

        return newUtilisateur;
    }

    public Utilisateur createUtilisateur(){
        String nom = mNom.getText().toString().trim(),
                prenom = mPrenoms.getText().toString().trim(),
                email = mEmail.getText().toString().trim(),
                numero = mPurseId.getText().toString().trim(),
                id_card = mId_card.getText().toString().trim(),
                mdp = mMdp.getText().toString().trim(),
                ville = mVille.getText().toString().trim();

        return new Utilisateur(nom, prenom, email, date_naissance, ClasseUtilitaire.getUserSubscriberId(),
                numero, id_card,mdp, ville, account_type);

    }

    public boolean showRefreshProgress(final boolean show) {

        refresh_connexion.setVisibility(show ? View.INVISIBLE : View.VISIBLE);

        refresh_progress.setVisibility(show ? View.VISIBLE : View.INVISIBLE);

        return show;
    }

    private void viewFieldsListenersSetting() {

        dateNaissance_editText.addTextChangedListener(getTextWatcher(6));

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }
        };

        dateNaissance_editText_listeners_settings(myCalendar, date);


        mLegal_text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                are_empties[8] = !b;

                viewValidity();
            }
        });


        list_account_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

                are_empties[9] = String.valueOf(id).equals(Type_Account.info.toString());

                account_type = ((TextView)view).getText().toString().trim();
                String msg = (are_empties[9]) ? getString(R.string.spinner_label1) :
                        getString(R.string.spinner_label2, account_type);

                int couleur = (are_empties[9]) ? ContextCompat.getColor(RegistrationActivity.getInstance().getApplicationContext(),
                        R.color.color_red_holo) :
                        ContextCompat.getColor(RegistrationActivity.getInstance().getApplicationContext(),
                                R.color.colorPrimary);

                mSpinner_label.setText(msg);
                mSpinner_label.setTextColor(couleur);

                viewValidity();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        View.OnTouchListener myTouchListener = getTouchListener();

        mMdp.setOnFocusChangeListener(ClasseUtilitaire.getOnFocusListener(mMdp_label, RegistrationActivity.getInstance(), 0));
        mMdp.addTextChangedListener(getTextWatcher(4));
        mId_card.addTextChangedListener(getTextWatcher(5));
        mId_card.setOnFocusChangeListener(getFocusChangeListenerForOnlineTasks(5));
        mNom.addTextChangedListener(getTextWatcher(0));
        mPrenoms.addTextChangedListener(getTextWatcher(1));
        mEmail.addTextChangedListener(getTextWatcher(2));
        mPurseId.addTextChangedListener(getTextWatcher(3));
        mPurseId.setOnFocusChangeListener(getFocusChangeListenerForOnlineTasks(3));
        mVille.addTextChangedListener(getTextWatcher(7));

        mScrollView.setOnTouchListener(myTouchListener);

        mMdp.setOnTouchListener(myTouchListener);
        mId_card.setOnTouchListener(myTouchListener);
        mNom.setOnTouchListener(myTouchListener);
        mPrenoms.setOnTouchListener(myTouchListener);
        mEmail.setOnTouchListener(myTouchListener);
        mPurseId.setOnTouchListener(myTouchListener);
        mVille.setOnTouchListener(myTouchListener);
        dateNaissance_editText.setOnTouchListener(myTouchListener);
        mLegal_text.setOnTouchListener(myTouchListener);
        list_account_type.setOnTouchListener(myTouchListener);

        mToggle_mdp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                toggle_mdp_visible = !toggle_mdp_visible;
                if (toggle_mdp_visible) {
                    mToggle_mdp.setText(getString(R.string.toggle_mdp_cacher));
                    mMdp.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    mToggle_mdp.setText(getString(R.string.toggle_mdp_afficher));
                    mMdp.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
                }
                mMdp.setSelection(mMdp.getEditableText().toString().length());

                CalligraphyUtils.applyFontToTextView(mMdp.getContext(), mMdp, "fonts/andrfont.ttf");
            }
        });

        refresh_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showingRefreshProgress = showRefreshProgress(true);
                new ConnexionCheckingAsyncTask(1).execute((Void) null);

            }
        });

    }

    public View.OnTouchListener getTouchListener(){
        return new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:

                        ScaleAnimation expand = new ScaleAnimation(.2f, 1f, .2f, 1f, Animation.RELATIVE_TO_SELF, .5f,
                                Animation.RELATIVE_TO_SELF, .5f);
                        expand.setDuration(100);
                        expand.setFillAfter(true);
                        expand.setFillEnabled(true);
                        expand.setInterpolator(new AccelerateInterpolator());
                        RegistrationActivity.getInstance().startAnimationOnFab_layout(expand);

                        break;
                    case MotionEvent.ACTION_DOWN:

                        ScaleAnimation shrink = new ScaleAnimation(1f, .2f, 1f, .2f, Animation.RELATIVE_TO_SELF, .5f,
                                Animation.RELATIVE_TO_SELF, .5f);

                        shrink.setDuration(150);
                        shrink.setFillEnabled(true);
                        shrink.setFillAfter(true);
                        shrink.setInterpolator(new DecelerateInterpolator());
                        RegistrationActivity.getInstance().startAnimationOnFab_layout(shrink);
                        break;
                }

                /*if (event.getAction() == MotionEvent.ACTION_UP) {

                }*/

                return false;
            }
        };
    }

    private void viewValidity() {

        boolean empty = false;
        for(boolean is_empty : are_empties){
            empty = empty || is_empty;
        }

        boolean error = false;
        for(boolean view_error : view_errors){
            error = error || view_error;
        }

        RegistrationActivity.getInstance().updateCustomFbsSectionOne(empty || error || connection_error);

        if(!empty && !error && !connection_error)
            mFill_all.setVisibility(View.INVISIBLE);
        else
            mFill_all.setVisibility(View.VISIBLE);

        if(empty) mFill_all.setText(RegistrationActivity.getInstance().getString(R.string.fill_all_empty));
        else if(error) mFill_all.setText(RegistrationActivity.getInstance().getString(R.string.fill_all_error));
        else if(connection_error) mFill_all.setText(MSG_PB_CONNEXION);

    }


    public void updateLabel(Calendar calendar){

        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.FRANCE);
        String dateNaissance = sdf.format(calendar.getTime());

        Calendrier calendrier = null;

        try {
            calendrier = new Calendrier(dateNaissance);
        } catch (ValeurChampsIncorrecteException e) {
            e.printStackTrace();

            final String vcie = e.getMessage();
            if(RegistrationActivity.getInstance() != null){
                MyAlertDialog.choix = 2;
                 alert = new WeakReference<MyAlertDialog>(RegistrationActivity.getInstance().showAlertDialog(-1, RegistrationActivity.getInstance().getString(R.string.date_naissance_erronnee),
                                vcie + RegistrationActivity.getInstance().getString(R.string.msg_date_naissance_erronnee),
                                R.mipmap.logo_small, RegistrationActivity.getInstance().getString(R.string.ok_dialog_cnx_checking), ""));

                alert.get().setCancelable(false);

            }

            return;

        }


        Calendar cal = GregorianCalendar.getInstance();
        cal.set(calendrier.getAnnee(),
                calendrier.getMois() - 1, calendrier.getJour());

        date_naissance = new Timestamp(
                cal.getTimeInMillis()
        );

        dateNaissance_editText.setText(calendrier.toString());
    }

    private void dateNaissance_editText_listeners_settings(final Calendar myCalendar, final DatePickerDialog.OnDateSetListener date){
        dateNaissance_editText.setKeyListener(null);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(RegistrationActivity.getInstance() != null) {
                    MyAlertDialog.choix = 3;
                     RegistrationActivity.getInstance().showAlertDialog(R.layout.annee_naissance, R.string.titre_annee_naisance,
                            -1,
                            R.mipmap.logo_small, R.string.ok_dialog_cnx_checking, -1).setCancelable(false);

                }
            }
        };


        dateNaissance_editText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
               // datePickerDialog.show();
                new android.os.Handler().post(runnable);
                if(ClasseUtilitaire.isEmptyAndBlank(dateNaissance_editText.getText().toString())){
                    date_naissance_img_valide.setVisibility(View.INVISIBLE);
                    date_naissance_img_disable.setVisibility(View.VISIBLE);
                    dateNaissance_error.setVisibility(View.VISIBLE);
                    dateNaissance_error.setText(RegistrationActivity.getInstance().getString(R.string.empty_validation_message, getString(R.string.date_naissance)));
                }

                return false;
            }
        });

        dateNaissance_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //datePickerDialog.show();
                new android.os.Handler().post(runnable);

            }
        });

        dateNaissance_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                   // datePickerDialog.show();
                    new android.os.Handler().post(runnable);
                } else {
                    if(ClasseUtilitaire.isEmptyAndBlank(dateNaissance_editText.getText().toString())){
                        date_naissance_img_valide.setVisibility(View.INVISIBLE);
                        date_naissance_img_disable.setVisibility(View.VISIBLE);
                        dateNaissance_error.setVisibility(View.VISIBLE);
                        dateNaissance_error.setText(RegistrationActivity.getInstance().getString(R.string.empty_validation_message, getString(R.string.date_naissance)));
                    }
                }
            }
        });
    }

    private TextWatcher getTextWatcher(final int which){

        return (new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                String contenu = charSequence.toString().trim();

                switch (which){

                    case 0:

                    case 1:

                        view_errors[which] = ClasseUtilitaire.checkEditTextLength(contenu, NOM_PRENOM_MIN_LENGTH);
                        are_empties[which] = ClasseUtilitaire.isEmptyAndBlank(contenu);

                        if(are_empties[which]){
                            switch (which){
                                case 0:
                                    mNom_error.setVisibility(View.VISIBLE);
                                    mNom_error.setText(RegistrationActivity.getInstance().getString(R.string.empty_validation_message, getString(R.string.nom)));

                                    name_img_valide.setVisibility(View.INVISIBLE);
                                    name_img_disable.setVisibility(View.VISIBLE);
                                    break;

                                case 1:
                                    mPrenoms_error.setVisibility(View.VISIBLE);
                                    mPrenoms_error.setText(RegistrationActivity.getInstance().getString(R.string.empty_validation_message, getString(R.string.prenoms)));

                                    firstname_img_valide.setVisibility(View.INVISIBLE);
                                    firstname_img_disable.setVisibility(View.VISIBLE);
                                    break;
                            }
                        } else if(view_errors[which]){
                            switch (which){
                                case 0:
                                    mNom_error.setVisibility(View.VISIBLE);
                                    mNom_error.setText(RegistrationActivity.getInstance().getString(R.string.miniLength_validation_message, getString(R.string.nom), NOM_PRENOM_MIN_LENGTH));

                                    name_img_valide.setVisibility(View.INVISIBLE);
                                    name_img_disable.setVisibility(View.VISIBLE);
                                    break;

                                case 1:
                                    mPrenoms_error.setVisibility(View.VISIBLE);
                                    mPrenoms_error.setText(RegistrationActivity.getInstance().getString(R.string.miniLength_validation_message, getString(R.string.prenoms), NOM_PRENOM_MIN_LENGTH));

                                    firstname_img_valide.setVisibility(View.INVISIBLE);
                                    firstname_img_disable.setVisibility(View.VISIBLE);
                                    break;
                            }
                        } else{
                            switch (which){
                                case 0:
                                    mNom_error.setVisibility(View.GONE);
                                    mNom_error.setText("");

                                    name_img_valide.setVisibility(View.VISIBLE);
                                    name_img_disable.setVisibility(View.INVISIBLE);
                                    break;

                                case 1:
                                    mPrenoms_error.setVisibility(View.GONE);
                                    mPrenoms_error.setText("");

                                    firstname_img_valide.setVisibility(View.VISIBLE);
                                    firstname_img_disable.setVisibility(View.INVISIBLE);
                                    break;
                            }
                        }

                        break;

                    case 2:
                        view_errors[which] = ClasseUtilitaire.checkEmailValidation(contenu);
                        are_empties[which] = ClasseUtilitaire.isEmptyAndBlank(contenu);

                        if(are_empties[which]){

                            mEmail_error.setVisibility(View.VISIBLE);
                            mEmail_error.setText(RegistrationActivity.getInstance().getString(R.string.empty_validation_message, getString(R.string.prompt_email)));

                            email_img_valide.setVisibility(View.INVISIBLE);
                            email_img_disable.setVisibility(View.VISIBLE);

                        } else if(view_errors[which]){

                            mEmail_error.setVisibility(View.VISIBLE);
                            mEmail_error.setText(RegistrationActivity.getInstance().getString(R.string.error_invalid_email));

                            email_img_valide.setVisibility(View.INVISIBLE);
                            email_img_disable.setVisibility(View.VISIBLE);

                        } else{

                            mEmail_error.setVisibility(View.GONE);
                            mEmail_error.setText("");

                            email_img_valide.setVisibility(View.VISIBLE);
                            email_img_disable.setVisibility(View.INVISIBLE);

                        }

                        break;

                    case 3:

                        invalid_phone_format = ClasseUtilitaire.checkPhoneValidation(contenu);
                        view_errors[which] = invalid_phone_format || is_phone_not_unique;
                        are_empties[which] = ClasseUtilitaire.isEmptyAndBlank(contenu);

                        if(are_empties[which]){

                            mPurseId_error.setVisibility(View.VISIBLE);
                            mPurseId_error.setText(RegistrationActivity.getInstance().getString(R.string.empty_validation_message, getString(R.string.koris_id)));

                            korisId_img_disable.setVisibility(View.VISIBLE);
                            korisId_img_valide.setVisibility(View.INVISIBLE);
                            korisId_progress.setVisibility(View.INVISIBLE);

                        } else if(invalid_phone_format){

                            mPurseId_error.setVisibility(View.VISIBLE);
                            mPurseId_error.setText(RegistrationActivity.getInstance().getString(R.string.error_invalid_koris_id));

                            korisId_img_disable.setVisibility(View.VISIBLE);
                            korisId_img_valide.setVisibility(View.INVISIBLE);
                            korisId_progress.setVisibility(View.INVISIBLE);

                        } else if(is_phone_not_unique){

                            mPurseId_error.setVisibility(View.VISIBLE);
                            mPurseId_error.setText(RegistrationActivity.getInstance().getString(R.string.phone_not_unique));

                            korisId_img_disable.setVisibility(View.VISIBLE);
                            korisId_img_valide.setVisibility(View.INVISIBLE);
                            korisId_progress.setVisibility(View.INVISIBLE);

                        } else{

                            korisId_img_disable.setVisibility(View.INVISIBLE);
                            korisId_img_valide.setVisibility(View.VISIBLE);
                            korisId_progress.setVisibility(View.INVISIBLE);

                            mPurseId_error.setVisibility(View.GONE);
                            mPurseId_error.setText("");

                        }

                        break;

                    case 4:

                        String resultatValidation = ClasseUtilitaire.mot_de_passe_validation(RegistrationActivity.getInstance().getApplicationContext(),
                                contenu, MDP_MIN_LENGTH);
                        view_errors[which] = !ClasseUtilitaire.isEmptyAndBlank(resultatValidation);
                        are_empties[which] = ClasseUtilitaire.isEmptyAndBlank(contenu);

                        if(are_empties[which]){

                            mMdp_error.setVisibility(View.VISIBLE);
                            mMdp_error.setText(RegistrationActivity.getInstance().getString(R.string.empty_validation_message, getString(R.string.prompt_password)));

                            mdp_img_valide.setVisibility(View.INVISIBLE);
                            mdp_img_disable.setVisibility(View.VISIBLE);

                        } else if(view_errors[which]){

                            mMdp_error.setVisibility(View.VISIBLE);
                            mMdp_error.setText(resultatValidation);

                            mdp_img_valide.setVisibility(View.INVISIBLE);
                            mdp_img_disable.setVisibility(View.VISIBLE);

                        } else{

                            mMdp_error.setVisibility(View.GONE);
                            mMdp_error.setText("");

                            mdp_img_valide.setVisibility(View.VISIBLE);
                            mdp_img_disable.setVisibility(View.INVISIBLE);

                        }

                        break;

                    case 5:

                        are_empties[which] = ClasseUtilitaire.isEmptyAndBlank(contenu);

                        id_card_progress.setVisibility(View.INVISIBLE);

                        if(are_empties[which]){

                            mId_card_error.setVisibility(View.VISIBLE);
                            mId_card_error.setText(RegistrationActivity.getInstance().getString(R.string.empty_validation_message, getString(R.string.id_card)));

                            id_card_img_disable.setVisibility(View.VISIBLE);
                            id_card_img_valide.setVisibility(View.INVISIBLE);

                        } else{

                            id_card_img_disable.setVisibility(View.INVISIBLE);
                            id_card_img_valide.setVisibility(View.VISIBLE);

                            mId_card_error.setVisibility(View.GONE);
                            mId_card_error.setText("");

                        }

                        break;

                    case 7:
                        view_errors[which-1] = ClasseUtilitaire.checkEditTextLength(contenu, NOM_PRENOM_MIN_LENGTH-1);
                        are_empties[which] = ClasseUtilitaire.isEmptyAndBlank(contenu);

                        if(are_empties[which]){

                                    mVille_error.setVisibility(View.VISIBLE);
                                    mVille_error.setText(RegistrationActivity.getInstance().getString(R.string.empty_validation_message, getString(R.string.ville)));

                                    ville_img_valide.setVisibility(View.INVISIBLE);
                                    ville_img_disable.setVisibility(View.VISIBLE);
                        } else if(view_errors[which-1]){

                                    mVille_error.setVisibility(View.VISIBLE);
                                    mVille_error.setText(RegistrationActivity.getInstance().getString(R.string.miniLength_validation_message, getString(R.string.ville), NOM_PRENOM_MIN_LENGTH - 1));

                                    ville_img_valide.setVisibility(View.INVISIBLE);
                                    ville_img_disable.setVisibility(View.VISIBLE);

                        } else{

                                    mVille_error.setVisibility(View.GONE);
                                    mVille_error.setText("");

                                    ville_img_valide.setVisibility(View.VISIBLE);
                                    ville_img_disable.setVisibility(View.INVISIBLE);
                        }
                        break;

                    case 6:
                        are_empties[which] = charSequence.toString().isEmpty();

                        if(are_empties[which]){

                            dateNaissance_error.setVisibility(View.VISIBLE);
                            dateNaissance_error.setText(RegistrationActivity.getInstance().getString(R.string.empty_validation_message, getString(R.string.date_naissance)));

                            date_naissance_img_valide.setVisibility(View.INVISIBLE);
                            date_naissance_img_disable.setVisibility(View.VISIBLE);

                        } else{

                            dateNaissance_error.setVisibility(View.GONE);
                            dateNaissance_error.setText("");

                            date_naissance_img_valide.setVisibility(View.VISIBLE);
                            date_naissance_img_disable.setVisibility(View.INVISIBLE);

                        }

                        break;
                    default:
                        are_empties[which] = ClasseUtilitaire.isEmptyAndBlank(contenu);
                        break;
                }

                viewValidity();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private View.OnFocusChangeListener getFocusChangeListenerForOnlineTasks(final int which){
        return (new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, boolean b) {
                final String contenu = ((EditText)view).getText().toString().trim();

                if(b){

                    switch (which){
                        case 3:

                            invalid_phone_format = ClasseUtilitaire.checkPhoneValidation(contenu);

                            if(invalid_phone_format) {
                                if (!is_phone_not_unique) {
                                    korisId_img_disable.setVisibility(View.VISIBLE);
                                    korisId_img_valide.setVisibility(View.INVISIBLE);
                                    korisId_progress.setVisibility(View.INVISIBLE);
                                } else {
                                    korisId_img_disable.setVisibility(View.INVISIBLE);
                                    korisId_img_valide.setVisibility(View.VISIBLE);
                                    korisId_progress.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                if (is_phone_not_unique) {
                                    korisId_img_disable.setVisibility(View.VISIBLE);
                                    korisId_img_valide.setVisibility(View.INVISIBLE);
                                    korisId_progress.setVisibility(View.INVISIBLE);
                                } else {
                                    korisId_img_disable.setVisibility(View.INVISIBLE);
                                    korisId_img_valide.setVisibility(View.VISIBLE);
                                    korisId_progress.setVisibility(View.INVISIBLE);
                                }
                            }

                            is_phone_not_unique = false;

                            break;

                        case 5:
                            if(ClasseUtilitaire.isEmptyAndBlank(contenu)) {
                                RegistrationActivity.getInstance().showAlertDialog(-1, R.string.idCard_dialog_title,
                                        R.string.idCard_dialog_msg,
                                        R.mipmap.logo_small, R.string.ok_dialog_cnx_checking, -1).setCancelable(false);
                            }
                            break;
                    }
                }


                if(!b){

                    switch (which){
                        case 3:
                            if(!invalid_phone_format) {
                                korisId_img_disable.setVisibility(View.INVISIBLE);
                                korisId_img_valide.setVisibility(View.INVISIBLE);
                                korisId_progress.setVisibility(View.VISIBLE);
                                //if(contenu not empty)
                                final RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.getInstance().getApplicationContext());
                                String url = "https://torepofficiel.com/web/main_controlleur.php/verify/utilisateur/unique/numero/" + contenu;

                                final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        requestQueue.stop();
                                        connection_error = false;

                                    /*Toast.makeText(PlaceholderFragment.this.getActivity(),
                                                response, Toast.LENGTH_LONG).show();*/

                                        if (response.equals("\"1\"")) {
                                            is_phone_not_unique = false;
                                            korisId_img_disable.setVisibility(View.INVISIBLE);
                                            korisId_img_valide.setVisibility(View.VISIBLE);
                                            korisId_progress.setVisibility(View.INVISIBLE);

                                        } else {
                                            is_phone_not_unique = true;
                                            korisId_img_disable.setVisibility(View.VISIBLE);
                                            korisId_img_valide.setVisibility(View.INVISIBLE);
                                            korisId_progress.setVisibility(View.INVISIBLE);

                                        }

                                        invalid_phone_format = ClasseUtilitaire.checkPhoneValidation(contenu);
                                        view_errors[which] = invalid_phone_format || is_phone_not_unique;
                                        are_empties[which] = ClasseUtilitaire.isEmptyAndBlank(contenu);

                                        if (are_empties[which]) {

                                            mPurseId_error.setVisibility(View.VISIBLE);
                                            mPurseId_error.setText(RegistrationActivity.getInstance().getString(R.string.empty_validation_message, getString(R.string.koris_id)));

                                        } else if (invalid_phone_format) {

                                            mPurseId_error.setVisibility(View.VISIBLE);
                                            mPurseId_error.setText(RegistrationActivity.getInstance().getString(R.string.error_invalid_koris_id));

                                        } else if (is_phone_not_unique) {

                                            mPurseId_error.setVisibility(View.VISIBLE);
                                            mPurseId_error.setText(RegistrationActivity.getInstance().getString(R.string.phone_not_unique));

                                        } else {

                                            mPurseId_error.setVisibility(View.GONE);
                                            mPurseId_error.setText("");

                                        }

                                        viewValidity();

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        requestQueue.stop();

                                        ((EditText) view).setText("");
                                        mPurseId_error.setVisibility(View.VISIBLE);
                                        mPurseId_error.setText(RegistrationActivity.getInstance().getString(R.string.connexion_suspendue));

                                        korisId_img_disable.setVisibility(View.VISIBLE);
                                        korisId_img_valide.setVisibility(View.INVISIBLE);
                                        korisId_progress.setVisibility(View.INVISIBLE);

                                        connection_error = true;

                                    }
                                });

                                new android.os.Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        requestQueue.add(stringRequest);
                                    }
                                });

                            }
                            break;

                        case 5:
                            if(!ClasseUtilitaire.isEmptyAndBlank(contenu)) {
                                id_card_img_disable.setVisibility(View.INVISIBLE);
                                id_card_img_valide.setVisibility(View.INVISIBLE);
                                id_card_progress.setVisibility(View.VISIBLE);
                                //if(contenu not empty)
                                final RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.getInstance().getApplicationContext());
                                String url = "https://torepofficiel.com/web/main_controlleur.php/verify/utilisateur/unique/id_card/" + contenu.toLowerCase();

                                final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        requestQueue.stop();
                                        connection_error = false;
                                        id_card_progress.setVisibility(View.INVISIBLE);
                                        String numeroUtilisateurSiExiste = "";

                                    /*Toast.makeText(PlaceholderFragment.this.getActivity(),
                                                response, Toast.LENGTH_LONG).show();*/

                                        if (response.equals("\"1\"")) {
                                            is_id_card_not_unique = false;
                                            id_card_img_disable.setVisibility(View.INVISIBLE);
                                            id_card_img_valide.setVisibility(View.VISIBLE);

                                        } else {
                                            is_id_card_not_unique = true;
                                            id_card_img_disable.setVisibility(View.VISIBLE);
                                            id_card_img_valide.setVisibility(View.INVISIBLE);
                                            numeroUtilisateurSiExiste = response.substring( 1, response.length() - 1 );

                                        }

                                        view_errors[which] = is_id_card_not_unique;
                                        are_empties[which] = ClasseUtilitaire.isEmptyAndBlank(contenu);

                                        if (are_empties[which]) {

                                            mId_card_error.setVisibility(View.VISIBLE);
                                            mId_card_error.setText(RegistrationActivity.getInstance().getString(R.string.empty_validation_message, getString(R.string.id_card)));

                                        } else if (is_id_card_not_unique) {

                                            mId_card_error.setVisibility(View.VISIBLE);
                                            mId_card_error.setText(RegistrationActivity.getInstance().getString(R.string.id_card_not_unique, numeroUtilisateurSiExiste));

                                        } else {

                                            mId_card_error.setVisibility(View.GONE);
                                            mId_card_error.setText("");

                                        }

                                        viewValidity();

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        requestQueue.stop();

                                        ((EditText) view).setText("");
                                        mId_card_error.setVisibility(View.VISIBLE);
                                        mId_card_error.setText(RegistrationActivity.getInstance().getString(R.string.connexion_suspendue));

                                        id_card_img_disable.setVisibility(View.VISIBLE);
                                        id_card_img_valide.setVisibility(View.INVISIBLE);
                                        id_card_progress.setVisibility(View.INVISIBLE);

                                        connection_error = true;

                                    }
                                });

                                new android.os.Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        requestQueue.add(stringRequest);
                                    }
                                });

                            }
                            break;
                    }
                }
            }
        });
    }

    public static MyAlertDialog getAlert() {
        return (alert != null) ? alert.get() : null;
    }
}
