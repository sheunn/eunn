package login;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class PlayerDataSet {
	private ArrayList<Player> players;
	private final String FILE_PATH = "players.dat";

	public PlayerDataSet() {
		players = new ArrayList<Player>();
		loadPlayersFromFile(); // 프로그램 시작 시 파일에서 사용자 정보를 불러옴
	}

	// 회원 추가
	public void addUsers(Player player) {
		players.add(player);
		savePlayersToFile(); // 사용자 추가 시 파일에 저장
	}

	// 아이디 중복 확인
	public boolean isIdOverlap(String id) {
		return players.contains(new Player(id));

	}

	// 회원 삭제
	public void withdraw(String id) {
		players.remove(getUser(id));
		savePlayersToFile(); // 사용자 삭제 시 파일에 저장
	}

	// 유저 정보 가져오기
	public Player getUser(String id) {
		return players.get(players.indexOf(new Player(id)));
	}

	// 회원인지 아닌지 확인
	public boolean contains(Player player) {
		return players.contains(player);
	}

	public void updateUser(Player updatedPlayer) {
		int index = players.indexOf(updatedPlayer);
		if (index != -1) {
			players.set(index, updatedPlayer); // 기존 데이터를 수정
			savePlayersToFile(); // 변경 내용을 파일에 저장
		}
	}

	// 사용자 정보를 파일에 저장
	public void savePlayersToFile() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
			oos.writeObject(players);
		} catch (IOException e) {
			System.out.println("파일 저장 중 오류 발생: " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void loadPlayersFromFile() {
		File file = new File(FILE_PATH);
		if (!file.exists()) {
			players = new ArrayList<>(); // 파일이 없을 경우 기본 초기화
			return;
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
			Object obj = ois.readObject();
			if (obj instanceof ArrayList<?>) {
				players = (ArrayList<Player>) obj;
			} else {
				System.out.println("파일 형식이 올바르지 않습니다.");
				players = new ArrayList<>(); // 잘못된 형식일 경우 기본 초기화
			}
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("파일 로딩 중 오류 발생:");
			e.printStackTrace();
			players = new ArrayList<>(); // 오류 발생 시 초기화
		}
	}
}