package client;

import java.io.FileInputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import javax.crypto.Cipher;

import my_interface.Client;
import my_interface.IClient;
import my_interface.IServer;
import my_interface.Room;

public class ClientImp implements IClient, Serializable {
	/**
	 * 
	 */

	String myName;

	public IClient client;

	ClientView clientView;
	List<Client> onlineClients = new ArrayList<Client>();
	IServer chatServer;

	List<Client> chattersClients = new ArrayList<Client>();
	List<Room> listRooms = new ArrayList<>();

	Map<String, Map<String, byte[]>> listFileContents = new HashMap<String, Map<String, byte[]>>();
	Map<String, Map<String, byte[]>> listAudios = new HashMap<String, Map<String, byte[]>>();

	public ClientImp(IServer chatServer, String myName) throws RemoteException {
		clientView = new ClientView(this, myName);
		this.chatServer = chatServer;
		this.myName = myName;
		UnicastRemoteObject.exportObject(this, 0);
		Client client = new Client(myName, this);
		this.chatServer.registerClient(client);
		this.chatServer.unicastMessage(myName + " is connected");
	}

	// đăng ký client để nhắn tín
	@Override
	public void registerClient(IClient client) throws RemoteException {
		this.client = client;
	}

	@Override
	public void retriveMessage(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("retrive: " + msg);
		if (msg.startsWith("[")) {
			System.out.println("recive message: " + msg);
			String groupName = msg.substring(1, msg.indexOf("]"));
			String nameSender = msg.substring(msg.indexOf("]") + 1, msg.indexOf(":"));
			String mess = deCrypt(msg.substring(msg.indexOf(":") + 1, msg.length()));
			this.clientView.setMessage(groupName, nameSender + ": " + mess);
		} else {
			String nameSender = msg.substring(0, msg.indexOf(":"));
			String mess = deCrypt(msg.substring(msg.indexOf(":") + 1, msg.length()));
			this.clientView.setMessage(nameSender, nameSender + ": " + mess);
		}
	}

	@Override
	public void updateOnlineClients(List<Client> onlineClients) throws RemoteException {
		this.onlineClients = onlineClients;
		Vector<String> listNames = new Vector<>();

		for (Client object : onlineClients) {
			listNames.add(object.getName());
		}
		System.out.println(listNames.toString());
		this.clientView.listChatterOnline.setListData(listNames);
	}

	// add ban đầu
	public boolean addChatter(String name) throws RemoteException {
		// TODO Auto-generated method stub
		Client clientSelected = null;
		for (Client client : onlineClients) {
			if (name.equals(client.getName())) {
				clientSelected = client;
				break;
			}
		}
		IClient iClient = clientSelected.getiClient();
		boolean a = iClient.connectChatter(myName);
		if (!a)
			return false;
		chattersClients.add(clientSelected);
		// lấy

		return true;
	}

	public boolean addChatter(String name, int i) throws RemoteException {
		Client clientSelected = null;
		for (Client client : onlineClients) {
			if (name.equals(client.getName())) {
				clientSelected = client;
				break;
			}
		}
		chattersClients.add(clientSelected);
		return true;
	}

	public boolean isChatter(String name) {
		for (Client client : chattersClients) {
			if (name.equals(client.getName())) {
				return true;
			}
		}
		return false;
	}

	public void sendMessage(String nameReciver, String nameSender, String msg) throws RemoteException {
		// check is client or group
		if (isChatter(nameReciver)) {
			// send to chatter
			Client client = null;
			for (Client item : chattersClients) {
				if (item.getName().equals(nameReciver)) {
					client = item;
					break;
				}
			}
			String mess = nameSender + ":" + enCrypt(msg);
			System.out.println(mess);
			client.getiClient().retriveMessage(mess);
		} else {
			// send to group
			Room room = null;
			for (Room item : listRooms) {
				if (item.getRoomName().equals(nameReciver)) {
					room = item;
					break;
				}
			}

			String message = "[" + nameReciver + "]" + nameSender + ":" + enCrypt(msg);
			System.out.println(message);
			for (Client item : room.getListClients()) {
				Client client = getChatterByName(item.getName());
				System.out.println(onlineClients.indexOf(client));
				client.getiClient().retriveMessage(message);
			}
		}
	}

	// send file
	public void sendFile(String nameReciver, byte[] data, String fileName) throws RemoteException {

		if (isChatter(nameReciver)) {
			// send to chatter
			Client client = getChatterByName(nameReciver);
			client.getiClient().retriveFile(data, fileName, myName);

		} else {
			// send to group

		}
	}

	@Override
	public boolean connectChatter(String nameChatter) throws RemoteException {
		if (isChatter(nameChatter))
			return true;
		// thông báo muốn coonect
		return this.clientView.notifyNewChatter(nameChatter);
	}

	public void openCreateGroupView() {
		new GroupView(onlineClients, this);
	}

	public void addGroup(String roomName, List<String> listNames) throws RemoteException {
		List<Client> listClients = new ArrayList<Client>();

		for (String nameChatter : listNames) {
			Client chatter = getChatterByName(nameChatter);
			if (chatter != null)
				listClients.add(chatter);
		}

		Room room = new Room(roomName, listClients);

		listRooms.add(room);

		// update view room
		clientView.updateGroupList(listRooms);
		clientView.insertTabChat(room);

		// update view cho những thằng còn lại

		Client thisClient = new Client(myName, this);

		for (Client client : listClients) {
			IClient iClient = client.getiClient();
			List<Client> newList = new ArrayList<Client>(listClients);
			newList.remove(client);
			newList.add(thisClient);
			iClient.createGroup(roomName, newList);
		}
	}

