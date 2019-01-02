package com.torepofficiel.carem.koris.com.torepofficiel.carem.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.torepofficiel.carem.koris.BienvenueActivity;
import com.torepofficiel.carem.koris.KorisActivity;
import com.torepofficiel.carem.koris.LoginActivity;
import com.torepofficiel.carem.koris.R;
import com.torepofficiel.carem.koris.RegistrationActivity;
import com.torepofficiel.carem.koris.TabFragment2;

import static com.torepofficiel.carem.koris.RegistrationActivity.annee_naissance_text;

public class MyAlertDialog extends DialogFragment {

	private static int r_layout;
	public static int choix;
	public static int choixNegatif;

	private ViewGroup mContainer;
	
	public static MyAlertDialog newInstance(int v, int title, int message, int icone, int positive_button, int negative_button){
		MyAlertDialog dialog = new MyAlertDialog();
		r_layout = -1;
		Bundle arg = new Bundle();
		arg.putInt("title", title);
		arg.putInt("message", message);
		arg.putInt("icone", icone);
		arg.putInt("positive_button", positive_button);
		arg.putInt("negative_button", negative_button);
		arg.putBoolean("isRawString", false);
		arg.putBoolean("isView", false);
		dialog.setArguments(arg);
		return dialog;
	}

	public static MyAlertDialog newInstance(String title, String message, int icone, String positive_button, String negative_button){
		if(title == null)
			title = "";
		if(positive_button == null)
			positive_button = "";
		if(message == null)
			message = "";
		if( negative_button == null)
			negative_button = "";

		r_layout = -1;
		MyAlertDialog dialog = new MyAlertDialog();
		Bundle arg = new Bundle();
		arg.putString("title", title);
		arg.putString("message", message);
		arg.putBoolean("isRawString", true);
		arg.putBoolean("isView", false);
		arg.putInt("icone", icone);
		arg.putString("positive_button", positive_button);
		arg.putString("negative_button", negative_button);
		dialog.setArguments(arg);
		return dialog;
	}

	public static MyAlertDialog newInstance(int v, int title, int icone, int positive_button, int negative_button){
		MyAlertDialog dialog = new MyAlertDialog();
		r_layout = v;
		Bundle arg = new Bundle();
		arg.putBoolean("isRawString", false);
		arg.putBoolean("isView", true);
		arg.putInt("title", title);
		arg.putInt("icone", icone);
		arg.putInt("positive_button", positive_button);
		arg.putInt("negative_button", negative_button);
		dialog.setArguments(arg);
		return dialog;
	}

	public static MyAlertDialog newInstance(int v, String title, int icone, String positive_button, String negative_button){
		if(title == null)
			title = "";
		if(positive_button == null)
			positive_button = "";
		if( negative_button == null)
			negative_button = "";

		MyAlertDialog dialog = new MyAlertDialog();
		r_layout = v;
		Bundle arg = new Bundle();
		arg.putBoolean("isRawString", true);
		arg.putBoolean("isView", true);
		arg.putString("title", title);
		arg.putInt("icone", icone);
		arg.putString("positive_button", positive_button);
		arg.putString("negative_button", negative_button);
		dialog.setArguments(arg);
		return dialog;
	}

	@NonNull
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		int icone = getArguments().getInt("icone");

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		if(icone != -1) builder.setIcon(icone);
		boolean isRawString = getArguments().getBoolean("isRawString");
		boolean isView = getArguments().getBoolean("isView");
		if(isRawString){
			String title = getArguments().getString("title");
			String negative_button = getArguments().getString("negative_button");
			String positive_button = getArguments().getString("positive_button");

			if(isView){
				if(title != null && !title.isEmpty()) builder.setTitle(title);
				if(positive_button != null && !positive_button.equals("")) builder.setPositiveButton(positive_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						quelChoixPositif(dialog, which);
					}
				});

