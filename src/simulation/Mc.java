package simulation;

import OSPABA.*;

public class Mc extends IdList
{
	//meta! userInfo="Generated code: do not modify", tag="begin"
	public static final int inicializuj = 1001;
	public static final int prichodZakaznika = 1002;
	public static final int odchodZakaznika = 1003;
	public static final int obsluhaZakaznika = 1004;
	public static final int jeCasObedu = 1005;
	public static final int vydanieListku = 1008;
	public static final int pripravaObjednavky = 1009;
	public static final int platenie = 1010;
	public static final int vyzdvihnutieVelkejObjednavky = 1011;
	public static final int dajPocetMiestVCakarni = 1012;
	public static final int uvolniloSaMiesto = 1014;
	//meta! tag="end"

	// 1..1000 range reserved for user
	public static final int novyZakaznik = 1;
	public static final int koniecInterakcie = 2;
	public static final int koniecDiktovania = 3;
	public static final int koniecPripravyObjednavky = 4;
	public static final int koniecPlatby = 5;
	public static final int koniecVyzdvihnutiaVelkejObjednavky = 6;
}