package it.polito.tdp.meteo.model;

import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private MeteoDAO meteo;
	
	private List<Citta> leCitta;
	private List<Citta> migliore;
	private List<Rilevamento> listRilevamenti;

	private int costoMin;

	public Model() {
		meteo= new MeteoDAO();
		this.leCitta= meteo.getAllCitta();
	}

	public List<Rilevamento> getAllRilevamenti() {
		return this.meteo.getAllRilevamenti();
	}

	// of course you can change the String output with what you think works best
	public int getUmiditaMedia(int mese, String localita) {
		return this.meteo.getMediaUmiditaLocalitaMese(mese, localita);
	}

	
	

	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		
		List<Citta> parziale = new LinkedList<Citta>();
		this.migliore= null;
		
		for(Citta c: leCitta) {
			c.setRilevamenti(meteo.getListRilevamentoLocalitaMese(c.getNome(), mese));
		}
		
		cerca(parziale, 0);

		return migliore;
	}

	private void cerca(List<Citta> parziale, int livello) {
		
		if(livello==this.NUMERO_GIORNI_TOTALI) {
			double costo= calcolaCosto(parziale);
			
			if(migliore== null || costo<calcolaCosto(migliore)) {
				migliore= new LinkedList<Citta>(parziale);
			}
			
		}
		else {
			for (Citta prova: leCitta) {
				if(aggiuntaValida(parziale, prova)) {
					parziale.add(prova);
					cerca(parziale, livello+1);
					parziale.remove(parziale.size()-1);
				}
			}

		}
		
	}

	private boolean aggiuntaValida(List<Citta> parziale, Citta prova) {
		int conta=0;
		for(Citta precedente: parziale) {
			if(precedente.equals(prova))
				conta+=1;
		}
		if(conta>=this.NUMERO_GIORNI_CITTA_MAX)
			return false;
		
		if(parziale.size()==0)
			return true;
		
		if(parziale.size()==1 || parziale.size()==2) {
			return parziale.get(parziale.size()-1).equals(prova);
		}
		if(parziale.get(parziale.size()-1).equals(prova))
			return true;
		if(parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) && parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3)))
			return true;
	
		return false;
	}

	private double calcolaCosto(List<Citta> parziale) {
		double costo=0.0;
		
		for(int giorno=1; giorno<=this.NUMERO_GIORNI_TOTALI; giorno++) {
			Citta c= parziale.get(giorno-1);
			double umidita= c.getRilevamenti().get(giorno-1).getUmidita();
			
			costo+=umidita;
		}
		
		for(int giorno=2;giorno<=this.NUMERO_GIORNI_TOTALI; giorno++) {
			if(!parziale.get(giorno-1).equals(parziale.get(giorno-2))) {
				costo += COST;
			}
		}
		
		
		return costo;
	}

	

}



