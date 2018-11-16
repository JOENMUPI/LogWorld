package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.Calendar;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.json.JSONObject;

@MultipartConfig
@WebServlet("/ServletMain")
public class ServletMain extends HttpServlet {
	private WebSI db;
	private static final String objectName = "Connection";
	private static final long serialVersionUID = 1L;
    
	public ServletMain() { super(); }
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	this.db = Service.create(new URL("http://127.0.0.1:9000/kuku?wsdl"), new QName("http://LogWorld/", "WebSService")).getPort(new QName("http://LogWorld/","WebSPort"), WebSI.class);
    	JSONObject json = new JSONObject();
		HttpSession session = request.getSession();
		JSONObject reqBody = new JSONObject(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
		
		switch(reqBody.getString("methodName")) {
			case "Login":
				this.loginPost(session, reqBody, json, response);
				break;
			case "ChangePass":
				this.changePass(session, reqBody, json, response);
				break;
			case "Register":
				this.registerPost(session, reqBody, json, response);
				break;	
			case "Logout":
				this.logoutPost(session, json, response);
				break;	
		}
    }	
    
    private void logoutPost(HttpSession session, JSONObject json, HttpServletResponse response) throws IOException {
    	InetAddress address = InetAddress.getLocalHost();
    	
    	if(session.isNew()) { 
    		db.sendLog(getTime(), address.getHostAddress(), objectName, "ERROR", session.getAttribute("email").toString(), "logout", "the User " + session.getAttribute("email").toString()+" ni login ha hecho pues...");
			json.put("status", "401").put("response", "You're not logged in");
			System.out.println("Not logged --");
			session.invalidate();
		}
		
		else {
			db.sendLog(getTime(), address.getHostAddress(), objectName, "info", session.getAttribute("email").toString(), "logout", "the User " + session.getAttribute("email").toString()+" is logout");			json.put("status","200").put("response","You have logged out");
			System.out.println("Logout --");
			session.invalidate();
		}
		
		response.getWriter().print(json.toString());
    }
    
    
    private void changePass(HttpSession session, JSONObject reqBody, JSONObject json, HttpServletResponse response) throws IOException {
    	InetAddress address = InetAddress.getLocalHost();
    	
    	if(!session.isNew()) {	
			if(session.getAttribute("password").equals(reqBody.getString("oldPass"))){
				if(db.changePass(session.getAttribute("email").toString(), reqBody.getString("oldPass"), reqBody.getString("newPass"))) {
					db.sendLog(getTime(), address.getHostAddress(), objectName, "info", session.getAttribute("email").toString(), reqBody.getString("methodName"), "the pass is updated "+reqBody.getString("oldPass")+" for "+reqBody.getString("newPass"));
					json.put("status", "200").put("response", "your pass is updated");
					System.out.println("------------------------------------------------------------");
					System.out.println("User-> " + session.getAttribute("email").toString());
					session.setAttribute("password", reqBody.getString("newPass"));
				}
				
				else {
					db.sendLog(getTime(), address.getHostAddress(), objectName, "ERROR", session.getAttribute("email").toString(), reqBody.getString("methodName"), "internal error on register");
					json.put("response", "your pass is not updated, internal error").put("status", "400");
					System.out.println("internal error");
				}
			}
			
			else {
				db.sendLog(getTime(), address.getHostAddress(), objectName, "ERROR", session.getAttribute("email").toString(), reqBody.getString("methodName"), "error oldpass");
				json.put("response", "your pass is not updated, oldPass error").put("status", "400");
				System.out.println("error oldpass --");
			}
    	}

		else {
			db.sendLog(getTime(), address.getHostAddress(), objectName, "ERROR", session.getAttribute("email").toString(), reqBody.getString("methodName"), "the user isn't login");
			json.put("response", "your pass is not updated, you're not login").put("status", "400");
			System.out.println("no login --");
		}

		response.getWriter().println(json.toString()); 
    }
    
	private void registerPost(HttpSession session, JSONObject reqBody, JSONObject json, HttpServletResponse response) throws IOException {
		InetAddress address = InetAddress.getLocalHost();
		
		if(!db.login(reqBody.getString("email"), reqBody.getString("password"))) {
	    	db.register(reqBody.getString("email"), reqBody.getString("password"));
	    	json.put("status", "200").put("response", "signup finished");
	    	db.sendLog(getTime(), address.getHostAddress(), objectName, "info", reqBody.getString("email"), reqBody.getString("methodName"), "the User " + reqBody.getString("email")+" are regitrated");
	    	System.out.println("Register --");
	    	this.storeValue(reqBody.getString("email"), reqBody.getString("password"), session);
	    	db.sendLog(getTime(), address.getHostAddress(), objectName, "info", reqBody.getString("email"), reqBody.getString("methodName"), " login the User " + reqBody.getString("email"));
			System.out.println("------------------------------------------------------------");
			System.out.println("User-> " + reqBody.getString("email"));
		}
		
		else {
			db.sendLog(getTime(), address.getHostAddress(), objectName, "ERROR", reqBody.getString("email"), reqBody.getString("methodName"), " we couldn't register " + reqBody.getString("email") + ", email in use");
	    	json.put("status", "400").put("response", "email already used");
	    	System.out.println("Fail register --");
			session.invalidate();
		}
		
		response.getWriter().println(json.toString()); 
	}
    
    private void loginPost(HttpSession session, JSONObject reqBody, JSONObject json, HttpServletResponse response) throws IOException {
    	InetAddress address = InetAddress.getLocalHost();
    	if(session.isNew()) {
			if(db.login(reqBody.getString("email"), reqBody.getString("password"))) {
				this.storeValue(reqBody.getString("email"), reqBody.getString("password"), session);
				json.put("status", "200").put("response", reqBody.getString("email"));
				db.sendLog(getTime(), address.getHostAddress(), objectName, "info", reqBody.getString("email"), reqBody.getString("methodName"), " login the User " + reqBody.getString("email"));
				System.out.println("------------------------------------------------------------");
				System.out.println("User-> " + reqBody.getString("email"));
			}
	
			else {
				db.sendLog(getTime(), address.getHostAddress(), objectName, "ERROR", reqBody.getString("email"), reqBody.getString("methodName"), " Wrong email" + reqBody.getString("email") + " with password " + reqBody.getString("password"));
				json.put("response", "Wrong email or password").put("status", "400");
				session.invalidate();
				System.out.println("Wrong data --");
			}
		}

		else {
			db.sendLog(getTime(), address.getHostAddress(), objectName, "info", reqBody.getString("email"), reqBody.getString("methodName"), " the User " + reqBody.getString("email")+"Already logged");
			json.put("response", "you're logged in").put("status", "400");
			System.out.println("Already log --");
		}

		response.getWriter().println(json.toString()); 
	}	
    
    private String getTime() {
    	return Calendar.getInstance().get(Calendar.YEAR)+"-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+" "+Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+":"+Calendar.getInstance().get(Calendar.MINUTE)+":"+Calendar.getInstance().get(Calendar.SECOND)+".00";
    }
    
	private void storeValue(String email, String password, HttpSession session) {
		if(email == null) {
			session.setAttribute("email", "");
			session.setAttribute("password", "");
		} 
		
		else {
			session.setAttribute("email", email);
			session.setAttribute("password", password);
		}
	}
}