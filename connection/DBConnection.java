package connection;

import java.sql.Connection;
import java.sql.DriverManager;
/**
 * Esta clase gestiona la conexión a la base de datos.
 */
public class DBConnection {
	static String bd = "libreria_web";
	static String port = "3306";
	private String login = "";
	static String password = "";
	//Cadena de conexión 
	//jdbc:mysql://ip:puerto/bd
	static String url = "jdbc:mariadb://localhost:" + port + "/" + bd;
	
	Connection connection = null; 
	
	/**
     * Constructor que establece la conexión a la base de datos.
     * Utiliza el Driver de MariaDB y la cadena de conexión para conectarse.
     * Imprime un mensaje indicando si la conexión fue exitosa o fallida.
     */
	public DBConnection() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			connection = DriverManager.getConnection(url, login, password);
			
			if (connection == null) {
				System.out.println("La conexión a " + bd + " ha fallado");
			}
			else {
				System.out.println("La conexión a " + bd + " ha sido satisfactoria");
			}
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace(); 
		}
	}
	
	/**
     * Método que devuelve la conexión activa a la base de datos.
     * 
     * @return Connection - El objeto de conexión a la base de datos.
     */
	public Connection getConnection() {
		return connection; 
	}
	
	/**
     * Método que desconecta la conexión a la base de datos.
     * Se establece la conexión como null.
     */
	public void desconectar() {
		connection = null;
	}
}
