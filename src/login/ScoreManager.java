package login;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreManager {
	private List<Player> level1Scores = new ArrayList<>();
	private List<Player> level2Scores = new ArrayList<>();
	private List<Player> level3Scores = new ArrayList<>();
	private String level1FilePath;
	private String level2FilePath;
	private String level3FilePath;

	public ScoreManager(String level1FilePath, String level2FilePath, String level3FilePath) {
		this.level1FilePath = level1FilePath;
		this.level2FilePath = level2FilePath;
		this.level3FilePath = level3FilePath;

		createFileIfNotExists(level1FilePath);
		createFileIfNotExists(level2FilePath);
		createFileIfNotExists(level3FilePath);

		loadScores(1);
		loadScores(2);
		loadScores(3);
	}

	// ScoreManager.java
	public List<String> getGlobalRankings() {
		Map<String, Integer> highestScores = new HashMap<>();

		// 모든 레벨의 점수를 합산하여 각 사용자의 최고 기록 추출
		List<Player> allScores = new ArrayList<>();
		allScores.addAll(level1Scores);
		allScores.addAll(level2Scores);
		allScores.addAll(level3Scores);

		for (Player player : allScores) {
			int currentScore = highestScores.getOrDefault(player.getName(), 0);
			highestScores.put(player.getName(), Math.max(currentScore, player.getScore()));
		}

		// 점수 기준으로 정렬
		List<Map.Entry<String, Integer>> rankingEntries = new ArrayList<>(highestScores.entrySet());
		rankingEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

		// 출력 형식에 맞게 변환
		List<String> rankings = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : rankingEntries) {
			rankings.add(entry.getKey() + " : " + entry.getValue() + "점");
		}

		return rankings;
	}

	private void createFileIfNotExists(String filePath) {
		File file = new File(filePath);
		try {
			if (file.createNewFile()) {
				System.out.println("새로운 점수 파일이 생성되었습니다: " + filePath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addScore(Player player, int level) {
		List<Player> scores = getScoresList(level);
		if (scores != null) {
			scores.add(player);
			scores.sort(Comparator.comparingInt(Player::getScore).reversed());
		}
	}

	public List<String> getTopScores(int level) {
		List<Player> scores = getScoresList(level);
		List<String> rankingList = new ArrayList<>();

		if (scores == null || scores.isEmpty()) {
			return rankingList; // 점수가 없으면 빈 리스트 반환
		}

		// 점수 내림차순으로 정렬
		scores.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

		// 상위 10명까지만 이름과 점수를 리스트에 추가
		for (int i = 0; i < Math.min(10, scores.size()); i++) {
			Player player = scores.get(i);
			rankingList.add(player.getName() + " - " + player.getScore() + "점");
		}

		return rankingList;
	}

	public List<String> getUserScores(String playerName, int level) {
		List<Player> scores = getScoresList(level);
		List<Player> userScores = new ArrayList<>();
		List<String> rankingList = new ArrayList<>();

		if (scores == null || scores.isEmpty()) {
			return rankingList;
		}

		for (Player player : scores) {
			if (player.getName().equals(playerName)) {
				userScores.add(player);
			}
		}
		int rank = 1;
		int nextRank = 1;
		int previousScore = userScores.get(0).getScore();

		for (int i = 0; i < userScores.size(); i++) {
			Player player = userScores.get(i);

			// 점수가 이전 점수와 다르면 순위를 업데이트
			if (player.getScore() != previousScore) {
				rank = nextRank;
			}

			rankingList.add(rank + "위: " + player.getName() + " - " + player.getScore() + "점");

			// 다음 순위는 현재 인덱스 + 2로 설정하여 공동 순위 발생 시 건너뜀
			previousScore = player.getScore();
			nextRank = i + 2;
		}

		return rankingList;
	}

	public void saveScores(int level) {
		List<Player> scores = getScoresList(level);
		String filePath = getFilePath(level);

		if (scores != null && filePath != null) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
				for (Player player : scores) {
					writer.write(player.getName() + "," + player.getScore());
					writer.newLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadScores(int level) {
		String filePath = getFilePath(level);
		List<Player> scores = getScoresList(level);

		if (filePath != null && scores != null) {
			try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] parts = line.split(",");
					if (parts.length == 2) {
						scores.add(new Player(parts[0], Integer.parseInt(parts[1])));
					}
				}
				scores.sort(Comparator.comparingInt(Player::getScore).reversed());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getFilePath(int level) {
		return switch (level) {
		case 1 -> level1FilePath;
		case 2 -> level2FilePath;
		case 3 -> level3FilePath;
		default -> null;
		};
	}

	private List<Player> getScoresList(int level) {
		return switch (level) {
		case 1 -> level1Scores;
		case 2 -> level2Scores;
		case 3 -> level3Scores;
		default -> null;
		};
	}

	// 구현 필요
	public String getPoints(String playerName) {
		// TODO Auto-generated method stub
		return null;
	}

	// 구현 필
	public String getTotalTime(String playerName) {
		// TODO Auto-generated method stub
		return null;
	}
}