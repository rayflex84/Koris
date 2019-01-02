package com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires;

import com.torepofficiel.carem.koris.com.torepofficiel.carem.exceptions.ValeurChampsIncorrecteException;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


@SuppressWarnings("serial")
public class Calendrier extends Date {
	
	//Declaration des Attributs
	private byte m_jour, m_mois;
	private short m_annee;

	/**
	 * 
	 * @param jour
	 * @param mois
	 * @param annee
	 * @throws ValeurChampsIncorrecteException
	 */
	
	public Calendrier(byte jour, byte mois, short annee) throws ValeurChampsIncorrecteException {
		setAnnee(annee); setMois(mois); setJour(jour);
		if(!isValide()) throw new ValeurChampsIncorrecteException(this);
	}

	public Calendrier(int jour, int mois, int annee) throws ValeurChampsIncorrecteException {
		setAnnee(annee); setMois((byte)mois); setJour((byte) jour);
	}
	
	/**
	 * 
	 * @param date La date sous forme de chaine de caracteres au format jj/mm/aaaa
	 * @throws ValeurChampsIncorrecteException 
	 */
	
	public Calendrier(String date) throws ValeurChampsIncorrecteException {
		
		byte jour, mois; jour=mois=0; short annee=0, tabSplitDate[] = splitStringDate(date);
		jour=(byte)tabSplitDate[0]; mois=(byte)tabSplitDate[1];
		annee=tabSplitDate[2];
		
		setAnnee(annee); setMois(mois); setJour(jour);
		
		if(!isValide()) throw new ValeurChampsIncorrecteException(this);
	}
	
	
	
	//Getters et Setters
	public byte getJour() {
		return m_jour;
	}

	public void setJour(byte jour) throws ValeurChampsIncorrecteException {
		
		if(jour < 1) throw new ValeurChampsIncorrecteException(jour);
		if (isBissextile(getAnnee()) && getMois() == 2 ) {
			if(jour > 29) {
				System.out.println(getAnnee() + " est une année bissextile\n");
				throw new ValeurChampsIncorrecteException(jour);
			}
		} else if(getMois() ==2) {
			if(jour > 28) {
				System.out.println(getAnnee() + " est une année non bissextile\n");
				throw new ValeurChampsIncorrecteException(jour);
			}
		}
		
		if(getMois() < 8){
			if (getMois() % 2 == 0 && jour > 30 ) {
				System.out.println("Le mois : "+ getLitteralMois(getMois())+ " se termine toujours par le 30\n");
				throw new ValeurChampsIncorrecteException(jour);
			} else if(jour > 31){
				System.out.println("Le mois de "+ getLitteralMois(getMois())+ " se termine toujours par le 31\n");
				throw new ValeurChampsIncorrecteException(jour);
			}
		
		} else {
			if (getMois() % 2 != 0 && jour > 30 ) {
				System.out.println("Le mois de "+ getLitteralMois(getMois())+ " se termine toujours par le 30\n");
				throw new ValeurChampsIncorrecteException(jour);
			} else if(jour > 31){
				System.out.println("Le mois : "+ getLitteralMois(getMois())+ " se termine toujours par le 31\n");
				throw new ValeurChampsIncorrecteException(jour);
			}
		}
			m_jour = jour;
	}

	public byte getMois() {
		return m_mois;
	}

	public void setMois(byte mois) throws ValeurChampsIncorrecteException {
		
		if(mois < 1 || mois > 12) throw new ValeurChampsIncorrecteException(mois, "Mois");
		
		m_mois = mois;
	}

	public short getAnnee() {
		return m_annee;
	}

	public void setAnnee(short annee) throws ValeurChampsIncorrecteException {
		
		int currentYear = getCurrentYear();
		if(annee < 1901 || annee > currentYear) {
			System.out.println("l'année doit être comprise entre 1901 (inclue) et "+currentYear+" (inclue).");
			throw new ValeurChampsIncorrecteException(annee, "Année");
		}
		
		m_annee = annee;
	}

	public void setAnnee(int annee) throws ValeurChampsIncorrecteException {

		int currentYear = getCurrentYear();
		if(annee < 1901) {
			System.out.println("l'année doit être supérieur à 1901");
			throw new ValeurChampsIncorrecteException(annee, "Année");
		}

		m_annee = (short) annee;
	}

	
	@Override
	public int compareTo(Date anotherDate) {
		
		if(anotherDate == null || !(anotherDate instanceof Calendrier)) return 1;
		
		Calendrier another = (Calendrier) anotherDate;
		
		if (getAnnee() != another.getAnnee()) {
			if (getAnnee() < another.getAnnee()) return -3;
			else return 3;
		} else if (getMois() != another.getMois()) {
			if (getMois() < another.getMois()) return -2;
			else return 2;
		}else if (getJour() != another.getJour()) {
			if (getJour() < another.getJour()) return -1;
			else return 1;
		}
		
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj == null || !(obj instanceof Calendrier)) return false;
		else if(compareTo((Calendrier)obj) == 0) return true;
		
