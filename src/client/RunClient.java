package client;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import my_interface.IServer;

public class RunClient {
	
	public static void main(String[] args) {
		
		try {
			Registry registry = LocateRegistry.getRegistry("localhost", 1099);
			IServer chatServer = (IServer) registry.lookup("server");
			new Login(chatServer);
//			UnicastRemoteObject.exportObject(clientImp, 0);
//			
//			chatServer.registerClient("h3", clientImp);
//			chatServer.unicastMessage("h3 online");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
