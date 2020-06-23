/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {
    	txtResult.clear();
    	Actor scelto = this.boxAttore.getValue();
    	txtResult.appendText("Attori simili a "+scelto.toString()+":\n\n");
    	List<Actor> simili = new ArrayList<>(this.model.getAttoriSimili(scelto));
    	for(Actor a : simili) {
    		txtResult.appendText(a.toString()+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String gScelto;
    	
    	if(this.boxGenere.getValue() != null) {
    		//creo grafo seconod genere scelo
    		gScelto = this.boxGenere.getValue();
    		this.model.creaGrafo(gScelto);
    		txtResult.appendText(String.format("GRAFO CREATO CON %d VERTICI E %d ARCHI", this.model.getVertici(), this.model.getArchi()));
    		List<Actor> lista = new ArrayList<>(this.model.getAllAttoriGrafo());
    		Collections.sort(lista);
    		this.boxAttore.getItems().addAll(lista);
    	}
    	else {
    		//Errore, segnalo, ritorno
    		txtResult.appendText("PRIMA DI CREARE IL GRAFO SELEZIONA UN GENERE!");
    		return;
    	}
    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	txtResult.clear();
    	int g;
    	try {
    		g =Integer.parseInt(this.txtGiorni.getText());
    	}catch(NumberFormatException e) {
    		txtResult.appendText("INSERIRE UN NUMERO NEL CAMPO GIORNI!");
    		return;
    	}
    	this.model.simula(g);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxGenere.getItems().addAll(this.model.getAllGeneri());
    }
}
