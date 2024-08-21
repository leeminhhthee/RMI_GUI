package my_interface;

import java.io.Serializable;
import java.util.List;

public class Room implements Serializable {
	private String roomName;
	private List<Client> listClients;
	
	
	public Room(String roomName, List<Client> listClients) {
		this.roomName = roomName;
		this.listClients = listClients;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public List<Client> getListClients() {
		return listClients;
	}
	public void setListClients(List<Client> listClients) {
		this.listClients = listClients;
	}
	
	
}
