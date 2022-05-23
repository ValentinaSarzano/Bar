package it.polito.tdp.bar.model;

import java.time.Duration;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.bar.model.Event.EventType;

public class Simulator {

	//Modello
	private List<Tavolo> tavoli;
	
	//Parametri della simulazione
	private int numEventi = 2000; //Dobbiamo generare a caso 2000 eventi di persone che arrivano al bar
	private int tArrivoMax = 10; //Arrivato un gruppo, sappiamo che tra 1 e max 10 min arriverà un altro gruppo
	private int numPersoneMax = 10; //Per ogni evento (arrivano/se ne vanno al max 10 persone)
	private int durataMin = 60;
	private int durataMax = 120;
	private double tolleranzaMax = 0.9; 
	private double occupazioneMin = 0.5; //Far accomodare i clienti in modo da occupare almento il 50% dei posti disponibili del tavolo
	
	//Coda degli eventi
	private PriorityQueue<Event> queue;
	
	//Parametri di output (Statistiche)
	private Statistiche statistiche;
	
	private void creaTavolo(int qta, int dimensione) {
		for(int i =0; i < qta; i++) {
			this.tavoli.add(new Tavolo(dimensione, false));
		}
	}
	public void creaTavoli() {
		creaTavolo(2, 10);
		creaTavolo(4, 8);
		creaTavolo(4, 6);
		creaTavolo(5, 4);
		
		//Potrebbe essere utile ordinare questi tavoli per dimensione
		Collections.sort(this.tavoli, new Comparator<Tavolo>() {

			@Override
			public int compare(Tavolo o1, Tavolo o2) {
				return o1.getPosti()-o2.getPosti();
			}
			
		});
	}
	
	public void creaEventi() {
		//supponiamo di partire dall'istante 0 e andare avanti
		Duration tArrivo = Duration.ofMinutes(0); //Tempo di arrivo, potevo anche scirverlo, se avessi usato int: int tArrivo = 0;
	    for(int i=0; i < this.numEventi; i++) {
	    	//Devo tirare a caso: numero persone, durata, tolleranza
	    	int nPersone = (int) (Math.random() * this.numPersoneMax + 1); //+1 perchè voglio andare da 1 a 10 (non da 0 a 9, math.random() mi da un numero da 0 a 0.99999)
	    	Duration durata = Duration.ofMinutes(this.durataMin +          //Trasformo durataMin/durataMax e operazioni in Duration
	    			(int) (Math.random() * (this.durataMax - this.durataMin + 1))); 
	    	double tolleranza = Math.random()+this.tolleranzaMax;
	    	//Creo un nuovo evento, lo aggiungo alla coda, scifto avanti il tempo di un numero a caso tra 1 e 10
	    	Event e = new Event(EventType.ARRIVO_GRUPPO_CLIENTI, tArrivo, nPersone, durata, tolleranza, null); //il riferimento al tavolo è null perchè non ho ancora assegnato nessun tavolo a questo gruppo
	    	this.queue.add(e);
	    	tArrivo = tArrivo.plusMinutes((int) (Math.random()* this.tArrivoMax + 1)); //tempo di arrivo del prossimo gruppo
	    }
	
	}
	
	public void inizializza() { 
		//Creo la coda degli eventi, le statistiche, i tavoli e gli eventi
		this.queue = new PriorityQueue<Event>();
		this.statistiche = new Statistiche();
		creaTavoli();
		creaEventi();
	}
	
	public void run() {
		while(!this.queue.isEmpty()) { //Finchè la coda non è vuota
			Event e = queue.poll(); //prendo un evento dalla coda
		    processaEvento(e); //e lo eseguo
		}
		
	}
	
	private void processaEvento(Event e) {
		//Quando ho più tipologie di evento la prima cosa da fare è uno switch sulle varie tipologie di evento 
		switch(e.getType()) {
		case ARRIVO_GRUPPO_CLIENTI:
			//Conto i clienti totali
			this.statistiche.incrementaClienti(e.getnPersone());
			//Cerco un tavolo
			Tavolo tavolo = null;
			for(Tavolo t: this.tavoli) {
				if(!t.isOccupato() && t.getPosti() >= e.getnPersone() && (t.getPosti()* this.occupazioneMin <= e.getnPersone())) {
					tavolo = t;
					break;
				}
			}
			if(tavolo != null) {
				System.out.println("Trovato un tavolo da "+ tavolo.getPosti() + " per "+ e.getnPersone() + " persone.\n");
				statistiche.incrementaSoddisfatti(e.getnPersone());
			    tavolo.setOccupato(true);
			    e.setTavolo(tavolo);
			    //Dopo un po' i clienti si alzeranno --> aggiungo nella coda un evento di tipo TAVOLO_LIBERATO
			    queue.add(new Event(EventType.TAVOLO_LIBERATO, e.getTime().plus(e.getDurata()), e.getnPersone(), e.getDurata(), e.getTolleranza(), tavolo));
			}else { //C'è solo il bancone, tavolo non disponibile
				double bancone = Math.random();
				if(bancone <= e.getTolleranza()) {//I clienti accettano di fermarsi al bancone
					System.out.println(e.getnPersone() + " si fermano al bancone.");
					statistiche.incrementaSoddisfatti(e.getnPersone());
				}else { //I clienti accettano di fermarsi al bancone
					System.out.println(e.getnPersone() + " sono rimaste insoddisfatte e se ne vanno a casa.");
					statistiche.incrementaInsoddisfatti(e.getnPersone());
				}
			}
			break;
		case TAVOLO_LIBERATO:
			e.getTavolo().setOccupato(false); //clienti se ne vanno, prendo il mio tavolo e lo imposto come libero (occupato == false)
			break;
		}
		
	}
	
	public void getStatistiche() {
		//TODO 
	}
	
}
