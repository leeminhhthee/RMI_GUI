package client;

import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import my_interface.IServer;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	IServer chatServer;

	public Login(IServer chatServer) {
		this.chatServer = chatServer;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 340, 153);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(103, 82, 89, 23);
		btnLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					login();
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(btnLogin);
		
		textField = new JTextField();
		textField.setBounds(152, 43, 126, 23);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Nickname: ");
		lblNewLabel.setBounds(82, 43, 77, 23);
		contentPane.add(lblNewLabel);
		
		this.setVisible(true);
		this.setTitle("Login");
	}

	protected void login() throws HeadlessException, RemoteException {
		// check nick name
		String nickName = textField.getText().toString();
		
		if (chatServer.checkClientExits(nickName)) {
			JOptionPane.showMessageDialog(null, nickName + " đã tồn tại");
		} else {
			ClientImp clientImp = new ClientImp(chatServer, nickName);	
			dispose();
		}
		
	}
}
