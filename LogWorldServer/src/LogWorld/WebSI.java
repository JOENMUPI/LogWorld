package LogWorld;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;


@WebService
@SOAPBinding(style = Style.RPC)
public interface WebSI {
	@WebMethod
	public boolean register(String userName, String pass);
	@WebMethod
	public boolean login(String userName, String pass);
	@WebMethod
	public boolean changePass(String userName, String oldPass, String newPass);
	@WebMethod
	public boolean sendLog(String date, String hostComponent, String typeComponent, String type_msg, String user, String methodName, String params);
	@WebMethod
	public String requestData(String user, String host, String typeC, String typeM, String method);
	@WebMethod
	public String requestLastData();
}