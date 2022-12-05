import javax.swing.*;


import java.awt.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
public class WaitRoom extends JFrame{
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private JTextPane textArea;
	Main gameMain;
	String myName;
	JButton createRoom;
	JScrollPane Roominfo;
	JDialog createRoomlog;
	JButton enterRoom;
	public WaitRoom(String username, String ip_addr, String port_no) {
		getContentPane().setLayout(null);
		Myaction action = new Myaction();
		myName = username;
		Roominfo = new JScrollPane();
		Roominfo.setBounds(0, 0, 300, 493);
		getContentPane().add(Roominfo);
		
		textArea = new JTextPane();
		textArea.setEditable(true);
		textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
		Roominfo.setViewportView(textArea);
		
		createRoom = new JButton("방 생성");
		createRoom.setBounds(312, 31, 108, 44);
		createRoom.addActionListener(action);
		enterRoom = new JButton("방 입장");
		enterRoom.setBounds(312,90,108,44);
		enterRoom.addActionListener(action);
		getContentPane().add(createRoom);
		getContentPane().add(enterRoom);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(450,570);
		setTitle("대기 방");
		setVisible(true);
		
		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());
			System.out.println(oos);
			System.out.println(ois);
			//SendMessage("/login " + UserName);
			User obcm = new User(myName, "100", "Hello");
			SendObject(obcm);
			
			ListenNetwork net = new ListenNetwork();
			net.start();

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {
					
					Object obcm = null;
					String msg = null;
					User cm;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof User) {
						cm = (User) obcm;
						msg = String.format("[%s] %s", cm.UserName, cm.data);
					} else
						continue;
					switch (cm.code) {
					case "100":
						break;
					case "1100":
						setRoom(cm.room_id);
						break;
					}
				} catch (IOException e) {
					gameMain.AppendText("ois.readObject() error");
					try {
//						dos.close();
//						dis.close();
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝	``	

			}
		}
	}
	
	public void setRoom(int id) {
		int len = textArea.getDocument().getLength();
		// 끝으로 이동
		textArea.setCaretPosition(len);
		textArea.replaceSelection(id + "\n");
	}
	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == createRoom) {
				String roomName = JOptionPane.showInputDialog("방 제목을 입력하세요.");
				User obcm = new User(myName,"1100","createRoom");
				obcm.room_id = Integer.parseInt(roomName.trim());
				SendObject(obcm);
				gameMain = new Main(myName,oos,ois);
				setVisible(false);
			}
			else if(e.getSource() == enterRoom) {
				String roomNum = JOptionPane.showInputDialog("방 번호를 입력하세요.");
				User obcm = new User(myName,"1100","createRoom");
				obcm.room_id = Integer.parseInt(roomNum.trim());
				SendObject(obcm);
				gameMain = new Main(myName,oos,ois);
				setVisible(false);
			}
		}
	}
	
	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("메세지 송신 에러!!\n");
			gameMain.AppendText("SendObject Error");
		}
	}

}
