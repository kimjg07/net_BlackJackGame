import javax.swing.*;


import java.awt.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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

public class Main extends JFrame{
	
	
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private String cardex = "./images/D9.png";
	public Image card1 = new ImageIcon(Main.class.getResource(cardex)).getImage();
	public Image changeCard = card1.getScaledInstance(75, 124, Image.SCALE_SMOOTH);
	public ImageIcon cardimg = new ImageIcon(changeCard);
	public Image cardback = new ImageIcon(Main.class.getResource("./images/CardBackImg.png")).getImage();
	public Image changeCardback = cardback.getScaledInstance(75, 124, Image.SCALE_SMOOTH);
	public ImageIcon cardbackimg = new ImageIcon(changeCardback);
	private JTextArea textArea;
	private JTextField textField;
	private JButton SendButton;
	private String UserName;
    private JButton BetButton;
    private JScrollPane scrollPane;
    private JButton heatButton;
    private JButton stayButton;
    private JButton resetButton;
    private JPanel ButtonPanel;
    private JPanel GamePanel;
    private Image tableImg = new ImageIcon(Main.class.getResource("./images/pokertable.jpeg")).getImage();
    private int checkSum[]= {0,0,0,0};
    final static BasicStroke stroke = new BasicStroke(4.0f);
    private JLabel d_Sum;
    private JLabel dc6;
    private JLabel dc5;
    private JLabel dc4;
    private JLabel dc3;
    private JLabel dc2;
    private JLabel dc1;
    private JLabel u1_Sum;
    private JLabel u1c6;
    private JLabel u1c5;
    private JLabel u1c4;
    private JLabel u1c3;
    private JLabel u1c2;
    private JLabel u1c1;
    private JLabel u2_Sum;
    private JLabel u2c6;
    private JLabel u2c5;
    private JLabel u2c4;
    private JLabel u2c3;
    private JLabel u2c2;
    private JLabel u2c1;
    private JLabel u3_Sum;
    private JLabel u3c6;
    private JLabel u3c5;
    private JLabel u3c4;
    private JLabel u3c3;
    private JLabel u3c2;
    private JLabel u3c1;
    private JLabel u4_Sum;
    private JLabel u4c6;
    private JLabel u4c5;
    private JLabel u4c4;
    private JLabel u4c3;
    private JLabel u4c2;
    private JLabel u4c1;
    private Boolean vis = true;
    private int cardCount[] = {1,1,1,1};
    private JLabel u1_NameLabel;
    private JLabel u2_NameLabel;
    private JLabel u3_NameLabel;
    private JLabel u4_NameLabel;
    private JLabel u1Money;
    private JLabel u2Money;
    private JLabel u3Money;
    private JLabel u4Money;
    private JLabel GameEnd;
    private String myName;
    private int d_turn=0;
    private int userCount=1;
    private int d_checksum;
    private ImageIcon d_card;
    private WaitRoom wr;
	public Main(String username,WaitRoom wr) {
		myName = username;
		this.wr = wr;
		setTitle(username);
		System.out.println(oos);
		System.out.println(ois);
		getContentPane().setBackground(new Color(255, 255, 255));
		Myaction action = new Myaction();
		setSize(1280,900);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		GameEnd = new JLabel("Game END!");
		GameEnd.setBounds(300,300,300,300);
		GameEnd.setBackground(Color.BLACK);
		GameEnd.setVisible(false);
		GameEnd.setFont(new Font("맑은 고딕", Font.PLAIN, 30));
		GameEnd.setForeground(Color.RED);
		getContentPane().add(GameEnd);
		
		u1_NameLabel = new JLabel();
		u1_NameLabel.setForeground(Color.BLACK);
		u1_NameLabel.setText(myName);
		u1_NameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 30));
		u1_NameLabel.setBounds(38,332,122,38);
		getContentPane().add(u1_NameLabel);
		
		
		
		u1_Sum = new JLabel();
		u1_Sum.setBackground(Color.WHITE);
		u1_Sum.setForeground(Color.RED);
		u1_Sum.setText("0");
		u1_Sum.setFont(new Font("맑은 고딕", Font.PLAIN, 30));
		u1_Sum.setBounds(38, 707, 122, 38);
		getContentPane().add(u1_Sum);
		
		u2_NameLabel = new JLabel();
		u2_NameLabel.setForeground(Color.BLACK);
		u2_NameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 30));
		u2_NameLabel.setBounds(283,332,122,38);
		getContentPane().add(u2_NameLabel);
		
		u2_Sum = new JLabel();
		u2_Sum.setBackground(Color.WHITE);
		u2_Sum.setForeground(Color.RED);
		u2_Sum.setText("0");
		u2_Sum.setFont(new Font("맑은 고딕", Font.PLAIN, 30));
		u2_Sum.setBounds(283, 707, 122, 38);
		getContentPane().add(u2_Sum);
		
		u3_NameLabel = new JLabel();
		u3_NameLabel.setForeground(Color.BLACK);
		u3_NameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 30));
		u3_NameLabel.setBounds(528,332,122,38);
		getContentPane().add(u3_NameLabel);
		
		u3_Sum = new JLabel();
		u3_Sum.setBackground(Color.WHITE);
		u3_Sum.setForeground(Color.RED);
		u3_Sum.setText("0");
		u3_Sum.setFont(new Font("맑은 고딕", Font.PLAIN, 30));
		u3_Sum.setBounds(528, 707, 122, 38);
		getContentPane().add(u3_Sum);
		
		u4_NameLabel = new JLabel();
		u4_NameLabel.setForeground(Color.BLACK);
		u4_NameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 30));
		u4_NameLabel.setBounds(773,332,122,38);
		getContentPane().add(u4_NameLabel);
		
		u4_Sum = new JLabel();
		u4_Sum.setBackground(Color.WHITE);
		u4_Sum.setForeground(Color.RED);
		u4_Sum.setText("0");
		u4_Sum.setFont(new Font("맑은 고딕", Font.PLAIN, 30));
		u4_Sum.setBounds(773, 707, 122, 38);
		getContentPane().add(u4_Sum);
		
		JPanel panel = new JPanel();
		panel.setBounds(964, 0, 300, 861);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		SendButton = new JButton("Send");
		SendButton.setBounds(205, 740, 84, 23);
		SendButton.addActionListener(action);
		panel.add(SendButton);
		
		BetButton = new JButton("배팅");
		BetButton.setBounds(130, 770, 84, 23);
		BetButton.addActionListener(action);
		panel.add(BetButton);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 277, 721);
		panel.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
		scrollPane.setViewportView(textArea);
		
		textField = new JTextField();
		textField.setBounds(22, 741, 172, 22);
		textField.addActionListener(action);
		panel.add(textField);
		textField.setColumns(10);
		
		ButtonPanel = new JPanel();
		ButtonPanel.setBounds(0, 765, 964, 96);
		getContentPane().add(ButtonPanel);
		ButtonPanel.setLayout(null);
		
		heatButton = new JButton("히트");
		heatButton.setBounds(12, 10, 161, 53);
		heatButton.setEnabled(false);
		ButtonPanel.add(heatButton);
		
		stayButton = new JButton("스테이");
		stayButton.setBounds(196, 10, 161, 53);
		stayButton.setEnabled(false);
		ButtonPanel.add(stayButton);
		
		stayButton.addActionListener(action);
		heatButton.addActionListener(action);
		
		
		GamePanel = new JPanel() {
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D)g;
				g2.setStroke( stroke );
				g.drawImage(tableImg,0,0,964,766,this);
				g.setColor(Color.BLACK);
				g2.drawRect(63,24,547,164);
				g2.drawRect(25,332,155,423);
				g2.drawRect(270,332,155,423);
				g2.drawRect(515,332,155,423);
				g2.drawRect(760,332,155,423);
			}
		};
		
		dc6 = new JLabel();
		dc6.setBounds(340, 40, 76, 124);
		dc6.setVisible(false);
		getContentPane().add(dc6);
		
		dc5 = new JLabel();
		dc5.setBounds(310, 40, 76, 124);
		dc5.setVisible(false);
		getContentPane().add(dc5);
		
		dc4 = new JLabel();
		dc4.setBounds(280, 40, 76, 124);
		dc4.setVisible(false);
		getContentPane().add(dc4);
		
		dc3 = new JLabel();
		dc3.setBounds(250, 40, 76, 124);
		dc3.setVisible(false);
		getContentPane().add(dc3);
		
		dc2 = new JLabel();
		dc2.setBounds(220, 40, 76, 124);
		dc2.setVisible(false);
		getContentPane().add(dc2);
		
		dc1 = new JLabel();
		dc1.setBounds(190, 40, 76, 124);
		dc1.setVisible(false);
		getContentPane().add(dc1);
		
		
		d_Sum = new JLabel();
		d_Sum.setBackground(Color.WHITE);
		d_Sum.setForeground(Color.RED);
		d_Sum.setText("0");
		d_Sum.setFont(new Font("맑은 고딕", Font.PLAIN, 30));
		d_Sum.setBounds(70, 140, 122, 38);
		getContentPane().add(d_Sum);
		
		
		u1c6 = new JLabel();
		u1c6.setBounds(55, 569, 76, 124);
		u1c6.setVisible(false);
		getContentPane().add(u1c6);
		
		u1c5 = new JLabel();
		u1c5.setBounds(55, 536, 76, 124);
		u1c5.setVisible(false);
		getContentPane().add(u1c5);
		
		u1c4 = new JLabel();
		u1c4.setBounds(55, 503, 76, 124);
		u1c4.setVisible(false);
		getContentPane().add(u1c4);
		
		u1c3 = new JLabel();
		u1c3.setBounds(55, 470, 76, 124);
		u1c3.setVisible(false);
		getContentPane().add(u1c3);
		
		u1c2 = new JLabel();
		u1c2.setBounds(55, 437, 76, 124);
		u1c2.setVisible(false);
		getContentPane().add(u1c2);
		
		u1c1 = new JLabel();
		u1c1.setBounds(55, 404, 76, 124);
		u1c1.setVisible(false);
		getContentPane().add(u1c1);
		
		//
		
		u2c6 = new JLabel();
		u2c6.setBounds(300, 569, 76, 124);
		u2c6.setVisible(false);
		getContentPane().add(u2c6);
		
		u2c5 = new JLabel();
		u2c5.setBounds(300, 536, 76, 124);
		u2c5.setVisible(false);
		getContentPane().add(u2c5);
		
		u2c4 = new JLabel();
		u2c4.setBounds(300, 503, 76, 124);
		u2c4.setVisible(false);
		getContentPane().add(u2c4);
		
		u2c3 = new JLabel();
		u2c3.setBounds(300, 470, 76, 124);
		u2c3.setVisible(false);
		getContentPane().add(u2c3);
		
		u2c2 = new JLabel();
		u2c2.setBounds(300, 437, 76, 124);
		u2c2.setVisible(false);
		getContentPane().add(u2c2);
		
		u2c1 = new JLabel();
		u2c1.setBounds(300, 404, 76, 124);
		u2c1.setVisible(false);
		getContentPane().add(u2c1);
		
		//
		
		u3c6 = new JLabel();
		u3c6.setBounds(545, 569, 76, 124);
		u3c6.setVisible(false);
		getContentPane().add(u3c6);
		
		u3c5 = new JLabel();
		u3c5.setBounds(545, 536, 76, 124);
		u3c5.setVisible(false);
		getContentPane().add(u3c5);
		
		u3c4 = new JLabel();
		u3c4.setBounds(545, 503, 76, 124);
		u3c4.setVisible(false);
		getContentPane().add(u3c4);
		
		u3c3 = new JLabel();
		u3c3.setBounds(545, 470, 76, 124);
		u3c3.setVisible(false);
		getContentPane().add(u3c3);
		
		u3c2 = new JLabel();
		u3c2.setBounds(545, 437, 76, 124);
		u3c2.setVisible(false);
		getContentPane().add(u3c2);
		
		u3c1 = new JLabel();
		u3c1.setBounds(545, 404, 76, 124);
		u3c1.setVisible(false);
		getContentPane().add(u3c1);
		
		//
		
		u4c6 = new JLabel();
		u4c6.setBounds(790, 569, 76, 124);
		u4c6.setVisible(false);
		getContentPane().add(u4c6);
		
		u4c5 = new JLabel();
		u4c5.setBounds(790, 536, 76, 124);
		u4c5.setVisible(false);
		getContentPane().add(u4c5);
		
		u4c4 = new JLabel();
		u4c4.setBounds(790, 503, 76, 124);
		u4c4.setVisible(false);
		getContentPane().add(u4c4);
		
		u4c3 = new JLabel();
		u4c3.setBounds(790, 470, 76, 124);
		u4c3.setVisible(false);
		getContentPane().add(u4c3);
		
		u4c2 = new JLabel();
		u4c2.setBounds(790, 437, 76, 124);
		u4c2.setVisible(false);
		getContentPane().add(u4c2);
		
		u4c1 = new JLabel();
		u4c1.setBounds(790, 404, 76, 124);
		u4c1.setVisible(false);
		getContentPane().add(u4c1);
		
		GamePanel.setBounds(0, 0, 964, 766);
		GamePanel.setLayout(null);
		GamePanel.setOpaque(true);
		getContentPane().add(GamePanel);
		
		
		setVisible(true);
		
		
	}
	
	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == SendButton || e.getSource() == textField) {
				String msg = null;
				msg = textField.getText();
				wr.SendMessage(msg);
				textField.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				textField.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				
				if (msg.contains("/exit")) // 종료 처리
					System.exit(0);
			}else if(e.getSource() == BetButton) {
				//
				User obcm = new User(myName, "500", "BET");
				wr.SendObject(obcm);
				BetButton.setEnabled(false);
			}else if(e.getSource() == stayButton) {
				User obcm = new User(myName, "700", "STAY");
				wr.SendObject(obcm);
				heatButton.setEnabled(false);
				stayButton.setEnabled(false);
			}else if(e.getSource() == heatButton) {
				User obcm = new User(myName, "700", "HEAT");
				wr.SendObject(obcm);
				heatButton.setEnabled(false);
				stayButton.setEnabled(false);
			}
		}
	}
	
	public void setCardimg(String uname,String card,int checkSum,int turn) {
		String card_name = String.format("./images/"+card+".png");
		card1 = new ImageIcon(Main.class.getResource(card_name)).getImage();
		changeCard = card1.getScaledInstance(75, 124, Image.SCALE_SMOOTH);
		cardimg = new ImageIcon(changeCard);
		if(uname.equals(u1_NameLabel.getText())) {
			switch(turn) {
			case 0:
				u1_Sum.setText(""+checkSum);
				u1c1.setIcon(cardimg);
				u1c1.setVisible(true);
				break;
			case 1:
				this.checkSum[0]=checkSum;
				u1_Sum.setText(""+checkSum);
				u1c2.setIcon(cardimg);
				u1c2.setVisible(true);
				break;
			case 2:
				this.checkSum[0]=checkSum;
				u1_Sum.setText(""+checkSum);
				u1c3.setIcon(cardimg);
				u1c3.setVisible(true);
				break;
			case 3:
				this.checkSum[0]=checkSum;
				u1_Sum.setText(""+checkSum);
				u1c4.setIcon(cardimg);
				u1c4.setVisible(true);
				break;
			case 4:
				this.checkSum[0]=checkSum;
				u1_Sum.setText(""+checkSum);
				u1c5.setIcon(cardimg);
				u1c5.setVisible(true);
				break;
			case 5:
				this.checkSum[0]=checkSum;
				u1_Sum.setText(""+checkSum);
				u1c6.setIcon(cardimg);
				u1c6.setVisible(true);
				break;
			}
		}else if(uname.equals(u2_NameLabel.getText())) {
			switch(turn) {
			case 0:
				this.checkSum[1]=checkSum;
				u2_Sum.setText(""+checkSum);
				u2c1.setIcon(cardimg);
				u2c1.setVisible(true);
				break;
			case 1:
				this.checkSum[1]=checkSum;
				u2_Sum.setText(""+checkSum);
				u2c2.setIcon(cardimg);
				u2c2.setVisible(true);
				break;
			case 2:
				this.checkSum[1]=checkSum;
				u2_Sum.setText(""+checkSum);
				u2c3.setIcon(cardimg);
				u2c3.setVisible(true);
				break;
			case 3:
				this.checkSum[1]=checkSum;
				u2_Sum.setText(""+checkSum);
				u2c4.setIcon(cardimg);
				u2c4.setVisible(true);
				break;
			case 4:
				this.checkSum[1]=checkSum;
				u2_Sum.setText(""+checkSum);
				u2c5.setIcon(cardimg);
				u2c5.setVisible(true);
				break;
			case 5:
				this.checkSum[1]=checkSum;
				u2_Sum.setText(""+checkSum);
				u2c6.setIcon(cardimg);
				u2c6.setVisible(true);
				break;
			}
		}else if(uname.equals(u3_NameLabel.getText())) {
			switch(turn) {
			case 0:
				this.checkSum[2]=checkSum;
				u3_Sum.setText(""+checkSum);
				u3c1.setIcon(cardimg);
				u3c1.setVisible(true);
				break;
			case 1:
				this.checkSum[2]=checkSum;
				u3_Sum.setText(""+checkSum);
				u3c2.setIcon(cardimg);
				u3c2.setVisible(true);
				break;
			case 2:
				this.checkSum[2]=checkSum;
				u3_Sum.setText(""+checkSum);
				u3c3.setIcon(cardimg);
				u3c3.setVisible(true);
				break;
			case 3:
				this.checkSum[2]=checkSum;
				u3_Sum.setText(""+checkSum);
				u3c4.setIcon(cardimg);
				u3c4.setVisible(true);
				break;
			case 4:
				this.checkSum[2]=checkSum;
				u3_Sum.setText(""+checkSum);
				u3c5.setIcon(cardimg);
				u3c5.setVisible(true);
				break;
			case 5:
				this.checkSum[2]=checkSum;
				u3_Sum.setText(""+checkSum);
				u3c6.setIcon(cardimg);
				u3c6.setVisible(true);
				break;
			}
		}else if(uname.equals(u4_NameLabel.getText())) {
			switch(turn) {
			case 0:
				this.checkSum[3]=checkSum;
				u4_Sum.setText(""+checkSum);
				u4c1.setIcon(cardimg);
				u4c1.setVisible(true);
				break;
			case 1:
				this.checkSum[3]=checkSum;
				u4_Sum.setText(""+checkSum);
				u4c2.setIcon(cardimg);
				u4c2.setVisible(true);
				break;
			case 2:
				this.checkSum[3]=checkSum;
				u4_Sum.setText(""+checkSum);
				u4c3.setIcon(cardimg);
				u4c3.setVisible(true);
				break;
			case 3:
				this.checkSum[3]=checkSum;
				u4_Sum.setText(""+checkSum);
				u4c4.setIcon(cardimg);
				u4c4.setVisible(true);
				break;
			case 4:
				this.checkSum[3]=checkSum;
				u4_Sum.setText(""+checkSum);
				u4c5.setIcon(cardimg);
				u4c5.setVisible(true);
				break;
			case 5:
				this.checkSum[3]=checkSum;
				u4_Sum.setText(""+checkSum);
				u4c6.setIcon(cardimg);
				u4c6.setVisible(true);
				break;
			}
		}else if(uname.equals("Dealer")) {
			switch(d_turn) {
			case 0:
				dc1.setIcon(cardimg);
				dc1.setVisible(true);
				d_turn++;
				break;
			case 1:
				d_checksum=checkSum;
				dc2.setIcon(cardbackimg);
				dc2.setVisible(true);
				d_card=cardimg;
				d_turn++;
				break;
			case 2:
				dc3.setIcon(cardimg);
				d_Sum.setText(""+checkSum);
				dc2.setIcon(d_card);
				dc3.setVisible(true);
				d_turn++;
				break;
			case 3:
				d_Sum.setText(""+checkSum);

				dc4.setIcon(cardimg);
				dc4.setVisible(true);
				d_turn++;
				break;
			case 4:
				d_Sum.setText(""+checkSum);
				dc5.setIcon(cardimg);
				dc5.setVisible(true);
				d_turn++;
				break;
			case 5:
				d_Sum.setText(""+checkSum);
				dc6.setIcon(cardimg);
				dc6.setVisible(true);
				d_turn=0;
				break;
			}
		}
	}
	
	

	public void AppendText(String msg) {
		textArea.append(msg + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}
	
	public void setUserList(String msg[]) {
		int c=1;
		System.out.println(myName);
		for(int i=0;i<4;i++) {
			if(!(msg[i].equals(myName))) {
				switch(c) {
				case 1:
					u2_NameLabel.setText(msg[i]);
					c++;
					break;
				case 2:
					u3_NameLabel.setText(msg[i]);
					c++;
					break;
				case 3:
					u4_NameLabel.setText(msg[i]);
					c++;
					break;
				}
			}
		}
	}
	
		
		public void setDealerToggle() {
			dc2.setIcon(d_card);
			d_Sum.setText(""+d_checksum);

		}
		
		public void setClear() {
			d_turn=0;
			u1c1.setVisible(false);
			u1c2.setVisible(false);
			u1c3.setVisible(false);
			u1c4.setVisible(false);
			u1c5.setVisible(false);
			u1c6.setVisible(false);
			u2c1.setVisible(false);
			u2c2.setVisible(false);
			u2c3.setVisible(false);
			u2c4.setVisible(false);
			u2c5.setVisible(false);
			u2c6.setVisible(false);
			u3c1.setVisible(false);
			u3c2.setVisible(false);
			u3c3.setVisible(false);
			u3c4.setVisible(false);
			u3c5.setVisible(false);
			u3c6.setVisible(false);
			u4c1.setVisible(false);
			u4c2.setVisible(false);
			u4c3.setVisible(false);
			u4c4.setVisible(false);
			u4c5.setVisible(false);
			u4c6.setVisible(false);
			dc1.setVisible(false);
			dc2.setVisible(false);
			dc3.setVisible(false);
			dc4.setVisible(false);
			dc5.setVisible(false);
			dc6.setVisible(false);
			heatButton.setEnabled(false);
			stayButton.setEnabled(false);
			BetButton.setEnabled(true);
			u1_Sum.setText("0");
			u2_Sum.setText("0");
			u3_Sum.setText("0");
			u4_Sum.setText("0");
			d_Sum.setText("0");
			d_checksum=0;
			d_turn=0;
		}
		public void setButton(String name) {
			if(name.equals(myName))
			{
				heatButton.setEnabled(true);
				stayButton.setEnabled(true);
			}
		}
		
	

	
}