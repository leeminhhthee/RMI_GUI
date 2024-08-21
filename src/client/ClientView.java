package client;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import my_interface.IClient;
import my_interface.Room;

import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JSeparator;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.JLabel;
import javax.crypto.Cipher;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;

public class ClientView extends JFrame {

	public JPanel contentPane;
	public JTextField txtMessage;
	public JList<String> listChatterOnline;
	
	public String message;
	 
	public ClientImp clientImp;
	public String myName;
	private JTabbedPane tabbedPane;
	private JList listGroups;
	private JPanel panel_3;
	private JButton btnCreateGroup;
	private JButton btnEmoji;
	private JButton btnFile;
	
	private boolean isRecording = false;
	private JButton btnAudio;
	private byte audioData[];
	
	
	public ClientView(ClientImp clientImp, String myName) {
		this.clientImp = clientImp;
		this.myName = myName;
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int option = JOptionPane.showConfirmDialog(null, "Ban da dang xuat!", "Thông báo", JOptionPane.WARNING_MESSAGE);
				try {
					clientImp.close();
					System.exit(0);
					
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		setBounds(100, 100, 535, 339);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 145, 280);
		contentPane.add(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		listChatterOnline = new JList<String>();
		listChatterOnline.setBorder(new TitledBorder(null, "Danh s\u00E1ch Online", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.add(listChatterOnline);
		listChatterOnline.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		listChatterOnline.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if (!e.getValueIsAdjusting()) {
					String clientSelected = listChatterOnline.getSelectedValue();
					try {
						addToChatter(clientSelected);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
		});
		
		panel_3 = new JPanel();
		panel.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		listGroups = new JList();
		listGroups.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Danh s\u00E1ch nh\u00F3m", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3.add(listGroups);
		
		btnCreateGroup = new JButton("Tạo nhóm");
		btnCreateGroup.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				clientImp.openCreateGroupView();
			}
		});
		panel_3.add(btnCreateGroup, BorderLayout.SOUTH);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(165, 11, 346, 291);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 346, 249);
		panel_1.add(tabbedPane);
		
		txtMessage = new JTextField();
		txtMessage.setBounds(0, 260, 231, 25);
		panel_1.add(txtMessage);
		txtMessage.setColumns(10);
		
		JButton btnSend = new JButton("");
		btnSend.setBackground(new Color(255, 255, 255));
		btnSend.setIcon(new ImageIcon("C:\\Users\\My computer\\eclipse-workspace\\RMI_GUI\\resources\\img\\airplane (1).png"));
		btnSend.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					sendMessage();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		btnSend.setBounds(301, 260, 45, 25);
		panel_1.add(btnSend);
		
