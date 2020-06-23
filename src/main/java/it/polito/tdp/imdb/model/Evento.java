package it.polito.tdp.imdb.model;

public class Evento implements Comparable<Evento>{

	public enum EventType{
		INTERVISTA, RIPOSO
	}
		
	private Integer giorno;
	private Actor attore;
	private EventType type;
	
	
	public EventType getType() {
		return type;
	}



	public void setType(EventType type) {
		this.type = type;
	}



	public Integer getGiorno() {
		return giorno;
	}



	public void setGiorno(Integer giorno) {
		this.giorno = giorno;
	}



	public Actor getAttore() {
		return attore;
	}



	public void setAttore(Actor attore) {
		this.attore = attore;
	}



	public Evento(Integer giorno, Actor attore, EventType type) {
		super();
		this.giorno = giorno;
		this.attore = attore;
		this.type = type;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attore == null) ? 0 : attore.hashCode());
		result = prime * result + ((giorno == null) ? 0 : giorno.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Evento other = (Evento) obj;
		if (attore == null) {
			if (other.attore != null)
				return false;
		} else if (!attore.equals(other.attore))
			return false;
		if (giorno == null) {
			if (other.giorno != null)
				return false;
		} else if (!giorno.equals(other.giorno))
			return false;
		return true;
	}


	public int compareTo(Evento other) {
		// TODO Auto-generated method stub
		return this.giorno.compareTo(other.giorno);
	}



	@Override
	public String toString() {
		switch(this.type) {
		case INTERVISTA:
			
			return this.giorno+". INTERVISTA-"+this.attore.toString()+"("+this.attore.getGender()+")";
			
		case RIPOSO:
			return this.giorno+". RIPOSO";
		}
		return "!!!problema!!!";
	}

	
}
