import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Main extends JFrame{
	private String cardex = "./images/C10.png";
	public Image card1 = new ImageIcon(Main.class.getResource(cardex)).getImage();
	public Image changeCard1 = card1.getScaledInstance(75, 124, Image.SCALE_SMOOTH);
	public ImageIcon card = new ImageIcon(changeCard1);
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
    private int checkSum;
    final static BasicStroke stroke = new BasicStroke(4.0f);
    private JLabel u1sum;
    private JLabel u1c6;
    private JLabel u1c5;
    private JLabel u1c4;
    private JLabel u1c3;
    private JLabel u1c2;
    private JLabel u1c1;
    private Boolean vis = true;
    private int cardCount =1;
	public Main() {
		getContentPane().setBackground(new Color(255, 255, 255));
		Myaction action = new Myaction();
		setSize(1280,900);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		u1sum = new JLabel();
		u1sum.setBackground(Color.WHITE);
		u1sum.setForeground(Color.RED);
		u1sum.setText("0");
		u1sum.setFont(new Font("맑은 고딕", Font.PLAIN, 30));
		u1sum.setBounds(38, 707, 122, 38);
		getContentPane().add(u1sum);
		
		JPanel panel = new JPanel();
		panel.setBounds(964, 0, 300, 861);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		SendButton = new JButton("Send");
		SendButton.setBounds(205, 740, 84, 23);
		SendButton.addActionListener(action);
		panel.add(SendButton);
		
		BetButton = new JButton("배팅");
		BetButton.setBounds(130, 805, 84, 23);
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
		ButtonPanel.add(heatButton);
		
		stayButton = new JButton("스테이");
		stayButton.setBounds(196, 10, 161, 53);
		ButtonPanel.add(stayButton);
		
		resetButton = new JButton("리셋");
		resetButton.setBounds(330,10,161,53);
		ButtonPanel.add(resetButton);
		stayButton.addActionListener(action);
		heatButton.addActionListener(action);
		resetButton.addActionListener(action);
		
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
		u1c6 = new JLabel(card);
		u1c6.setBounds(55, 569, 76, 124);
		u1c6.setVisible(false);
		getContentPane().add(u1c6);
		
		u1c5 = new JLabel(card);
		u1c5.setBounds(55, 536, 76, 124);
		u1c5.setVisible(false);
		getContentPane().add(u1c5);
		
		u1c4 = new JLabel(card);
		u1c4.setBounds(55, 503, 76, 124);
		u1c4.setVisible(false);
		getContentPane().add(u1c4);
		
		u1c3 = new JLabel(card);
		u1c3.setBounds(55, 470, 76, 124);
		u1c3.setVisible(false);
		getContentPane().add(u1c3);
		
		u1c2 = new JLabel(card);
		u1c2.setBounds(55, 437, 76, 124);
		u1c2.setVisible(false);
		getContentPane().add(u1c2);
		
		u1c1 = new JLabel(card);
		u1c1.setBounds(55, 404, 76, 124);
		u1c1.setVisible(false);
		getContentPane().add(u1c1);
		
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
				AppendText(msg);
				textField.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				textField.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				
				if (msg.contains("/exit")) // 종료 처리
					System.exit(0);
			}else if(e.getSource() == BetButton) {
				String msg = "100원배팅";
				AppendText(msg);
			}else if(e.getSource() == stayButton) {
				AppendText("카드 받지않음");
			}else if(e.getSource() == heatButton) {
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
				if(card_num<10) {
					checkSum+=card_num;
				}
				else
					checkSum+=10;
				String card_name = String.format("./images/"+card_Type+card_num+".png");
				switch(cardCount) {
				case 1:
					u1c1.setVisible(true);
					break;
				case 2:
					u1c2.setVisible(true);
					break;
				case 3:
					u1c3.setVisible(true);
					break;
				case 4:
					u1c4.setVisible(true);
					break;
				case 5:
					u1c5.setVisible(true);
					break;
				case 6:
					u1c6.setVisible(true);
					break;
				}
				AppendText(card_name);

				if(checkSum<=21)
					u1sum.setText(Integer.toString(checkSum));
				else
					u1sum.setText("BURST!!!");
				cardCount++;
			}else if(e.getSource()==resetButton) {
				u1c1.setVisible(false);
				u1c2.setVisible(false);
				u1c3.setVisible(false);
				u1c4.setVisible(false);
				u1c5.setVisible(false);
				u1c6.setVisible(false);
				cardCount=1;
				checkSum=0;
				u1sum.setText("0");
			}
		}
	}
	
	public void AppendText(String msg) {
		textArea.append(msg + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}
	
	public void setCard(String card) {
		
	}

	public static void main(String[] args) {
		new Main();
	}
}
