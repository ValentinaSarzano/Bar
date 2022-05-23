package it.polito.tdp.bar.model;

public class Model {

	private Simulator sim;
	
	private Model() {
		this.sim = new Simulator();
	}
	
	public void simula() {
		sim.inizializza();
		sim.run();
	}
}
