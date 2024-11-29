package minesweeper;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import login.LoginForm;
import login.Player;
import login.PlayerDataSet;
import login.ScoreManager;
import login.TimeManager;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	private ScoreManager scoreManager;
	private TimeManager timeManager;
	private GamePanel gamePanel;
	private PlayerDataSet pds;
	private MyPage myPage;
	private Player player;
	private String playerName;
	private int previewUsageCount = 0;
	private int timeAddUsageCount = 0;
	
	public ScoreManager getScoreManager() {
	    return scoreManager;
	}

	public int getPreviewUsageCount() {
		return previewUsageCount;
	}

	public int usePreviewUsageCount() {
		return previewUsageCount--;
	}

	public void setPreviewUsageCount(int previewUsageCount) {
		this.previewUsageCount = previewUsageCount;
	}

	public int getTimeAddUsageCount() {
		return timeAddUsageCount;
	}

	public int useTimeAddUsageCount() {
		return timeAddUsageCount--;
	}

	public void setTimeAddUsageCount(int timeAddUsageCount) {
		this.timeAddUsageCount = timeAddUsageCount;
	}

	public Main() {
		
		pds = new PlayerDataSet(); // 데이터셋 초기화
		LoginForm loginForm = new LoginForm(this, pds);
		loginForm.setVisible(true);
	}

	public void loginSuccessful(Player player) {
		this.player = player;
		this.playerName = player.getName();
		this.previewUsageCount = player.getPreviewUsageCount();
		this.timeAddUsageCount = player.getTimeAddUsageCount();
		scoreManager = new ScoreManager("scores_level_1.txt", "scores_level_2.txt", "scores_level_3.txt");
		timeManager = new TimeManager("total_time.txt");
		timeManager.loadPlayerTimes();

		// MyPage 생성 전에 timeManager 초기화 상태 확인
		if (timeManager == null) {
			System.err.println("[DEBUG] loginSuccessful에서 timeManager가 null입니다.");
		} else {
			System.out.println("[DEBUG] loginSuccessful에서 timeManager가 초기화되었습니다.");
		}

		showMainScreen();
		timeManager.startSession();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				handleWindowClosing();
			}
		});
	}

	void showMainScreen() {
		// MyPage 생성
		createMyPage();
		MainScreen mainScreen = new MainScreen(this, scoreManager, gamePanel, player, playerName);
		setContentPane(mainScreen.getPanel());
		setSize(800, 800);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void startGame(int level) {
		int gridSize;
		int numMines;

		switch (level) {
		case 1 -> {
			gridSize = 8;
			numMines = 10;
		}
		case 2 -> {
			gridSize = 12;
			numMines = 20;
		}
		case 3 -> {
			gridSize = 16;
			numMines = 40;
		}
		default -> throw new IllegalArgumentException("유효하지 않은 레벨");
		}
		if (this.gamePanel != null) {
			this.gamePanel.stopTimer(); // 기존 타이머 정지
			this.remove(this.gamePanel); // 기존 GamePanel 제거
		}
		
		// GamePanel 생성
		this.gamePanel = new GamePanel(this, playerName, scoreManager, level, gridSize, numMines);

		// GamePanel을 화면에 설정
		setContentPane(gamePanel);
		revalidate();
		repaint();

	}

	private void handleWindowClosing() {
		int choice = JOptionPane.showConfirmDialog(Main.this, // Main(JFrame)의 인스턴스를 참조
				"프로그램을 종료합니다", "종료", JOptionPane.YES_NO_OPTION);
		System.out.println(choice);
		if (choice == JOptionPane.YES_OPTION) {
			timeManager.endSession(player.getName()); // 세션 종료 처리
			onProgramExit(); // 데이터 저장
			System.exit(0); // 프로그램 종료
		} else {
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}
	}

	public void endGame(int score, int level) {
		scoreManager.addScore(new Player(playerName, score), level);
		scoreManager.saveScores(level);
		player.addPoint(score / 10);
		System.out.println("\n" + player.getPoint());
		displayRankings(level);
		pds.updateUser(player);
		showMainScreen();
	}

	public void displayRankings(int level) {
		List<String> rankings = scoreManager.getTopScores(level);
		StringBuilder message = new StringBuilder("레벨 " + level + " 전체 랭킹 (상위 10등)\n");

		for (String rank : rankings) {
			message.append(rank).append("\n");
		}

		JOptionPane.showMessageDialog(this, message.toString());
	}

	public void displayPersonalRankings(int level) {
		List<String> personalRankings = scoreManager.getUserScores(playerName, level);
		StringBuilder message = new StringBuilder("레벨 " + level + " 개인 랭킹 (상위 10등)\n");

		for (String rank : personalRankings) {
			message.append(rank).append("\n");
		}

		JOptionPane.showMessageDialog(this, message.toString());
	}

	public TimeManager getTimeManager() {
		return timeManager;
	}

	public void createMyPage() {
		if (myPage == null) {
			if (timeManager == null) {
				System.err.println("[DEBUG] createMyPage에서 timeManager가 null입니다.");
				return;
			}
			myPage = new MyPage(this, this, scoreManager, player, playerName, gamePanel, timeManager);
			System.out.println("[DEBUG] MyPage가 새로 생성되었습니다.");
		} else {
			System.out.println("[DEBUG] MyPage가 이미 생성되어 재사용됩니다.");
			myPage.refresh(); // MyPage 데이터 갱신
		}

		// MyPage를 화면에 표시
		setContentPane(myPage);
		revalidate();
		repaint();
	}

	private void onProgramExit() {
		if (player != null) {
			// 현재 사용자의 데이터 업데이트
			player.setPreviewUsageCount(this.previewUsageCount);
			player.setTimeAddUsageCount(this.timeAddUsageCount);

			Player existingPlayer = pds.getUser(player.getId());
			if (existingPlayer != null) {
				existingPlayer.setPreviewUsageCount(this.previewUsageCount);
				existingPlayer.setTimeAddUsageCount(this.timeAddUsageCount);
			}
		}

		// 모든 데이터 저장
		pds.savePlayersToFile();
		System.out.println("데이터가 성공적으로 저장되었습니다.");
	}

	public static void main(String[] args) {
		new Main();

	}
}