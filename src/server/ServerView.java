package server;

import java.awt.EventQueue;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import my_interface.IClient;
import my_interface.IServer;

import javax.swing.JTextArea;

public class ServerView extends JFrame{


	private static final long serialVersionUID = 1L;
	JPanel contentPane;
	JTextArea textArea;
	
	
	public ServerView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(22, 23, 404, 192);
		textArea.setCaret(new DefaultCaret());
		contentPane.add(textArea);
		
		this.setVisible(true);
	}
	
	public void addUser(String msg) {
		textArea.setText(msg);
	}

	
}