		return false; 
		
	}
	/**
	 * Get the current year
	 * @return the current year
	 */
	public static int getCurrentYear() {

		Calendar now = GregorianCalendar.getInstance();
		
		return now.get(Calendar.YEAR);
	}
	
	/**
	 * Get the current month
	 * @return the current month
	 */
	public static int getCurrentMonth() {

		Calendar now = GregorianCalendar.getInstance();

		return now.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * Get the current day
	 * @return the current day
	 */
	public static int getCurrentDay() {

		Calendar now = GregorianCalendar.getInstance();

		return now.get(Calendar.DAY_OF_MONTH);
	}
	/**
	 * Verifie si la date de l'instance actuelle est anterieure a la date actuelle
	 * @return true si la date est anterieure a la date actuelle, false si non
	 */
	public boolean isValide() {
		
		int currentMonth = getCurrentMonth();
		if(getAnnee() == getCurrentYear()) {
			if(getMois() > currentMonth) return false;
			else {
				if (getMois() == currentMonth && getJour() > getCurrentDay()) return false;
			}
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		
		String jour = (m_jour == 1) ? "1er" : ""+m_jour;
		String dayOfWeek = "";
		
		try {
			dayOfWeek = day_of_week(m_jour+"/"+m_mois+"/"+m_annee);
		} catch (ValeurChampsIncorrecteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		return dayOfWeek + ", le "+ jour + " " +getLitteralMois(m_mois) +" " + m_annee;
	}
	
	//Static methods about getting the day of week of a given date
	/**
	 * 
	 * @param date au format jj/mm/aaaa
	 * @return retourne le nombre de jours ecoules dans l'annee en parametre
	 * @throws ValeurChampsIncorrecteException 
	 */
	public static int numberDaysInCurrentYear(String date) throws ValeurChampsIncorrecteException{
		
		short tab[] = splitStringDate(date), annee = tab[2] ;
		int moisBis = 0, numberOfDays= 0; byte jour = (byte)tab[0], mois = (byte)tab[1];
		
		if (mois == 1) return jour;
		moisBis = (mois-1)/2; //les calculs doivent se faire sur les mois precedents le mois actuel d'ou m-1
		
		if(mois == 9 || mois == 11) moisBis--;
		numberOfDays = (moisBis * 30) + ((mois-1)-moisBis) * 31 + jour;
		
		return (isBissextile(annee)) ? numberOfDays-(1 *((moisBis > 0 ) ? 1 : 0))  : numberOfDays-(2 *((moisBis > 0 ) ? 1 : 0)) ;
	}
	
	/**
	 * Methode permettant de savoir si une annee est bissextile
	 * @param annee
	 * @return retourne true si annee est bissextile, false si annee n'est pas bissextile
	 */
	public static boolean isBissextile(int annee){
		
		return ((annee % 4 == 0) && (annee % 100 != 0)) | (annee % 400 == 0);
	}
	
	/**
	 * Methode permettant de connaitre le nombre de jours ecoules du 01/01/1901 a la date donnee
	 * @param date au format jj/mm/aaaa
	 * @return le nombre de jours de 1901 a la date donnee
	 * @throws ValeurChampsIncorrecteException 
	 */
	public static int numberOfDays(String date) throws ValeurChampsIncorrecteException{
		
		int numberOfDays = 0; short annee = splitStringDate(date)[2];
		
		for(int i = 1901; i <= annee; i++){
			if(i == annee) numberOfDays += numberDaysInCurrentYear(date);
			else numberOfDays += numberDaysInCurrentYear("31/12/"+i);
		}
		
		return numberOfDays;
	}
	
	/**
	 * Methode permettant de trouver le jour litteral de la semaine par rapport a une date donnee
	 * @param date au format jj/mm/aaaa
	 * @return le jour litteral d'une date donnee
	 * @throws ValeurChampsIncorrecteException 
	 */
	public static String day_of_week(String date) throws ValeurChampsIncorrecteException {
	
		String days[] = {"Lundi","Mardi","Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
		int numberDays = numberOfDays(date), day = numberDays % 7;
		
		return days[day];
	}
	/**
	 * Methode retournant le mois en toutes lettres correspondant au mois en parametre
	 * @param mois
	 * @return le mois en chaine litterale du mois correspondant
	 */
	public static String getLitteralMois(byte mois){
		
		String[] months = {"Janv", "Fev", "Mars","Avr","Mai","Juin","Juil","Aout","Sept","Oct","Nov","Dec"};
		
		return months[mois-1];
	}
	
	/**
	 * 
	 * @param date au format jj/mm/aaaa
	 * @return Un tableau de short contenant le jour, le mois et l'annee de la date en parametre
	 * @throws ValeurChampsIncorrecteException
	 */
	private static short[] splitStringDate(String date) throws ValeurChampsIncorrecteException{
		
		short[] dateTab = new short[3];
		
		if(date != null && !date.isEmpty()){
		
			dateTab[0]=Short.parseShort(date.split("/")[0]); dateTab[1]=Short.parseShort(date.split("/")[1]);
			dateTab[2]=Short.parseShort(date.split("/")[2]);
		}
		else throw new ValeurChampsIncorrecteException("La valeur du champ Date Naissance est soit nulle ou vide");
		
		return dateTab;
	}

	public static String timestampInLitteral(Timestamp timestamp){
		String[] dateSplitted = timestamp.toString().split("-");
		int year = Short.parseShort(dateSplitted[0]);
		int month = Byte.parseByte(dateSplitted[1]);
		String[] timePart = dateSplitted[2].split(" ");
		int day = Byte.parseByte(timePart[0]);
		String time = timePart[1].substring(0,8);
		String h = time.split(":")[0];
		String m = time.split(":")[1];
		String s = time.split(":")[2];


		Calendrier calendrier=null;
		try {
			calendrier = new Calendrier(day, month,
					year);
		} catch (ValeurChampsIncorrecteException e) {
			e.printStackTrace();
		}

		return day + " " +getLitteralMois((byte)month) +" " + year + ", à " + h + "h " + m + "min " + s + "sec.";
	}

	public static int getNumberOfDaysInMonth(int month){
		int number;
		switch(month){
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				number = 31;
				break;
			case 2:
				number = (isBissextile(getCurrentYear())) ? 29 : 28;
				break;

			default:
				number = 30;
				break;
		}

		return number;
	}

}