	private Client getChatterByName(String nameChatter) {
		for (Client client : onlineClients) {
			if (client.getName().equals(nameChatter))
				return client;
		}
		return null;
	}

	// tạo group từ xa
	@Override
	public void createGroup(String roomName, List<Client> listClients) throws RemoteException {
		// TODO Auto-generated method stub
		Room room = new Room(roomName, listClients);
		listRooms.add(room);
		// update view room
		clientView.updateGroupList(listRooms);
		clientView.insertTabChat(room);
	}

	@Override
	public void addClient(Client client) throws RemoteException {
		onlineClients.add(client);
		Vector<String> listNames = new Vector<>();
		for (Client object : onlineClients) {
			listNames.add(object.getName());
		}
		this.clientView.listChatterOnline.setListData(listNames);
	}

	@Override
	public void retriveFile(byte[] data, String fileName, String sender) throws RemoteException {

		Map<String, byte[]> dataFile = new HashMap<String, byte[]>();
		dataFile.put(fileName, data);

		this.listFileContents.put(sender, dataFile);

		// set into ui
		this.clientView.setMessageFile(sender, fileName);

	}

	public void sendAudio(byte[] dataAudio, String nameReciver) throws RemoteException {
		String audioKey = generateRandomString(5);

		if (isChatter(nameReciver)) {
			// send to chatter
			Client client = getChatterByName(nameReciver);
			client.getiClient().retriveAudio(dataAudio, myName, audioKey);

		} else {
			// send to group

		}

	}

	@Override
	public void retriveAudio(byte[] data, String sender, String audioKey) throws RemoteException {
		Map<String, byte[]> dataFile = new HashMap<String, byte[]>();
		dataFile.put(audioKey, data);
		System.out.println("Retrive: " + data);
		this.listAudios.put(sender, dataFile);
		this.clientView.setMessageAudio(sender, audioKey);
	}

	public static String generateRandomString(int length) {
		// Ký tự cho phép trong chuỗi ngẫu nhiên
		String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuilder sb = new StringBuilder(length);

		// Tạo chuỗi ngẫu nhiên bằng cách chọn ngẫu nhiên ký tự từ chuỗi allowedChars
		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(allowedChars.length());
			char randomChar = allowedChars.charAt(randomIndex);
			sb.append(randomChar);
		}

		return sb.toString();
	}

	public void close() throws RemoteException {
		for (Client client : onlineClients) {
			if (!client.getName().equals(myName)) {
				client.getiClient().removeClient(myName);
			}
		}
	}

	@Override
	public void removeClient(String clientName) throws RemoteException {
		// TODO Auto-generated method stub
		// xoa khoi danh sach online
		for (Client client : onlineClients) {
			if (client.getName().equals(clientName)) {
				onlineClients.remove(client);
				break;
			}
		}

		// xoa khoi room
		for (Room room : listRooms) {
			for (Client client : room.getListClients()) {
				if (client.getName().equals(clientName)) {
					room.getListClients().remove(client);
					break;
				}
			}
		}

		// update GUI
		Vector<String> listNames = new Vector<>();
		for (Client object : onlineClients) {
			listNames.add(object.getName());
		}
		this.clientView.listChatterOnline.setListData(listNames);

	}

	public String enCrypt(String s) {
		String strEncrypt = null;
		try {
			// �?�?c file chứa public key
			FileInputStream fis = new FileInputStream("E:/ChatRMICode/publicKey.rsa");
			byte[] b = new byte[fis.available()];
			fis.read(b);
			fis.close();
			// Tạo public key
			X509EncodedKeySpec spec = new X509EncodedKeySpec(b);
			KeyFactory factory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = factory.generatePublic(spec);
			// Mã hoá dữ liệu
			Cipher c = Cipher.getInstance("RSA");
			c.init(Cipher.ENCRYPT_MODE, pubKey);
			byte encryptOut[] = c.doFinal(s.getBytes());
			strEncrypt = Base64.getEncoder().encodeToString(encryptOut);
			// System.out.println("Chuỗi sau khi mã hoá: " + strEncrypt);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return strEncrypt;
	}

	public String deCrypt(String s) {
		String strDecrypt = null;
		try {
			// Tạo file chứa private key
			FileInputStream fis = new FileInputStream("E:/ChatRMICode/privateKey.rsa");
			byte[] b = new byte[fis.available()];
			fis.read(b);
			fis.close();
			// Tạo private key
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b);
			KeyFactory factory = KeyFactory.getInstance("RSA");
			PrivateKey priKey = factory.generatePrivate(spec);
			// Giải mã dữ liệu
			Cipher c = Cipher.getInstance("RSA");
			c.init(Cipher.DECRYPT_MODE, priKey);
			byte decryptOut[] = c.doFinal(Base64.getDecoder().decode(s));
			// System.out.println("Chuỗi sau khi giải mã là: " + new String(decryptOut));
			strDecrypt = new String(decryptOut, "UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return strDecrypt;

	}

}