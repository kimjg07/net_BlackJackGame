//JavaObjServer.java ObjectStream 기반 채팅 Server

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class Server extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea;
	private JTextField txtPortNumber;

	private ServerSocket socket; // 서버소켓
	private Socket client_socket; // accept() 에서 생성된 client 소켓
	private Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	public Vector<String> CardList = new Vector<String>();
	public String[] UserOrder = new String[4];  //user가 들어온 순서대로 Vector에 저장
	public int order = 0;  //UserOrder 벡터 안에서 순서지정
	public HashMap<String,Integer> UserMoney = new HashMap<String,Integer>();
	public int UserBetStatus = 0;
	public int dealerCheckSum = 0; //딜러의 카드 합 점수
	public String dealerStatus = "A"; //딜러 상태 버스트 B 살았을때 A
	public int userCnt = 0; //user가 들어온 순서 user class에 기입
	public int dealerCardCnt = 0;
	public HashMap<Integer,String> roomList = new HashMap<Integer,String>();  //현재 존재하는 방 리스트
	public int room_id = 0; //방 코드
	public String userList; //방의 참가한 유저 리스트
	
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server frame = new Server();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	public Server() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 338, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 300, 298);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JLabel lblNewLabel = new JLabel("Port Number");
		lblNewLabel.setBounds(13, 318, 87, 26);
		contentPane.add(lblNewLabel);

		txtPortNumber = new JTextField();
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setText("30000");
		txtPortNumber.setBounds(112, 318, 199, 26);
		contentPane.add(txtPortNumber);
		txtPortNumber.setColumns(10);

		JButton btnServerStart = new JButton("Server Start");
		btnServerStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				AppendText("Chat Server Running..");
				btnServerStart.setText("Chat Server Running..");
				btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
				txtPortNumber.setEnabled(false); // 더이상 포트번호 수정못 하게 막는다
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	// 새로운 참가자 accept() 하고 user thread를 새로 생성한다.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
					AppendText("새로운 참가자 from " + client_socket);
					// User 당 하나씩 Thread 생성
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user); // 새로운 참가자 배열에 추가
					new_user.start(); // 만든 객체의 스레드 실행
					AppendText("현재 참가자 수 " + UserVec.size());
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
			}
		}
	}

	public void AppendText(String str) {
		// textArea.append("사용자로부터 들어온 메세지 : " + str+"\n");
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(User msg) {
		// textArea.append("사용자로부터 들어온 object : " + str+"\n");
		textArea.append("code = " + msg.code + "\n");
		textArea.append("id = " + msg.UserName + "\n");
		textArea.append("data = " + msg.data + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	// User 당 생성되는 Thread
	// Read One 에서 대기 -> Write All
	class UserService extends Thread {
		private InputStream is;
		private OutputStream os;
		private DataInputStream dis;
		private DataOutputStream dos;

		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		private Socket client_socket;
		private Vector user_vc;
		public String UserName = "";
		public String UserStatus;
		public String UserGameStatus;
		public int userTurn = 0;
		public int checkSum = 0;  //user의 카드 합 포인트
		public int betAmount = 0; //user가 배팅한 금액
		public String list;
		public int currentRoom_id = 0;
		
		
		public UserService(Socket client_socket) {
			// TODO Auto-generated constructor stub
			// 매개변수로 넘어온 자료 저장
			this.client_socket = client_socket;
			this.user_vc = UserVec;
			try {
//				is = client_socket.getInputStream();
//				dis = new DataInputStream(is);
//				os = client_socket.getOutputStream();
//				dos = new DataOutputStream(os);

				oos = new ObjectOutputStream(client_socket.getOutputStream());
				oos.flush();
				ois = new ObjectInputStream(client_socket.getInputStream());

				// line1 = dis.readUTF();
				// /login user1 ==> msg[0] msg[1]
//				byte[] b = new byte[BUF_LEN];
//				dis.read(b);		
//				String line1 = new String(b);
//
//				//String[] msg = line1.split(" ");
//				//UserName = msg[1].trim();
//				UserStatus = "O"; // Online 상태
//				Login();
			} catch (Exception e) {
				AppendText("userService error");
			}
		}
		
		public void JoinRoom(User cm) {
			UserOrder[userCnt] = UserName; userCnt++;
			UserMoney.put(UserName,1000);
			UserStatus = "A";
			userList += UserName;
			currentRoom_id = cm.room_id;
			roomList.replace(cm.room_id, userList); //클라이언트가 접속한 room_id 받아와 roomList에 UserName 추가
			AppendText("새로운 참가자 " + UserName + " 입장.");
			WriteOne("Welcome to Java chat server\n");
			WriteOne(UserName + "님 환영합니다.\n"); // 연결된 사용자에게 정상접속을 알림
			String msg = "[" + UserName + "]님이 입장 하였습니다.\n";
			WriteOthers(msg); // 아직 user_vc에 새로 입장한 user는 포함되지 않았다.
			
			list = UserOrder[0] + " " + UserOrder[1] + " " + UserOrder[2] + " " + UserOrder[3];
			
			WriteList(list);
		}
		
		public void Login() {
			for(int key : roomList.keySet()) {
				User obcm = new User("SERVER", "100", ""+key); 
				WriteAllObject(obcm);
			}
		}

		public void Logout() {
			String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
			UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
			WriteAll(msg); // 나를 제외한 다른 User들에게 전송
			AppendText("사용자 " + "[" + UserName + "] 퇴장. 현재 참가자 수 " + UserVec.size());
		}

		// 모든 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
		public void WriteAll(String str) {
			String[] senduserlist = new String[4];
			senduserlist = roomList.get(currentRoom_id).split(" ");
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				for(int j=0;j<senduserlist.length;j++) {
					if(senduserlist[j].equals(UserName))
						user.WriteOne(str);
				}
			}
		}
		// 모든 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
		public void WriteAllObject(Object ob) {
			String[] senduserlist = new String[4];
			senduserlist = roomList.get(currentRoom_id).split(" ");
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				for(int j=0;j<senduserlist.length;j++) {
					if(senduserlist[j].equals(UserName))
						user.WriteOneObject(ob);
				}
			}
		}
		
		public void SendAllCard() {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				user.SendCard();
			}
			DealerSendCard();
		}
		
		// 나를 제외한 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
		public void WriteOthers(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user != this)
					user.WriteOne(str);
			}
		}

		public void WriteList(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
					user.WriteOneList(str);
			}
		}
		// Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
		public byte[] MakePacket(String msg) {
			byte[] packet = new byte[BUF_LEN];
			byte[] bb = null;
			int i;
			for (i = 0; i < BUF_LEN; i++)
				packet[i] = 0;
			try {
				bb = msg.getBytes("euc-kr");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (i = 0; i < bb.length; i++)
				packet[i] = bb[i];
			return packet;
		}

		// UserService Thread가 담당하는 Client 에게 1:1 전송
		public void WriteOne(String msg) {
			try {
				// dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
				User obcm = new User("SERVER", "200", msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
//					dos.close();
//					dis.close();
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
			}
		}

		// 귓속말 전송
		public void WritePrivate(String msg) {
			try {
				User obcm = new User("귓속말", "200", msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
			}
		}
		
		public void WriteOneObject(Object ob) {
			try {
			    oos.writeObject(ob);
			} 
			catch (IOException e) {
				AppendText("oos.writeObject(ob) error");		
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;				
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout();
			}
		}
		
		public void WriteOneList(String list) {
			try {
				// dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
				User obcm = new User("SERVER", "300", list);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
//					dos.close();
//					dis.close();
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
			}
		}
		
		public void SendCard() {
			while(true) {
				boolean cnt = false;
				int card_num = (int)(Math.random()*13+1);
				int card_shape = (int)(Math.random()*4+1);
				String card_Type = null;
				switch(card_shape) {
				case 1:
					card_Type="C";
					break;
				case 2:
					card_Type="D";
					break;
				case 3:
					card_Type="H";
					break;
				case 4:
					card_Type="S";
					break;
				}
				String primaryKey = card_Type + card_num;
				for(int i=0;i<CardList.size();i++) {
					if(CardList.get(i).equals(primaryKey) == true)
						cnt = true;
				}
				if(cnt == false) {
					if(card_num<10) {
						checkSum+=card_num;
					}
					else
						checkSum+=10;
					AppendText(UserName +":"+checkSum);
					CardList.add(primaryKey);
					User obcm = new User(UserName, "800", primaryKey); 
					obcm.setCheckSum(checkSum);
					obcm.turn = userTurn;
					userTurn++;
					if(checkSum > 21) {
						UserStatus = "B";
						obcm.UserStatus = "B";
					}
					WriteAllObject(obcm);
					break;
				}
			}
		}
		
		public void DealerSendCard() {
			while(true) {
				boolean cnt = false;
				int card_num = (int)(Math.random()*13+1);
				int card_shape = (int)(Math.random()*4+1);
				String card_Type = null;
				switch(card_shape) {
				case 1:
					card_Type="C";
					break;
				case 2:
					card_Type="D";
					break;
				case 3:
					card_Type="H";
					break;
				case 4:
					card_Type="S";
					break;
				}
			
				String primaryKey = card_Type + card_num;
				for(int i=0;i<CardList.size();i++) {
					if(CardList.get(i).equals(primaryKey) == true)
						cnt = true;
				}
				if(cnt == false) {
					if(card_num<10) {
						dealerCheckSum+=card_num;
					}
					else
						dealerCheckSum+=10;
					AppendText("Dealer:"+dealerCheckSum);
					CardList.add(primaryKey);
					dealerCardCnt++;
					User obcm = new User("Dealer", "800", primaryKey); 
					obcm.setCheckSum(dealerCheckSum);
					if(dealerCheckSum > 21) {
						dealerStatus = "B";
						obcm.UserStatus = "B";
						WriteAll("Dealer가 BUST 하였습니다.");
						for (int i = 0; i < user_vc.size(); i++) {
							UserService user = (UserService) user_vc.elementAt(i);
							if (!(user.UserStatus.equals("B"))) {
								User cm = new User("SERVER", "600", UserName);
								cm.amount += 200;
								WriteAll(user.UserName + "님이 이기셨습니다");
								WriteAllObject(cm);
							}	
						}
						CardList.clear();
						order = 0;
						UserBetStatus = 0;
						dealerCheckSum = 0;
						userTurn = 0;
						dealerStatus = "A";
						for (int i = 0; i < user_vc.size(); i++) {
							UserService user = (UserService) user_vc.elementAt(i);
							user.UserStatus = "A";
							user.checkSum = 0;
						}
						break;
					}
					WriteAllObject(obcm);
					break;
				}
			}
		}
		
		public void CurrentPerson() {
			User obcm = new User("SERVER", "900", UserOrder[order]);
			WriteAllObject(obcm);
			order++;
		}
		
		public void NextPerson() {  //버스트나 스테이 상태 판단하고 순서 배정
			if(order == 4) {
				order = 0;
				DealerTurn();
			}
			UserService user = (UserService) user_vc.elementAt(order);
			if (user.UserStatus.equals("S") || user.UserStatus.equals("B")) { 
				order++;
				NextPerson();
			}
			else {
				User obcm = new User("SERVER", "900", UserOrder[order]);
				WriteAllObject(obcm);
				order++;
			}
		}
		
		public void DealerTurn() { //딜러 버스트-22이상 힛-16이하 스테이-17이상 상태 판단
			if(dealerCheckSum < 17) {
				DealerSendCard();
			}
			else if(dealerCheckSum >= 17) {
				if(dealerCardCnt == 2) {
					String msg = "Cardopen";
					User obcm = new User("SERVER", "1000", msg);
					WriteAllObject(obcm);
				}
				if(EndChecking() == true) {
					for (int i = 0; i < user_vc.size(); i++) {
						UserService user = (UserService) user_vc.elementAt(i);
						if (!(user.UserStatus.equals("B"))) {
							System.out.println(user.checkSum + "x" + dealerCheckSum);
							if(user.checkSum > dealerCheckSum) 
								WriteAll(user.UserName + "님이 이겼습니다");
							else if(user.checkSum == dealerCheckSum) 
								WriteAll(user.UserName + "님이 비겼습니다");
							else
								WriteAll(user.UserName + "님이 졌습니다");
						}	
					}
					User cm = new User("SERVER", "600", UserName);
					WriteAllObject(cm);
					
					CardList.clear();
					order = 0;
					dealerCheckSum = 0;
					userTurn = 0;
					dealerStatus = "A";
					for (int i = 0; i < user_vc.size(); i++) {
						UserService user = (UserService) user_vc.elementAt(i);
						user.UserStatus = "A";
						user.checkSum = 0;
					}
				}	
			}	
		}
		
		public boolean EndChecking() {  //user가 모두 b나 s일때 그리고 딜러가 b나 checkSum이 17이상 일때 게임 종료
			int endCnt=0;
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "S" || user.UserStatus == "B") {
					endCnt++;
				}	
			}
			if(endCnt == 4)
				return true;
			else
				return false;
		}
		
		public void Bet(User cm) {
			int oldAmount = UserMoney.get(cm.UserName);
			UserMoney.replace(cm.UserName, oldAmount - 100);
			int newAmount = UserMoney.get(cm.UserName);
			betAmount = oldAmount - newAmount;
			String msg = cm.UserName + "님이" + betAmount + "를 배팅하셨습니다.";
			WriteAll(msg);
			UserBetStatus++;
			if(UserBetStatus == 4) {
				SendAllCard();
				SendAllCard();
				UserBetStatus = 0;
				CurrentPerson();
			}
		}
		
		public void Hit(User cm) {
			SendCard();
			AppendText(cm.UserName + "님이 HIT 하셨습니다.");
			String msg = cm.UserName + "님이 HIT 하셨습니다.";
			WriteAll(msg);
			if(UserStatus.equals("B")) {
				msg = cm.UserName + "님이 BUST 하셨습니다.";
				WriteAll(msg);
			}
			NextPerson();
		}
		
		public void Stay(User cm) {
			AppendText(UserStatus + "님이 STAY 하셨습니다.");
			UserStatus = "S";
			
			AppendText(cm.UserName + "님이 STAY 하셨습니다.");
			String msg = cm.UserName + "님이 STAY 하셨습니다.";
			WriteAll(msg);
			NextPerson();
		}
		 
		public void MakeRoom() {
			if(userList.isEmpty()) //userList가 비어있으면 UserName 삽입
				userList = UserName; 
			else					//userList에 데이터가 남아있으면 뒤에 이어서 추가
				userList += UserName + " " ;
			roomList.replace(room_id, userList);
			User obcm = new User("SERVER", "1100", "" + room_id); //room_id 클라이언트에게 전송
			WriteAllObject(obcm);
			room_id++;
		}
		
		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {
					// String msg = dis.readUTF();
//					byte[] b = new byte[BUF_LEN];
//					int ret;
//					ret = dis.read(b);
//					if (ret < 0) {
//						AppendText("dis.read() < 0 error");
//						try {
//							dos.close();
//							dis.close();
//							client_socket.close();
//							Logout();
//							break;
//						} catch (Exception ee) {
//							break;
//						} // catch문 끝
//					}
//					String msg = new String(b, "euc-kr");
//					msg = msg.trim(); // 앞뒤 blank NULL, \n 모두 제거
					Object obcm = null;
					String msg = null;
					User cm = null;
					
					if (socket == null)
						break;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					if (obcm == null)
						break;
					if (obcm instanceof User) {
						cm = (User) obcm;
						AppendObject(cm);
					} else
						continue;
					if (cm.code.matches("100")) {
						Login();
						
					} else if (cm.code.matches("200")) {
						msg = String.format("[%s] %s", cm.UserName, cm.data);
						AppendText(msg); // server 화면에 출력
						String[] args = msg.split(" "); // 단어들을 분리한다.
						if (args.length == 1) { // Enter key 만 들어온 경우 Wakeup 처리만 한다.
							//UserStatus = "O";
						} else if (args[1].matches("/exit")) {
							Logout();
							break;
						} else if (args[1].matches("/list")) {
							WriteOne("User list\n");
							WriteOne("Name\tStatus\n");
							WriteOne("-----------------------------\n");
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								WriteOne(user.UserName + "\t" + user.UserStatus + "\n");
							}
							WriteOne("-----------------------------\n");
						} else if (args[1].matches("/sleep")) {
							//UserStatus = "S";
						} else if (args[1].matches("/wakeup")) {
							//UserStatus = "O";
						} else if (args[1].matches("/cardup")) {
							SendAllCard();
						} else if (args[1].matches("/to")) { // 귓속말
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								if (user.UserName.matches(args[2]) && user.UserStatus.matches("O")) {
									String msg2 = "";
									for (int j = 3; j < args.length; j++) {// 실제 message 부분
										msg2 += args[j];
										if (j < args.length - 1)
											msg2 += " ";
									}
									// /to 빼고.. [귓속말] [user1] Hello user2..
									user.WritePrivate(args[0] + " " + msg2 + "\n");
									//user.WriteOne("[귓속말] " + args[0] + " " + msg2 + "\n");
									break;
								}
							}
						} else { // 일반 채팅 메시지
							//UserStatus = "O";
							//WriteAll(msg + "\n"); // Write All
							WriteAllObject(cm);
						}
					} else if (cm.code.matches("400")) { // logout message 처리
						Logout();
						break;
					}
					else if (cm.code.matches("500")) { 
						Bet(cm);
					}
					else if (cm.code.matches("700")) { 
						if (cm.data.matches("HEAT")) {
							Hit(cm);
						} else if (cm.data.matches("STAY")) {
							Stay(cm);
						}
					}
					else if (cm.code.matches("1100")) { 
						MakeRoom();
					}
					else if (cm.code.matches("1200")) { 
						UserName = cm.UserName;
						JoinRoom(cm);
					}
					else { // 300, 500, ... 기타 object는 모두 방송한다.
						WriteAllObject(obcm);
					} 
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
//						dos.close();
//						dis.close();
						ois.close();
						oos.close();
						client_socket.close();
						Logout(); // 에러가난 현재 객체를 벡터에서 지운다
						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝
			} // while
		} // run
	}

}
