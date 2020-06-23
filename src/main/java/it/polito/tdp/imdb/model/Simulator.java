package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.imdb.model.Evento.EventType;


public class Simulator {
	
	private PriorityQueue<Evento> queue;
	
	private Graph<Actor, DefaultWeightedEdge> grafo;
	
	private List<Evento> eventi;
	
	private int gRiposo;
	
	private int n;
	
	private Random r;
	
	private List<Actor> attori;
	
	
	public void init(int n, Graph<Actor, DefaultWeightedEdge> grafo) {
		this.r = new Random();
		this.queue = new PriorityQueue<>();
		this.eventi = new ArrayList<>();
		this.grafo = grafo;

		this.n = n;
	}
	
	public void run() {
		this.gRiposo = 0;
		
		this.attori = new ArrayList<>(this.grafo.vertexSet());
		int random = r.nextInt(this.grafo.vertexSet().size()-1);
		this.queue.add(new Evento(1, attori.get(random), EventType.INTERVISTA));
		
		while(!this.queue.isEmpty()) {
			Evento e = this.queue.poll();
			this.eventi.add(e);
			System.out.println(e);
			processEvent(e);
		}

	}

	
	private void processEvent(Evento e) {

		switch(e.getType()) {
		
		case INTERVISTA:
			if(this.eventi.size() < n) {
				//Posso ancora intervistare
				double prob = Math.random();
				if(prob <= 0.6) {
					//intervisto sempre random
					int random = r.nextInt(this.grafo.vertexSet().size()-1);
					Evento nuovoEvento = new Evento(e.getGiorno()+1, attori.get(random), EventType.INTERVISTA);
					this.queue.add(nuovoEvento);
					this.eventi.add(nuovoEvento);
					//Se per 2 giorni ha inervistato due attori dellos stessos esso, al 90% si ferma
					if(isStessoGenere(e.getAttore(), nuovoEvento.getAttore())) {
						double probPausa = Math.random();
						if(probPausa < 0.9) {
							//creo evento pausa
							Evento eventoPausa = new Evento(nuovoEvento.getGiorno()+1, null, EventType.RIPOSO);
							this.queue.add(eventoPausa);
							this.eventi.add(eventoPausa);
						}
						//else: pazienza, fa da solo
					}
				}
				else if(prob > 0.6) {
					//Chiuedo allo scorso intervistato di darmi il vicino di grado max
					Actor prossimo = getNextActor(e.getAttore());
					Evento nuovoEvento = new Evento(e.getGiorno()+1, prossimo, EventType.INTERVISTA);
					this.queue.add(nuovoEvento);
					this.eventi.add(nuovoEvento);
					if(e.getType().equals(nuovoEvento.getType()) && isStessoGenere(e.getAttore(), nuovoEvento.getAttore())) {
						double probPausa = Math.random();
						if(probPausa < 0.9) {
							//creo evento pausa
							Evento eventoPausa = new Evento(nuovoEvento.getGiorno()+1, null, EventType.RIPOSO);
							this.queue.add(eventoPausa);
							this.eventi.add(eventoPausa);
						}
						//else: pazienza, fa da solo
					}
				}
			}
			break;
			
		case RIPOSO:
			if(this.eventi.size() < n) {
				for(int i = 0; i < 100; i++) {	//per stare sicuri
				int random = r.nextInt(this.grafo.vertexSet().size()-1);
				Actor dopoPausa = attori.get(random);
				if(!this.attori.contains(dopoPausa)) {
					Evento nuovoEvento = new Evento(e.getGiorno()+1, dopoPausa, EventType.INTERVISTA);
					this.queue.add(nuovoEvento);
					this.eventi.add(nuovoEvento);
					//return;
				}
			}
		}
			break;
		}
		
	}

	private Actor getNextActor(Actor attore) {
		Actor best = null;
		for(DefaultWeightedEdge e : this.grafo.edgesOf(attore)) {
			if(best == null) {
				best = Graphs.getOppositeVertex(this.grafo, e, attore);
			}
			else {
				if(this.grafo.getEdgeWeight(e) > this.grafo.getEdgeWeight(this.grafo.getEdge(attore, best))) {
					best = Graphs.getOppositeVertex(this.grafo, e, attore);
				}
			}
		}
		return best;
	}

	public List<Evento> getEventi() {
		return this.eventi;
	}
	
	public boolean isStessoGenere(Actor a1, Actor a2) {
		if(a1.gender.compareTo(a2.gender) == 0) {
			return true;
		}
		return false;
	}
	

}
