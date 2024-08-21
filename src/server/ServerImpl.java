package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import my_interface.Client;
import my_interface.IClient;
import my_interface.IServer;

public class ServerImpl extends UnicastRemoteObject implements IServer {
	
	
	private ServerView serverView;
	public List<Client> listClient = new ArrayList<Client>();
	
	static String name = "";
	
	protected ServerImpl(ServerView serverView) throws RemoteException {
		this.serverView = serverView;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void unicastMessage(String msg) throws RemoteException {
		System.out.println("Running on server....: " + msg);
		name += msg + "\n";
		this.serverView.addUser(name);
	}
	
	@Override
	public void registerClient(Client client) throws RemoteException {
		
		if (client == null) {
			System.out.println("Client is null");
		}
		
		for (Client client2 : listClient) {
			client2.getiClient().addClient(client);
		}
		listClient.add(client);
		System.out.println("register client " + name);
		notifyClient(client);
		// return list còn lại
	}
	
	private void notifyClient(Client client) throws RemoteException {
		Vector<String> clientName = new Vector<>();
		for (Client string : listClient) {
			clientName.add(string.getName());
		}
		List<Client> updateList = new ArrayList<Client>(listClient);
		updateList.remove(client);
		client.getiClient().updateOnlineClients(updateList);
	}

	@Override
	public List<Client> getOnlineClients() throws RemoteException {
		return listClient;
	}

	@Override
	public boolean checkClientExits(String nickName) throws RemoteException {
		for (Client client : listClient) {
			if (client.getName().equals(nickName)) return true;
		}
		return false;
	}

	@Override
	public void addClient(Client client) throws RemoteException {
	}

	@Override
	public void removeClient(String name) throws RemoteException {
		// TODO Auto-generated method stub
		for (Client client : listClient) {
			if (client.getName().equals(name)) {
				listClient.remove(client);
				break;
			}
		}
		serverView.textArea.append(name + " client offline...\n");
	}

}
