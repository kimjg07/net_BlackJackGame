import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.ImageIcon;

class User implements Serializable {
	private static final long serialVersionUID = 1L;
	public String code; // 100:로그인, 400:로그아웃, 200:채팅메시지, 300:Image, 500: Mouse Event
	public String UserName;
	public String data;
	public int amount; // 총 자산
	public int betAmount; 
	public int checkSum;
	public String UserStatus; //유저 상태 이기면 W 지면 L 비긴 경우 DR 버스트 B 스테이 S 살았을때 A 
	public String cardList;
	public int turn; // 유저 자신의 순서
	
	public User(String UserName, String code, String msg) {
		this.code = code;
		this.UserName = UserName;
		this.data = msg;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public int getCheckSum() {
		return checkSum;
	}

	public void setCheckSum(int checkSum) {
		this.checkSum = checkSum;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
}
