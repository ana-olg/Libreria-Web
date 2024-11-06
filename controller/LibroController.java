package controller;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

import beans.Libro;
import connection.DBConnection;

public class LibroController implements ILibroController{

	@Override
	public String listar(boolean ordenar, String orden) {
		
		Gson gson = new Gson();
		
		DBConnection con = new DBConnection();
		String sql = "SELECT * FROM libros";
		
		if (ordenar == true) {
			sql += " ORDER BY genero " + orden;
		}
		
		List<String> libros = new ArrayList<String>();
		
		try {
			Statement st = con.getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while (rs.next()) {
				int id = rs.getInt("id");
				String titulo = rs.getString("titulo");
				String genero = rs.getString("genero");
				String autor = rs.getString("autor");
				int copias = rs.getInt("copias");
				boolean novedad = rs.getBoolean("novedad");
				
				Libro libro = new Libro(id, titulo, genero, autor, copias, novedad);
				
				libros.add(gson.toJson(libro));
			}
					
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			con.desconectar();
		}
		return gson.toJson(libros);
	}

	@Override
	public String alquilar(int id, String username) {
		
		Timestamp fecha = new Timestamp(new Date().getTime());
		
		DBConnection con = new DBConnection();
		String sql = "INSERT INTO alquiler VALUES ('" + id + "', '" + username + "', '" + fecha + "')";
		
		try {
			Statement st = con.getConnection().createStatement();
			st.execute(sql);
			
			String modificar = modificar(id);
			if(modificar == "true") {
				return "true";
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			con.desconectar();
		}
		return "false";
	}

	@Override
	public String modificar(int id) {
		DBConnection con = new DBConnection();
		String sql = "UPDATE libros SET copias = (copias-1) WHERE id = " + id;
				
		try {
			Statement st = con.getConnection().createStatement();
			st.execute(sql);
			
			return "true";
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			
		}
		
		return "false";
	}

	@Override
	public String devolver(int id, String username) {
		// TODO Auto-generated method stub
		
		DBConnection con = new DBConnection(); 
		String sql = "DELETE FROM alquiler WHERE id= " + id + " AND username = '" + username + "' LIMIT 1";
		
		try {
			Statement st = con.getConnection().createStatement();
			//ExecuteUpdate para consultas tipo DELETE, INSERT o UPDATE
			st.executeUpdate(sql);
			
			this.sumarCantidad(id);
			
			return "true";
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			con.desconectar();
		}
		return "false";
	}

	@Override
	public String sumarCantidad(int id) {
		
		DBConnection con = new DBConnection();
		
		String sql = "UPDATE libros SET copias = copias + 1 WHERE id = " + id;
		
		try {
			
			Statement st = con.getConnection().createStatement();
			st.executeUpdate(sql);
			
			return "true";
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			con.desconectar();
		}
		
		return "false";
	}

}
