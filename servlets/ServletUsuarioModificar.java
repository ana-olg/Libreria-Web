package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.UsuarioController;

/**
 * Servlet implementation class ServletUsuarioModificar
 */
@WebServlet("/ServletUsuarioModificar")
public class ServletUsuarioModificar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletUsuarioModificar() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		UsuarioController usuario = new UsuarioController();
		
		String username = request.getParameter("username");
		String contrasena = request.getParameter("contrasena"); 
		String nombre = request.getParameter("nombre");
		String apellidos = request.getParameter("apellidos"); 
		String email = request.getParameter("email"); 
		Double saldo = Double.parseDouble(request.getParameter("saldo")); 
		boolean premium = Boolean.parseBoolean(request.getParameter("premium"));
		
		String usuarioStr = usuario.modificar(username, contrasena, nombre, apellidos, email, saldo, premium);
		
		response.setContentType("text/html;charser-UTF=8");
		PrintWriter out = response.getWriter();
		out.println();
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
