package my_interface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

public interface IClient extends Remote{

	public void retriveMessage(String msg) throws RemoteException;
	
	public void retriveFile(byte[] data, String fileName, String sender) throws RemoteException;
	
	public void retriveAudio(byte[] data, String sender, String audioKey) throws RemoteException;
	
	public void registerClient(IClient client) throws RemoteException;
	
	public void updateOnlineClients(List<Client> onlineClients) throws RemoteException;
	
	public void addClient(Client client) throws RemoteException;
	
	public boolean connectChatter(String name) throws RemoteException;
	
	public void createGroup(String groupName, List<Client> listClients) throws RemoteException;
	
	public void removeClient(String clientName) throws RemoteException;
}
