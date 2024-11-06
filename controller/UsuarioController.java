package controller;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import beans.Usuario;
import connection.DBConnection;

public class UsuarioController implements IUsuarioController{

	@Override
	public String login(String username, String contrasena) {
		Gson gson = new Gson();
		
		DBConnection con = new DBConnection();
		
		String sql = "SELECT * FROM usuarios WHERE username= '" + username + "' AND contrasena= '" + contrasena + "'";
		
		try {
			Statement st = con.getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while (rs.next()) {
	            // Si las credenciales son correctas, extraemos los datos
	            String nombre = rs.getString("nombre");
	            String apellidos = rs.getString("apellidos");
	            String email = rs.getString("email");
	            double saldo = rs.getDouble("saldo");
	            boolean premium = rs.getBoolean("premium");
	            
	            Usuario usuario = new Usuario(username, contrasena, nombre, apellidos, email, saldo, premium);
	            return gson.toJson(usuario);  // Devolvemos el usuario en formato JSON
	        }
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			con.desconectar();
		}
		
		return "false";
	}

	@Override
	public String register(String username, String contrasena, String nombre, String apellidos, String email,
			double saldo, boolean premium) {
		Gson gson = new Gson();
		
		DBConnection con = new DBConnection();
		String sql = "INSERT INTO usuarios VALUES('" + username + "', '" + contrasena + "', '"+ nombre + "', '"+ apellidos +"', '"+ email + "',"+ saldo +", "+ premium +")";
		
		try {
			Statement st = con.getConnection().createStatement();
			st.execute(sql);
			
			Usuario usuario = new Usuario(username, contrasena, nombre, apellidos, email, saldo, premium);
			
			st.close();
			
			return gson.toJson(usuario);
		}catch(Exception ex) {
			ex.printStackTrace();
			return "false";
		}finally {
			con.desconectar();
		}
	}

	@Override
	public String pedir(String username) {
		Gson gson = new Gson();
		DBConnection con = new DBConnection();
		String sql = "SELECT * FROM usuarios WHERE username = '" + username + "'";
		
		try {
			Statement st = con.getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()) {
				String contrasena = rs.getString("contrasena");
				String nombre = rs.getString("nombre");
				String apellidos = rs.getString("apellidos");
				String email = rs.getString("email");
				double saldo = rs.getDouble("saldo");
				boolean premium = rs.getBoolean("premium");
				
				Usuario usuario = new Usuario(username, contrasena, nombre, apellidos, email, saldo, premium);
				return gson.toJson(usuario);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return "false";
		}
		finally {
			con.desconectar();
		}
		
		return "false";
	}

	@Override
	public String restarDinero(String username, double nuevoSaldo) {
		
		DBConnection con = new DBConnection();
		String sql = "UPDATE usuarios SET saldo = " + nuevoSaldo + "WHERE username = '" + username + "'" ;
		
		try {
			Statement st = con.getConnection().createStatement();
			st.executeQuery(sql);
			
			return "true";
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			con.desconectar();
		}
		return "false";
	}

	@Override
	public String modificar(String username, String nuevaContrasena, String nuevoNombre, String nuevosApellidos,
			String nuevoEmail, double nuevoSaldo, boolean nuevoPremium) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		String sql = "UPDATE usuarios SET contrasena = '" + nuevaContrasena + "', nombre =  '" + nuevoNombre +
					  "', apellidos = '" + nuevosApellidos + "', email = '" + nuevoEmail + "', saldo = " + nuevoSaldo + ", premium = ";
		
		if (nuevoPremium == true) {
			sql += " 1 "; 
		}else {
			sql += " 0 ";
		}
		
		sql += " WHERE username = '" + username + "'";
		
		try{
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

	@Override
	public String verCopias(String username) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		String sql = "SELECT id, COUNT(*) AS num_copias FROM alquiler WHERE username = '" + username + "' GROUP BY id;";
		
		Map<Integer, Integer> copias = new HashMap<Integer, Integer>();
		
		try {
			Statement st = con.getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()) {
				int id = rs.getInt("id");
				int num_copias = rs.getInt("num_copias");
				
				copias.put(id, num_copias);
			}
			
			devolverLibros(username, copias);
			
			return "true";
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			con.desconectar();
		}
		return "false";
	}

	@Override
	public String devolverLibros(String username, Map<Integer, Integer> copias) {
		DBConnection con = new DBConnection();
		
		
		try {
			
			for (Map.Entry<Integer, Integer>libro: copias.entrySet()) {
				
				int id = libro.getKey();
				int num_copias = libro.getValue();
				
				String sql = "UPDATE libros SET copias = (SELECT copias + " + num_copias + " FROM libros WHERE id= " + id + ") WHERE id = " + id;
				Statement st = con.getConnection().createStatement();
				st.executeUpdate(sql);
			}
			
			this.eliminar(username);
			
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			con.desconectar();
		}
		
		return "false";
	}

	@Override
	public String eliminar(String username) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		
		String sql1 = "DELETE FROM alquiler WHERE username = '" + username + "'";
		String sql2 = "DELETE FROM usuarios WHERE username = '" + username + "'";
		
		try {
			Statement st = con.getConnection().createStatement();
			st.executeUpdate(sql1);
			st.executeUpdate(sql2);
			
			return "true";
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			con.desconectar();
		}
		
		return "false";
	}
}
