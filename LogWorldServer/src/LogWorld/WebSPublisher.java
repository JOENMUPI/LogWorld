package LogWorld;

import javax.xml.ws.Endpoint;

public class WebSPublisher {
	public static void main(String[] args) {
		Endpoint.publish("http://127.0.0.1:9000/kuku", new WebS());
	}
}