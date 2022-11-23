import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Main extends JFrame{
	public Image card1 = new ImageIcon(Main.class.getResource("./images/C1.png")).getImage();
	public Image changeCard1 = card1.getScaledInstance(75, 130, Image.SCALE_SMOOTH);
	public ImageIcon card = new ImageIcon(changeCard1);
	private JTextArea textArea;
	private JTextField textField;
	private JButton SendButton;
	private String UserName;
    private JButton BetButton;
    private JScrollPane scrollPane;
    private JButton heatButton;
    private JButton stayButton;
    private JPanel ButtonPanel;
    private JPanel GamePanel;
	public Main() {
		getContentPane().setBackground(new Color(255, 255, 255));
		Myaction action = new Myaction();
		setSize(1280,900);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
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
		
		heatButton = new JButton("히트");
		heatButton.setBounds(22, 798, 161, 53);
		heatButton.addActionListener(action);
		getContentPane().add(heatButton);
		
		stayButton = new JButton("스테이");
		stayButton.setBounds(195, 798, 161, 53);
		stayButton.addActionListener(action);
		getContentPane().add(stayButton);
		
		ButtonPanel = new JPanel();
		ButtonPanel.setBounds(0, 765, 964, 96);
		getContentPane().add(ButtonPanel);
		
		GamePanel = new JPanel();
		GamePanel.setBounds(0, 0, 964, 766);
		GamePanel.setLayout(null);
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
				String msg = "카드받지않음";
				AppendText(msg);
			}else if(e.getSource() == heatButton) {
				String msg = "카드받음";
				AppendText(msg);
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
