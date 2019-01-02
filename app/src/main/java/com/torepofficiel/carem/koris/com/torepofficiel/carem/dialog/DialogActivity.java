package com.torepofficiel.carem.koris.com.torepofficiel.carem.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

enum Dialog_Type {
    dialog_fragment, alert_dialog;
}

public abstract class DialogActivity extends AppCompatActivity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	protected MyAlertDialog showDialogType(int v, Dialog_Type type, int dialog_fragment_title, int message, int icone, int positive_button, int negative_button, int layout_resId){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		
		if(prev != null){
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		MyAlertDialog newFragment = null;
		switch(type){
			case dialog_fragment:
				/*newFragment = MyDialogFragment
								.newInstance(v, dialog_fragment_title, layout_resId);*/
				break;
			case alert_dialog:
				if(v != -1)
					newFragment = MyAlertDialog
								.newInstance(v, dialog_fragment_title, icone, positive_button, negative_button);
				else
					newFragment = MyAlertDialog
							.newInstance(-1, dialog_fragment_title, message, icone, positive_button, negative_button);
				break;
		}
		newFragment.show(ft, "dialog");
		return newFragment;
	}

	protected MyAlertDialog showDialogType(int v, Dialog_Type type, String dialog_fragment_title, String message, int icone, String positive_button, String negative_button, int layout_resId){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");

		if(prev != null){
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		MyAlertDialog newFragment = null;
		switch(type){
			case alert_dialog:
				if(v != -1) {
					newFragment = MyAlertDialog
							.newInstance(v, dialog_fragment_title, icone, positive_button, negative_button);
				}
				else
					newFragment = MyAlertDialog
							.newInstance(dialog_fragment_title, message, icone, positive_button, negative_button);
				break;
		}
		newFragment.show(ft, "dialog");
		return newFragment;
	}
	
	public MyAlertDialog showDialogFragment(int v, int fragment_title_ResId, int layout_resId){
		return showDialogType(v, Dialog_Type.dialog_fragment, fragment_title_ResId, 0, 0, 0, 0, layout_resId);
	}
	
	public MyAlertDialog showAlertDialog(int v, int fragment_title_ResId, int message, int icone, int positive_button, int negative_button){
		return showDialogType(v, Dialog_Type.alert_dialog, fragment_title_ResId, message, icone, positive_button, negative_button, 0);
	}

	public MyAlertDialog showAlertDialog(int v, String fragment_title_ResId, String message, int icone, String positive_button, String negative_button){
		return showDialogType(v, Dialog_Type.alert_dialog, fragment_title_ResId, message, icone, positive_button, negative_button, 0);
	}
	
	public abstract void doPositiveClick(DialogInterface dialog, int id);
		

	
	public abstract void doNegativeClick();
}