import java.util.HashMap;
import java.util.Vector;

public class GameSet {
	public Vector<String> CardList = new Vector<String>();
	public String[] UserOrder = new String[4];  //user가 들어온 순서대로 Vector에 저장
	public int order = 0;  //UserOrder 벡터 안에서 순서지정
	public HashMap<String,Integer> UserMoney = new HashMap<String,Integer>();
	public int UserBetStatus = 0;
	public int dealerCheckSum = 0; //딜러의 카드 합 점수
	public String dealerStatus = "A"; //딜러 상태 버스트 B 살았을때 A
	public int userCnt = 0; //user가 들어온 순서 user class에 기입
	public int dealerCardCnt = 0;
	public String userList = null; //방의 참가한 유저 리스트
	public int room_id;
	
	public GameSet(int room_id) {
		this.room_id = room_id;
	}
}