				if(negative_button != null && !negative_button.equals("")) builder.setNegativeButton(negative_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						quelChoixNegatif();
					}
				});
				builder.setView(getmLayout(LayoutInflater.from(getContext()), mContainer));

			} else {
				String message = getArguments().getString("message");
				builder.setTitle(title)
						.setMessage(message);
				if(positive_button != null && !positive_button.equals("")) builder.setPositiveButton(positive_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						quelChoixPositif(dialog, which);
					}
				});

				if(negative_button != null && !negative_button.equals("")) builder.setNegativeButton(negative_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						quelChoixNegatif();
					}
				});
			}

		} else {
			int title = getArguments().getInt("title");
			int negative_button = getArguments().getInt("negative_button");
			int positive_button = getArguments().getInt("positive_button");

			if(isView){
				if(title != -1) builder.setTitle(title);

				if(positive_button != -1) builder.setPositiveButton(positive_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						quelChoixPositif(dialog, which);
					}
				});

				if(negative_button != -1) builder.setNegativeButton(negative_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						quelChoixNegatif();
					}
				});

				builder.setView(getmLayout(LayoutInflater.from(getContext()), mContainer));

			} else {
				int message = getArguments().getInt("message");
				builder.setTitle(title)
						.setMessage(message);
				if(positive_button != -1) builder.setPositiveButton(positive_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						quelChoixPositif(dialog, which);
					}
				});

				if(negative_button != -1) builder.setNegativeButton(negative_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						quelChoixNegatif();
					}
				});
			}
		}

		return builder.create();
	}

	private static void quelChoixPositif(DialogInterface dialog, int id){
        boolean case2Reached = false;
		switch (choix){
			case 1:

				if(BienvenueActivity.getInstance() != null) {
					BienvenueActivity.getInstance().doPositiveClick(dialog, id);
				}

				if(LoginActivity.getInstance() != null) {
					LoginActivity.getInstance().doPositiveClick(dialog, id);
				}

				if(KorisActivity.getInstance() != null) {
					KorisActivity.getInstance().doPositiveClick(dialog, id);
				}
				break;

			case 2:
                if(RegistrationActivity.getInstance() != null) {
                    RegistrationActivity.getInstance().doPositiveClick(dialog,id);
                }
                case2Reached = true;
                break;
			case 3:
                case2Reached = false;
				if(RegistrationActivity.getInstance() != null) {
					RegistrationActivity.getInstance().doPositiveClick(dialog,id);
				}
				break;

			case 4:
				if(TabFragment2.getInstance() != null) {
					TabFragment2.getInstance().pay_abonnement_local_account();
				}
				break;

			case 5:
				if(TabFragment2.getInstance() != null) {
					TabFragment2.getInstance().pay_abonnement_online_account();
				}
				break;
		}

		if(!case2Reached)
		    choix = 0;

	}

	private static void quelChoixNegatif(){
		switch (choixNegatif){
			case 1:
				if(TabFragment2.getInstance() != null)
					TabFragment2.getInstance().switchViews(1);
				break;

			case 2:

				break;
			case 3:

				break;

			case 4:

				break;
		}

		choixNegatif = 0;

	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if(r_layout != -1) {
			mContainer = container;
			if (!getShowsDialog()) {
                return getmLayout(inflater, container);
            }
		}
	return super.onCreateView(inflater,container,savedInstanceState);
	}

	public View getmLayout(LayoutInflater inflater, ViewGroup container){
		View viewGroup = inflater.inflate(r_layout, container,false);

        AppCompatEditText mLayout = viewGroup.findViewById(R.id.naissance_edittext);

		if(AppCompatEditText.class.isAssignableFrom(mLayout.getClass())){
            annee_naissance_text = "";

			mLayout.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
					annee_naissance_text = charSequence.toString().trim();
				}

				@Override
				public void afterTextChanged(Editable editable) {

				}
			});

		}

		return viewGroup;
	}
}