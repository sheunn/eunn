package login;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TimeManager {
	private List<Player> playerTimes = new ArrayList<>(); // 사용자별 플레이 시간 리스트
	private String filePath = "total_time.txt";
	private LocalDateTime sessionStartTime; // 현재 세션 시작 시간

	public TimeManager(String filePath) {
		this.filePath = filePath;
		createFileIfNotExists(filePath);
		loadPlayerTimes();
	}

	// 파일이 없으면 생성
	private void createFileIfNotExists(String filePath) {
		File file = new File(filePath);
		try {
			if (file.createNewFile()) {
				System.out.println("새로운 시간 데이터 파일이 생성되었습니다: " + filePath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startSession() {
		sessionStartTime = LocalDateTime.now();
		System.out.println("세션이 시작되었습니다: " + sessionStartTime);
	}

	public void endSession(String playerName) {
		if (sessionStartTime != null) {
			int secondsElapsed = (int) ChronoUnit.SECONDS.between(sessionStartTime, LocalDateTime.now());
			addPlayTime(playerName, (int) secondsElapsed); // 사용자별 플레이 시간 추가
			sessionStartTime = null; // 세션 초기화
			System.out.println(playerName + "님의 이번 세션 경과 시간: " + secondsElapsed + "초");
		} else {
			System.err.println("세션이 시작되지 않았습니다.");
		}
	}

	// 사용자별 시간 추가
	public void addPlayTime(String playerName, int secondsToAdd) {
		Player player = findPlayer(playerName);

		if (player != null) {
			System.out.println("기존 플레이어 발견: " + playerName + ", 기존 시간: " + player.getTotalPlayTime());
			player.addPlayTime(secondsToAdd);
		} else {
			System.out.println("새 플레이어 생성: " + playerName);
			Player newPlayer = new Player(playerName, 0);
			newPlayer.addPlayTime(secondsToAdd);
			playerTimes.add(newPlayer);
		}
		savePlayerTimes(); // 변경 사항 저장
	}

	// 특정 플레이어의 총 플레이 시간 가져오기
	public int getTotalPlayTime(String playerName) {
		Player player = findPlayer(playerName);
		return (player != null) ? player.getTotalPlayTime() : 0;
	}

	// 사용자 데이터를 파일에 저장
	private void savePlayerTimes() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (Player player : playerTimes) {
				writer.write(player.getName() + "," + player.getTotalPlayTime());
				writer.newLine();
			}
		} catch (IOException e) {
			System.err.println("파일 저장 실패: " + e.getMessage());
		}
	}

	public void loadPlayerTimes() {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 2) {
					String name = parts[0];
					int totalTime = Integer.parseInt(parts[1]);

					Player existingPlayer = findPlayer(name);
					if (existingPlayer != null) {
						System.out.println("기존 데이터 업데이트: " + name + " (" + totalTime + "초)");
						existingPlayer.addPlayTime(totalTime);
					} else {
						System.out.println("새 데이터 추가: " + name + " (" + totalTime + "초)");
						playerTimes.add(new Player(name, totalTime));
					}
				}
			}
		} catch (IOException | NumberFormatException e) {
			System.err.println("파일 로드 실패: " + e.getMessage());
		}
	}

	public void idleBeforeShutdown(String playerName, int idleSeconds) {
		try {
			System.out.println("종료 전 유휴 상태로 " + idleSeconds + "초 대기합니다.");
			Thread.sleep(idleSeconds * 1000); // 유휴 상태 유지
		} catch (InterruptedException e) {
			System.err.println("유휴 상태 중 인터럽트 발생: " + e.getMessage());
		}

		// 세션 종료 처리
		endSession(playerName);

		System.out.println(playerName + "의 세션 종료가 완료되었습니다.");
	}

	// 특정 플레이어 찾기
	private Player findPlayer(String name) {
		for (Player player : playerTimes) {
			if (player.getName().equals(name)) {
				return player; // 이름이 일치하는 플레이어 반환
			}
		}
		return null; // 일치하는 플레이어가 없으면 null 반환
	}

}