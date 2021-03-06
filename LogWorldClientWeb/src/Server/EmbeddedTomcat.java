package Server;

import javax.servlet.ServletException;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

public class EmbeddedTomcat {
	
	static ServletMain sm = new ServletMain();
	
	public static void main(String[] args) throws LifecycleException, ServletException {
		Tomcat tomcat = new Tomcat();
		Context ctxt = null;
		Connector con = new Connector();
		con.setPort(8080);
		con.setMaxPostSize(10);
		tomcat.setConnector(con);
		ctxt = tomcat.addWebapp("/", System.getProperty("user.dir") + "\\WebContent");
		Tomcat.addServlet(ctxt, "ServletMain", sm);
		ctxt.addServletMappingDecoded("/ServletMain", "ServletMain");
		ctxt.setAllowCasualMultipartParsing(true);	
		tomcat.start();
		tomcat.getServer().await();		
//		Tomcat tomcat = new Tomcat();
//		Service service = tomcat.getService();
//		service.addConnector(getSslConnector());
//		Context ctx = tomcat.addWebapp("/", System.getProperty("user.dir") + "\\WebContent");
//		Tomcat.addServlet(ctx, "ServletMain", sm);
//		ctx.addServletMappingDecoded("/ServletMain", "ServletMain");
//		ctx.setAllowCasualMultipartParsing(true);
//		tomcat.start();
//		tomcat.getServer().await();
	}
	
	private static Connector getSslConnector() {
		Connector con = new Connector();
		con.setPort(8443);
		con.setSecure(true);
		con.setScheme("https");
		con.setAttribute("keyAlias", "mydomain");
		con.setAttribute("keystorePass", "changeit");
		con.setAttribute("keystoreType", "JKS");
		con.setAttribute("keystoreFile", System.getProperty("user.dir") + "\\certificate\\keystore.jks");
		con.setAttribute("clientAuth", "false");
		con.setAttribute("sslProtocol", "TLS");
		con.setAttribute("maxThreads", "200");
		con.setAttribute("protocol", "org.apache.coyote.http11.http11AprProtocol");
		con.setAttribute("SSLEnabled", true);
		con.setAttribute("protocol", "HTTP/1.1");
		return con;		
	}
}