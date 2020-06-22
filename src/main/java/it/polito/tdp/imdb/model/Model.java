package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private Map<Integer, Actor> actorsMap;
	
	public Model() {
		this.dao = new ImdbDAO ();
		this.actorsMap = new HashMap<>();
		this.dao.listAllActors(this.actorsMap);
	}

	public List<String> getAllGeneri() {
		List<String> result = new ArrayList<>(this.dao.getAllGeneri());
		return result;
	}

	public void creaGrafo(String gScelto) {

		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(gScelto, this.actorsMap));
		System.out.println(this.grafo.vertexSet().size());
		
		for(Arco a : this.dao.getAllArchi(gScelto, actorsMap)) {
			if(this.grafo.getEdge(a.getA1(), a.getA2()) == null) {
				Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			}
		}
	}
	
	public int getVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getArchi() {
		return this.grafo.edgeSet().size();
	}

}
