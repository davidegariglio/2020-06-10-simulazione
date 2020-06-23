package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private Map<Integer, Actor> actorsMap;
	private Simulator s;
	
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

	public Collection<Actor> getAllAttoriGrafo() {
		return this.grafo.vertexSet();
	}

	public Collection<Actor> getAttoriSimili(final Actor scelto) {
		final List<Actor> attoriConnessi = new ArrayList<>();
		
		BreadthFirstIterator<Actor, DefaultWeightedEdge> it = new BreadthFirstIterator<>(this.grafo, scelto);
		
		it.addTraversalListener(new TraversalListener<Actor, DefaultWeightedEdge>(){

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> e) {

				Actor sorgente = grafo.getEdgeSource(e.getEdge());
				Actor destinazione = grafo.getEdgeTarget(e.getEdge());
				
				if(!attoriConnessi.contains(sorgente) && !sorgente.equals(scelto)) {
					attoriConnessi.add(sorgente);
				}
				
				if(!attoriConnessi.contains(destinazione) && !destinazione.equals(scelto)) {
					attoriConnessi.add(destinazione);
				}

			
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Actor> e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Actor> e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		while(it.hasNext()) {
			it.next();
		}
		
		Collections.sort(attoriConnessi);
		
		return attoriConnessi;
	}

	public void simula(int g) {
		this.s = new Simulator();
		s.init(g, this.grafo);
		s.run();
		
	}

}
