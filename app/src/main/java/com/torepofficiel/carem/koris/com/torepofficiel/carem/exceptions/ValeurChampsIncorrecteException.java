package com.torepofficiel.carem.koris.com.torepofficiel.carem.exceptions;

import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.Calendrier;

@SuppressWarnings("serial")
public class ValeurChampsIncorrecteException extends Exception {

	public ValeurChampsIncorrecteException(short valeur, String champs) {
		super("La valeur du champs "+champs+" est incorrecte: " + valeur);
		
	}
	public ValeurChampsIncorrecteException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public ValeurChampsIncorrecteException(short taille) {
		super("La valeur du champs Taille est incorrecte: " + taille + " cm");
	}

	public ValeurChampsIncorrecteException(int numero, String message) {
		super(message + ": " + numero);
	}
	
	public ValeurChampsIncorrecteException(byte valeur, String champ){
		super("La valeur du champs "+ champ+" est incorrecte: " + valeur + ((champ.equals("age"))?" ans":""));
	}

	public ValeurChampsIncorrecteException(byte jour) {
		super("La valeur du champs Jour est incorrecte: " + jour);
		
	}
	
	public ValeurChampsIncorrecteException(Calendrier date) {
		super("La date ne peut être une date dans le futur: "+ date);
		// TODO Auto-generated constructor stub
	}
}