		btnEmoji = new JButton("");
		btnEmoji.setIcon(new ImageIcon("C:\\Users\\My computer\\eclipse-workspace\\RMI_GUI\\resources\\img\\icons8-emoji-48 (1) (1).png"));
		btnEmoji.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openEmojiDialog();
			}
		});
		btnEmoji.setBackground(new Color(255, 255, 255));
		btnEmoji.setBounds(279, 260, 22, 25);
		panel_1.add(btnEmoji);
		
		btnFile = new JButton("");
		btnFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sendFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnFile.setIcon(new ImageIcon("C:\\Users\\My computer\\eclipse-workspace\\RMI_GUI\\resources\\img\\fileIcon (1).png"));
		btnFile.setBackground(Color.WHITE);
		btnFile.setBounds(257, 260, 22, 25);
		panel_1.add(btnFile);
		
		btnAudio = new JButton("");
		btnAudio.setIcon(new ImageIcon("C:\\Users\\My computer\\eclipse-workspace\\RMI_GUI\\resources\\img\\icons8-microphone-25.png"));
		btnAudio.setBackground(new Color(240, 240, 240));
		btnAudio.setBounds(235, 260, 22, 25);
		btnAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendAudio();
			}
		});
		
		panel_1.add(btnAudio);
	
		
		this.setTitle(myName);
		this.setVisible(true);
	}

	protected void sendAudio() {
		
		// check
		if (!isRecording) {
			// start recording audio
			btnAudio.setIcon(new ImageIcon("C:\\Users\\My computer\\eclipse-workspace\\RMI_GUI\\resources\\img\\recording.png") );
			isRecording = true;
			
			Thread t = new Thread() {
				public void run() {
					AudioController audioController = new AudioController();
		            ByteArrayOutputStream out = new ByteArrayOutputStream();
		            audioData = null;
					while (isRecording) {
						audioController.numBytesRead = audioController.microphone.read(audioController.data, 0, audioController.CHUNK_SIZE);
						out.write(audioController.data, 0, audioController.numBytesRead);
					}
					
					audioController.microphone.close();
					audioData = out.toByteArray();
					
					// send data
					int option = JOptionPane.showConfirmDialog(null,  "Gửi audio vừa record?", "Thông báo", JOptionPane.YES_NO_OPTION);
			        if (option == JOptionPane.YES_OPTION) {
			            // send data
			        	int selectedIndex = tabbedPane.getSelectedIndex();
			    		if (selectedIndex  == -1) return;
 		
			    		String nameReciver = tabbedPane.getTitleAt(selectedIndex);
			        	try {
							clientImp.sendAudio(audioData, nameReciver);
							
							JTextPane textPane = (JTextPane) tabbedPane.getComponentAt(selectedIndex);
							
							String contentTextPane = textPane.getText().trim();
							String subContent  = contentTextPane.substring(contentTextPane.indexOf("<body>"), contentTextPane.indexOf("</body>"));
							
							String disPlayMes = "<div style='text-align: right; color: blue;'>"
									+ "<a href='#link2' style='text-decoration: none;'>" + "&#128266;" + "</a>"
									+ "</div>";
							textPane.setText(subContent + disPlayMes);
							// set Text message area 
							
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        	
			        	
			        } else return;
				}
			};
			t.start();
			
		} else {
			// end recording and send 
			isRecording = false;
			btnAudio.setIcon(new ImageIcon("D:\\ChatApp\\RMI_GUI\\resources\\img\\icons8-microphone-25.png") );
	
		}
		
	}

	protected void sendFile() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Chọn file để gửi");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Files", "txt", "pdf", "doc");
	    fileChooser.setFileFilter(filter);
		int result = fileChooser.showDialog(null, "Chọn file");
		fileChooser.setVisible(true);

		if (result == JFileChooser.APPROVE_OPTION) {
			String fileName = fileChooser.getSelectedFile().getName();
			String filePath = fileChooser.getSelectedFile().getAbsolutePath();
			
			// Convert file to byte[]
			Path file = Paths.get(filePath);
			byte[] fileData = Files.readAllBytes(file);
			
			int selectedIndex = tabbedPane.getSelectedIndex();
			if (selectedIndex  == -1) return;
			
			String nameReciver = tabbedPane.getTitleAt(selectedIndex);
			
			clientImp.sendFile(nameReciver, fileData, fileName);
			
			// update view 
			JTextPane textPane = (JTextPane) tabbedPane.getComponentAt(selectedIndex);
			
			String contentTextPane = textPane.getText().trim();
			String subContent  = contentTextPane.substring(contentTextPane.indexOf("<body>"), contentTextPane.indexOf("</body>"));
			
			String disPlayMes = "<div style='text-align: right; color: blue;'>"
					+ "<a href='#link2' style='text-decoration: underline;'>" + fileName + "</a>"
					+ "</div>";
			textPane.setText(subContent + disPlayMes);
			
		}
		
	}

	protected void openEmojiDialog() {
		JDialog emojiDialog = new JDialog();
		Object[][] emojiMatrix = new Object[6][6];
		int emojiCode = 0x1F601;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++)
				emojiMatrix[i][j] = new String(Character.toChars(emojiCode++));
		}

		JTable emojiTable = new JTable();
		emojiTable.setModel(new DefaultTableModel(emojiMatrix, new String[] { "", "", "", "", "", "" }) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		emojiTable.setFont(new Font("Dialog", Font.PLAIN, 20));
		emojiTable.setShowGrid(false);
		emojiTable.setIntercellSpacing(new Dimension(0, 0));
		emojiTable.setRowHeight(30);
		emojiTable.getTableHeader().setVisible(false);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < emojiTable.getColumnCount(); i++) {
			emojiTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			emojiTable.getColumnModel().getColumn(i).setMaxWidth(30);
		}
		emojiTable.setCellSelectionEnabled(true);
		emojiTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		emojiTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtMessage.setText(txtMessage.getText() + emojiTable
						.getValueAt(emojiTable.rowAtPoint(e.getPoint()), emojiTable.columnAtPoint(e.getPoint())));
			}
		});

		emojiDialog.setContentPane(emojiTable);
		emojiDialog.setTitle("Chọn emoji");
		emojiDialog.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
		emojiDialog.pack();
		emojiDialog.setLocationRelativeTo(this);
		emojiDialog.setVisible(true);
		
	}

	protected void addToChatter(String clientSelected) throws RemoteException {
		
		if (this.clientImp.isChatter(clientSelected)) return; 
		if (!this.clientImp.addChatter(clientSelected)) {
			JOptionPane.showMessageDialog(null, clientSelected+ " Từ chối chat với bạn");
			return;
		}
		// add tab
		JTextPane textPane = new JTextPane();
		textPane.setContentType("text/html");
	    textPane.setEditable(false);
	    
		tabbedPane.addTab(clientSelected, null, textPane, null);
		
	}

	protected void sendMessage() throws RemoteException {
		// TODO Auto-generated method stub
		int selectedIndex = tabbedPane.getSelectedIndex();
		if (selectedIndex  == -1) return;
		
		String msg = txtMessage.getText().trim();
		
		String nameReciver = tabbedPane.getTitleAt(selectedIndex);
		JTextPane textPane = (JTextPane) tabbedPane.getComponentAt(selectedIndex);
		
		String contentTextPane = textPane.getText().trim();
		String subContent  = contentTextPane.substring(contentTextPane.indexOf("<body>"), contentTextPane.indexOf("</body>"));
		
		String disPlayMes = "<div style='text-align: right;'><span>" + msg + "</span></div>";
		textPane.setText(subContent + disPlayMes);
	
		txtMessage.setText("");
		
		// send toi retrive
		clientImp.sendMessage(nameReciver, this.myName, msg);
		
	}

	public void setMessage(String nameSender, String msg) {
		JTextPane textPane = getTextAreaByTabName(nameSender);
		if (textPane != null ) {
			String contentTextPane = textPane.getText().trim();
			String subContent  = contentTextPane.substring(contentTextPane.indexOf("<body>"), contentTextPane.indexOf("</body>"));
			
			String disPlayMes = "<div style='text-align: left;'>"
								+  msg
								+ "</div>";
			textPane.setText(subContent + disPlayMes);
		}
	}
	
	public void setMessageAudio(String sender, String audioKey) {
		JTextPane textPane = getTextAreaByTabName(sender);
		if (textPane != null) {
			String contentTextPane = textPane.getText().trim();
			String subContent  = contentTextPane.substring(contentTextPane.indexOf("<body>"), contentTextPane.indexOf("</body>"));
			
			String disPlayMes = "<div style='text-align: left; '>" 
					+ "<span>" + sender + ": </span>"
					+ "<a href='" + audioKey + "' style='color: blue; text-decoration: none;'>" + "&#128266;" + "</a>"
					+ "</div>";
			textPane.setText(subContent + disPlayMes);
			
			textPane.addHyperlinkListener(new HyperlinkListener() {
				
				@Override
				public void hyperlinkUpdate(HyperlinkEvent e) {
					// hyper link
					if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
						String href = e.getDescription();
						System.out.print(href);
						int selectedIndex = tabbedPane.getSelectedIndex();
						if (selectedIndex  == -1) return;
								
					    String tagName = tabbedPane.getTitleAt(selectedIndex);
						
						Map<String, byte[]> mapData = clientImp.listAudios.get(tagName);
						byte[] audioData =  mapData.get(href);
						
						// phat am
						try {
							AudioController.playAudio(audioData);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			});
		}
	}
	
	public void setMessageFile(String nameSender, String fileName) {
		JTextPane textPane = getTextAreaByTabName(nameSender);
		if (textPane != null) {
			String contentTextPane = textPane.getText().trim();
			String subContent  = contentTextPane.substring(contentTextPane.indexOf("<body>"), contentTextPane.indexOf("</body>"));
			
			String disPlayMes = "<div style='text-align: left; '>" 
					+ "<span>" + nameSender + ": </span>"
					+ "<a href='" + fileName + "' style='text-decoration: underline; color: blue;'>" + fileName + "</a>"
					+ "</div>";
			textPane.setText(subContent + disPlayMes);
			
			textPane.addHyperlinkListener(new HyperlinkListener() {
				
				@Override
				public void hyperlinkUpdate(HyperlinkEvent e) {
					if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
						 String href = e.getDescription();
						 int selectedIndex = tabbedPane.getSelectedIndex();
							if (selectedIndex  == -1) return;
								
							String tagName = tabbedPane.getTitleAt(selectedIndex);
							
							Map<String, byte[]> mapData = clientImp.listFileContents.get(tagName);
							byte[] fileContent=  mapData.get(href);
							
							// save file
							if (fileContent != null) {
								 JFileChooser fileChooser = new JFileChooser();
								 fileChooser.setDialogTitle("Chọn vị trí để lưu file");
								 int userSelection = fileChooser.showSaveDialog(null);
								 if (userSelection == JFileChooser.APPROVE_OPTION) {
								     File selectedFile = fileChooser.getSelectedFile();
								     String filePath = selectedFile.getAbsolutePath() + File.separator;

								     try (FileOutputStream fos = new FileOutputStream(filePath)) {
								            fos.write(fileContent);
								            JOptionPane.showMessageDialog(null, "File đã được lưu thành công.");
								     } catch (IOException e1) {
								            e1.printStackTrace();
								        }
								    }
		                    }
							
					}
					
				}
			}); 
		}
	}
	
	private JTextPane getTextAreaByTabName(String tabName) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            String currentTabName = tabbedPane.getTitleAt(i);
            if (currentTabName.equals(tabName)) {
                return (JTextPane) tabbedPane.getComponentAt(i);
            }
        }
        return null; // Trả về null nếu không tìm thấy tab với tên cần tìm
    }

	public boolean notifyNewChatter(String nameChatter) throws RemoteException {

        // Hiển thị thông báo với tiêu đề và các tùy chọn
        int option = JOptionPane.showConfirmDialog(null, nameChatter + " muốn chat với bạn?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // add thêm vô chatter
        	addChatter(nameChatter);
        	return true;
        } else {
        
        }
        return false;
	}
	
	protected void addChatter(String nameChatter) throws RemoteException {
		this.clientImp.addChatter(nameChatter, 0);
		// add tab
		
		JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setEditable(false);
		tabbedPane.addTab(nameChatter, null, textPane, null);
	}

	public void updateGroupList(List<Room> listRooms) {
		Vector<String> roomNames = new Vector<String>();
		
		for (Room item : listRooms) {
			roomNames.add(item.getRoomName());
		}
		listGroups.setListData(roomNames);
	}

	public void insertTabChat(Room room) {
		// add tab
		JTextPane textPane = new JTextPane();
		
		textPane.setContentType("text/html");
	    textPane.setEditable(false);
	  
		tabbedPane.addTab(room.getRoomName(), null, textPane, null);
	}
	
}
