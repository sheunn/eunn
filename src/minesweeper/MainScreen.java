package minesweeper;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import login.Player;
import login.ScoreManager;
import login.TimeManager;

public class MainScreen extends JPanel {
	private static final long serialVersionUID = 1L;
	private MyPage myPage;
	private Main mainFrame;
	private ScoreManager scoreManager;
	private TimeManager timeManager;
	private GamePanel gamePanel;
	private Player player;
	private String playerName;
	private int previewUsageCount;
	private Image backgroundImage; // 배경 이미지 추가
    private Image logoImage; // 로고 이미지 추가

	public MainScreen(Main mainFrame, ScoreManager scoreManager, GamePanel gamepanel, Player player,
			String playerName) {
		this.mainFrame = mainFrame;
		this.scoreManager = scoreManager;
		this.timeManager = timeManager;
		this.gamePanel = gamePanel;
		this.player = player;
		this.playerName = playerName;
		this.previewUsageCount = mainFrame.getPreviewUsageCount();

		System.out.println("MainScreen initialized with mainFrame: " + this.mainFrame);

		this.setScoreManager(scoreManager);
		this.setPlayerName(playerName);
		
		// 배경 이미지 로드
		backgroundImage = new ImageIcon(getClass().getResource("/img/블러배경.png")).getImage();
        // 로고 이미지 로드
		logoImage = new ImageIcon(getClass().getResource("/img/메인로고.png")).getImage();

        setLayout(null); // 절대 위치 사용

        // 로고 설정
        if (logoImage != null) {
            int logoWidth = logoImage.getWidth(this) / 2; // 크기 50% 축소
            int logoHeight = logoImage.getHeight(this) / 2;
            JLabel logoLabel = new JLabel(new ImageIcon(logoImage.getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH)));
            logoLabel.setBounds((800 - logoWidth) / 2, 50, logoWidth, logoHeight); // 중앙 상단 배치
            add(logoLabel);
        }

		// 중앙 버튼 패널 생성
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS)); // 가로 배치
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
        // 버튼 생성
        JButton startGameButton = createHoverImageButton("/img/EASY.png", "/img/커서EASY.png", 0.6); // EASY
        JButton viewScoresButton = createHoverImageButton("/img/NORMAL.png", "/img/커서NORMAL.png", 0.6); // NOMAL
        JButton exitButton = createHoverImageButton("/img/HARD.png", "/img/커서HARD.png", 0.6); // HARD
        JButton myPageButton = createHoverImageButton("/img/main_MYPAGE.png", "/img/커서MYPAGE.png", 0.6); // MYPAGE
        
        // 버튼 위치와 크기 설정
        addButtonWithPosition(startGameButton, 800, 330, 170, 51, e -> mainFrame.startGame(1));
        addButtonWithPosition(viewScoresButton, 800, 405, 170, 51, e -> mainFrame.startGame(2));
        addButtonWithPosition(exitButton, 800, 480, 170, 51, e -> mainFrame.startGame(3));
        addButtonWithPosition(myPageButton, 800, 555, 170, 51, e -> mainFrame.createMyPage());

        // 버튼 추가
        add(startGameButton);
        add(viewScoresButton);
        add(exitButton);
        add(myPageButton);

        // 버튼 이벤트 설정
        startGameButton.addActionListener(e -> mainFrame.startGame(1));
        viewScoresButton.addActionListener(e -> mainFrame.startGame(2));
        exitButton.addActionListener(e -> mainFrame.startGame(3));
        myPageButton.addActionListener(e -> mainFrame.createMyPage());

		add(buttonPanel, BorderLayout.CENTER);
	}
	
	private void addButtonWithPosition(JButton button, int panelWidth, int y, int width, int height, ActionListener action) {
        button.setBounds((panelWidth - width) / 2, y, width, height);
        button.addActionListener(action);
        add(button);
    }
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 배경 이미지 그리기
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
	
	private JButton createHoverImageButton(String defaultImagePath, String hoverImagePath, double scale) {
        JButton button = new JButton();
        try {
            // 기본 이미지 설정
            ImageIcon defaultIcon = new ImageIcon(getClass().getResource(defaultImagePath));
            int newWidth = (int) (defaultIcon.getIconWidth() * scale);
            int newHeight = (int) (defaultIcon.getIconHeight() * scale);
            Image defaultResizedImage = defaultIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(defaultResizedImage));

            // 호버 이미지 설정
            ImageIcon hoverIcon = new ImageIcon(getClass().getResource(hoverImagePath));
            Image hoverResizedImage = hoverIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            button.setRolloverIcon(new ImageIcon(hoverResizedImage));
        } catch (Exception e) {
            System.err.println("이미지 버튼 생성 실패: " + e.getMessage());
        }

        // 버튼 투명화 (배경, 테두리 제거)
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }

	public void showMyPage() {
		if (mainFrame != null) {
			mainFrame.createMyPage();
		} else {
			System.err.println("[DEBUG] MainScreen에서 mainFrame이 null입니다.");
		}
	}

	public JPanel getPanel() {
		return this;
	}

	public ScoreManager getScoreManager() {
		return scoreManager;
	}

	public void setScoreManager(ScoreManager scoreManager) {
		this.scoreManager = scoreManager;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}