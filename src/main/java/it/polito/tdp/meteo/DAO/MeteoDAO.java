package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;
import java.util.LinkedList;
public class MeteoDAO {
	
	
	//mirestituisce una lista di Rilevamenti ordinata
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public List<Citta> getAllCitta(){
		String sql = "SELECT UNIQUE(localita) "
				+ "FROM situazione";

		List<Citta> citta = new ArrayList<Citta>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Citta c= new Citta(rs.getString("localita"));
				citta.add(c);				
			}

			conn.close();
			return citta;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public int getMediaUmiditaLocalitaMese(int mese, String localita) {
		String sql = "SELECT AVG(umidita) AS mediaUmidita "
				+ "FROM situazione "
				+ "WHERE MONTH(DATA)=? AND localita=?";

		int media = 0;

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			st.setString(2, localita);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
			
				 media= rs.getInt("mediaUmidita");
			
			}

			conn.close();
			return media;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public List<Rilevamento> getListRilevamentoLocalitaMese(String localita, int mese) {
		
		String sql = "SELECT Localita, Data, Umidita "
				+ "FROM situazione "
				+ "WHERE MONTH(DATA)=? AND Localita=? ";
		
		List<Rilevamento> list= new LinkedList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			st.setString(2, localita);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Rilevamento r= new Rilevamento(rs.getString("localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				list.add(r);
				Citta c= new Citta(localita);
				c.setRilevamenti(list);
				
			
			}

			conn.close();
			return list;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


}
