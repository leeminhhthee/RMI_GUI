package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;

import my_interface.Client;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class GroupView extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	
	List<Client> listClients = new ArrayList<Client>();
	ClientImp clientImp ;
	private JList listChatter;
	private JButton btnCreateGroup;
	

	public GroupView(List<Client> listClients, ClientImp clientImp ) {
		
		this.listClients = listClients;
		this.clientImp = clientImp;
		
		Vector<String> listNames = getListName();
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 260, 328);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 236, 227);
		contentPane.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		listChatter = new JList();
		listChatter.setBorder(new TitledBorder(null, "Ch\u1ECDn chatter th\u00EAm v\u00E0o nh\u00F3m", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		listChatter.setListData(listNames);
		listChatter.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		panel.add(listChatter);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 228, 236, 61);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Tên nhóm: ");
		lblNewLabel.setBounds(0, 0, 84, 23);
		panel_1.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(90, -1, 146, 23);
		panel_1.add(textField);
		textField.setColumns(10);
		
		btnCreateGroup = new JButton("Tạo nhóm");
		btnCreateGroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					createGroup();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnCreateGroup.setBounds(55, 30, 113, 23);
		panel_1.add(btnCreateGroup);
		
		this.setVisible(true);
		this.setTitle("Create group");
	}
	
	protected void createGroup() throws RemoteException {
		// TODO Auto-generated method stub
		String groupName = textField.getText().trim();
		
		if (groupName.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Tên nhóm không được trống");
			return;
		}
		
		List<String> listNames = listChatter.getSelectedValuesList();
		
		clientImp.addGroup(groupName, listNames);
		dispose();
	}

	public Vector<String> getListName() {
		Vector<String> listNames = new Vector<String>();
		for (Client item : listClients) {
			listNames.add(item.getName());
		}
		
		return listNames;
	}
	
	public Client getClient(String clientName) {
		Client client = null;
		
		for (Client item : listClients) {
			if (item.getName().equals(clientName)) {
				return item;
			}
		}
		return client;
	}
}
