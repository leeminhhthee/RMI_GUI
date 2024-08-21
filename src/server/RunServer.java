package server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunServer {
	
	public static void main(String[] args) {
		try {
			
			Registry registry = LocateRegistry.createRegistry(1099);			
			ServerView serverView = new ServerView();
			
			ServerImpl serverImpl = new ServerImpl(serverView);
			
			registry.rebind("server", serverImpl);
			
			System.out.println("Server is running...");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
