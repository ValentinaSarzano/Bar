package it.polito.tdp.bar.model;

public class Statistiche {

	private int clientiTot;
	private int clientiSoddisfatti;
	private int clientiInsoddisfatti;
	
	public Statistiche() { //Costruttore vuoto perchè quando creiamo le statistiche ancora non conosciamo i risultati
		super();
		//la prima volta che creiamo quato oggetto inizializziamo tutti i campi a 0 e man mano che la simulazione andrà avanti li incrementeremo
		this.clientiTot = 0; 
		this.clientiSoddisfatti = 0;
		this.clientiInsoddisfatti = 0;
	}

	public int getClientiTot() {
		return clientiTot;
	}

	public int getClientiSoddisfatti() {
		return clientiSoddisfatti;
	}

	public int getClientiInsoddisfatti() {
		return clientiInsoddisfatti;
	}

	public void incrementaClienti(int n) {
		this.clientiTot+=n;
	}
	
	public void incrementaSoddisfatti(int n) {
		this.clientiSoddisfatti+=n;
	}
	public void incrementaInsoddisfatti(int n) {
		this.clientiInsoddisfatti+=n;
	}
}
