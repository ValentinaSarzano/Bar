package it.polito.tdp.bar.model;

import java.time.Duration;

public class Event implements Comparable<Event>{

	//Eventi: 1) clienti arrivano
	//        2) clienti se ne vanno
	
	public enum EventType{
		ARRIVO_GRUPPO_CLIENTI,
		TAVOLO_LIBERATO
	}
	
	private EventType type;
	private Duration time; //Evito di lavorare con le date assumento che il nostro tempo sia un numero intero o una duration (E' UGUALE) che va avanti
	private int nPersone;
	private Duration durata;
	private double tolleranza; //E' una probabilità, ci sono quasi sempre nelle simulazioni
	private Tavolo tavolo;
	
	
	public Event(EventType type, Duration time, int nPersone, Duration durata, double tolleranza, Tavolo tavolo) {
		super();
		this.type = type;
		this.time = time;
		this.nPersone = nPersone;
		this.durata = durata;
		this.tolleranza = tolleranza;
		this.tavolo = tavolo;
	}


	public EventType getType() {
		return type;
	}


	public void setType(EventType type) {
		this.type = type;
	}


	public Duration getTime() {
		return time;
	}


	public void setTime(Duration time) {
		this.time = time;
	}


	public int getnPersone() {
		return nPersone;
	}


	public void setnPersone(int nPersone) {
		this.nPersone = nPersone;
	}


	public Duration getDurata() {
		return durata;
	}


	public void setDurata(Duration durata) {
		this.durata = durata;
	}


	public double getTolleranza() {
		return tolleranza;
	}


	public void setTolleranza(double tolleranza) {
		this.tolleranza = tolleranza;
	}


	public Tavolo getTavolo() {
		return tavolo;
	}


	public void setTavolo(Tavolo tavolo) {
		this.tavolo = tavolo;
	}

	//HashCode e equals non servono poichè non capita quasi mai che andiamo a confontare due eventi

	//Proprio perchè usiamo una coda prioritaria di eventi ci servirà un concetto di ordinamento

	@Override
	public int compareTo(Event o) {
		// ordino per il tempo
		return this.time.compareTo(o.getTime()); //avendo usato la classe Duration possiamo usare il metodo compareto già implementato in questa classe dalla libreria java.time
	}
	

}
