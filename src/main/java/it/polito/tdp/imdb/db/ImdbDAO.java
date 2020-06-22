package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Arco;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public void listAllActors(Map<Integer, Actor> actorsMap){
		String sql = "SELECT * FROM actors";

		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!actorsMap.containsKey(res.getInt("id"))) {
					Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
							res.getString("gender"));
					
					actorsMap.put(actor.getId(), actor);
				}
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getAllGeneri() {
		String sql = "SELECT g.genre AS genere " + 
				"FROM directors_genres g " + 
				"GROUP BY g.genre ";
		
		List<String> result = new ArrayList<String>();
		
		Connection conn = DBConnect.getConnection();
		

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("genere"));
			}
			conn.close();
			return result;
			
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
				}
	}

	public List<Actor> getVertici(String gScelto, Map<Integer, Actor> actorsMap) {
		String sql = "SELECT DISTINCT r.actor_id as id " + 
				"FROM roles r, movies m, movies_genres mg " + 
				"WHERE r.movie_id = m.id and mg.movie_id = m.id and mg.genre = ? ";
		
		Connection conn = DBConnect.getConnection();

		List<Actor> result = new ArrayList<>();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, gScelto);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(actorsMap.containsKey(res.getInt("id"))) {
					result.add(actorsMap.get(res.getInt("id")));
				}
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}		
	}

	
	public List<Arco> getAllArchi(String gScelto, Map<Integer, Actor> actorsMap) {
		String sql = "SELECT r1.actor_id as a1, r2.actor_id as a2, COUNT(DISTINCT r1.movie_id) as peso " + 
				"FROM roles r1, roles r2, movies_genres mg " + 
				"WHERE r1.actor_id > r2.actor_id AND " + 
				"r1.movie_id = r2.movie_id AND " + 
				"mg.movie_id = r1.movie_id AND " + 
				"mg.genre = ? " + 
				"GROUP BY r1.actor_id, r2.actor_id";
		
		List<Arco> result = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();
		

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, gScelto);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(actorsMap.containsKey(res.getInt("a1")) && actorsMap.containsKey(res.getInt("a2"))) {
					result.add(new Arco(actorsMap.get(res.getInt("a1")), actorsMap.get(res.getInt("a2")), res.getInt("peso")));
				}
			}
			conn.close();
			return result;
			
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
				}
	}

	
}
