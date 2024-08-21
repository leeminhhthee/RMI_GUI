package my_interface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IServer extends Remote {
	
	public void unicastMessage(String msg) throws RemoteException;
	
	public void registerClient(Client client) throws RemoteException;
	
	public List<Client> getOnlineClients() throws RemoteException;
	
	public void addClient(Client client) throws RemoteException;
	
	public void removeClient(String name) throws RemoteException;
	
	
	public boolean checkClientExits(String nickName) throws RemoteException;
}
