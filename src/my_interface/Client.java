package my_interface;

import java.io.Serializable;

public class Client implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private IClient iClient;
	
	public Client(String name, IClient iClient) {
		this.name = name;
		this.iClient = iClient;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IClient getiClient() {
		return iClient;
	}

	public void setiClient(IClient iClient) {
		this.iClient = iClient;
	}
	
}
