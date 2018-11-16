package LogWorld;

import java.sql.Timestamp;

import javax.jws.WebService;

import DB.DB;
import DB.DataSet;
import DB.Row;
import Utilities.Props;
import Utilities.Txt;
@WebService(endpointInterface = "LogWorld.WebSI")
public class WebS implements WebSI {
	public boolean register(String userName, String pass) {
		return new DB(Props.getPropertiesFile("config", "db")).doInsert("config", "queries", "createProfile", userName, pass).hasNext();
	}
	 
    public boolean login(String userName, String pass) {
    	return new DB(Props.getPropertiesFile("config", "db")).query("config", "queries", "login", userName, pass).hasNext();
    }
	
    public boolean sendLog(String date, String hostComponent, String typeComponent, String type_msg, String user, String methodName, String inf_msg) {
    	new Txt().setLine("["+ date + "] [" + user + "] [" + hostComponent + "] [" + typeComponent + "] [" + type_msg + "] [" + methodName + "]: " + inf_msg);
    	return new DB(Props.getPropertiesFile("config", "db")).doInsert("config", "queries", "registerData", Timestamp.valueOf(date), hostComponent, typeComponent, type_msg, user, methodName, inf_msg).hasNext();
    }
	 
    public boolean changePass(String userName, String oldPass, String newPass) {
    	if(new DB(Props.getPropertiesFile("config", "db")).query("config", "queries", "login", userName, oldPass).hasNext()) {
    		new DB(Props.getPropertiesFile("config", "db")).update("config", "queries", "updatePassWord", newPass, userName, oldPass);
    		return true;
    	} else { return false; }
    }
    
	public String requestData(String user, String host, String typeC, String typeM, String method) {
		String s = "";
		DataSet ds = new DB(Props.getPropertiesFile("config", "db")).query("config", "queries", "takeData", user, user, host, host, typeC, typeC, typeM, typeM, method, method);	 
    	while(ds.hasNext()) {
    		Row r = ds.next();
    		s += "["+ r.getField("log_dat").asString() + "] [" + r.getField("log_usr").asString() + "] [" + r.getField("log_hos_cmp").asString() + "] [" + r.getField("log_typ_cmp").asString() + "] [" + r.getField("log_typ_msg").asString() + "] [" + r.getField("log_met_nam").asString() + "]: " + r.getField("log_inf_msg").asString() + "\n";		
    	}   	
    	System.out.println(s);
		return s;
	}

	public String requestLastData() {
		String s = null;
		DataSet ds = new DB(Props.getPropertiesFile("config", "db")).query("config", "queries", "takeLastData");	
    	while(ds.hasNext()) {
    		Row r = ds.next();
    		s += "\n["+ r.getField("log_dat").asString() + "] [" + r.getField("log_usr").asString() + "] [" + r.getField("log_hos_cmp").asString() + "] [" + r.getField("log_typ_cmp").asString() + "] [" + r.getField("log_typ_msg").asString() + "] [" + r.getField("log_met_nam").asString() + "]: " + r.getField("log_inf_msg").asString();		
    	}
    	return s;
	}
}