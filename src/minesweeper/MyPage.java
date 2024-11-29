package minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import login.Player;
import login.PlayerDataSet;
import login.ScoreManager;
import login.TimeManager;

public class MyPage extends JPanel {
   private static final long serialVersionUID = 1L;
   private JLabel lblWelcome;
   private JLabel lblPoints;
   private JLabel lblTotalTime;
   private JButton viewRankingButton, storeButton, backButton;
   private JFrame parentFrame;
   private PlayerDataSet pds;
   private ScoreManager scoreManager;
   private TimeManager timeManager;
   private GamePanel gamePanel;
   private Player player;
   private String playerName;
   private int point;
   private int totalTime;
   private Main mainFrame;
   private Image backgroundImage; // 배경 이미지 추가
   
   public MyPage(JFrame parentFrame, Main mainFrame, ScoreManager scoreManager, Player player, String playerName,
         GamePanel gamePanel, TimeManager timeManager) {
      this.parentFrame = parentFrame;
      this.mainFrame = mainFrame;
      this.scoreManager = scoreManager;
      this.player = player;
      this.playerName = playerName;
      this.timeManager = timeManager;
      this.gamePanel = gamePanel;

       // 이미지 로드
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/img/블러배경.png")).getImage();
        } catch (Exception e) {
            System.err.println("배경 이미지 로드 실패: " + e.getMessage());
        }

        initializeMainView();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    
    
      if (timeManager == null) {
         System.err.println("[DEBUG] MyPage 생성자에서 timeManager가 null입니다.");
      } else {
         System.out.println("[DEBUG] MyPage 생성자에서 timeManager가 정상적으로 전달되었습니다.");
      }
    }
   private void initializeMainView() {
      removeAll();

   // GridBagLayout 설정
      setLayout(new java.awt.GridBagLayout());
      java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
      gbc.insets = new java.awt.Insets(15, 15, 15, 15); // 요소 간 간격
      gbc.gridx = 0; // X 좌표 중앙
      gbc.gridy = 0; // Y 좌표 시작
      gbc.anchor = java.awt.GridBagConstraints.CENTER; // 정렬 방식: 중앙
      
   // 상단 이미지
      ImageIcon originalIcon = new ImageIcon(getClass().getResource("/img/mypage.png"));
      Image scaledImage = originalIcon.getImage().getScaledInstance(600, 120, Image.SCALE_SMOOTH); // 크기 조정
      JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
      add(imageLabel, gbc);

   // 환영 메시지
      gbc.gridy++; // 다음 행으로 이동
      JPanel welcomePanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER)); // 이미지를 텍스트 양쪽에 배치
      welcomePanel.setOpaque(false); // 배경 투명
      
      // 왼쪽 이미지
      ImageIcon leftIcon = new ImageIcon(new ImageIcon(getClass().getResource("/img/픽맨픽셀_노랑.png"))
              .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)); // 크기 조정
      JLabel leftImageLabel = new JLabel(leftIcon);
      // 텍스트
      lblWelcome = new JLabel("  환영합니다, " + playerName + "님!  ");
      lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 40));
      lblWelcome.setForeground(new Color(0, 255, 255)); // 텍스트 색상 설정
      
      // 오른쪽 이미지
      ImageIcon rightIcon = new ImageIcon(new ImageIcon(getClass().getResource("/img/팩맨_핑크.png"))
              .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)); // 크기 조정
      JLabel rightImageLabel = new JLabel(rightIcon);
      
   // 패널에 추가
      welcomePanel.add(leftImageLabel);
      welcomePanel.add(lblWelcome);
      welcomePanel.add(rightImageLabel);

      add(welcomePanel, gbc);
      
   // 포인트 표시
      gbc.gridy++; // 다음 행으로 이동
      lblPoints = new JLabel();
      lblPoints.setFont(new Font("SansSerif", Font.BOLD, 23)); // 포인트 텍스트 크기
      lblPoints.setForeground(Color.WHITE);
      add(lblPoints, gbc);

      // 총 플레이 시간 표시
      gbc.gridy++; // 다음 행으로 이동
      lblTotalTime = new JLabel();
      lblTotalTime.setFont(new Font("SansSerif", Font.BOLD, 23)); // 총 플레이 시간 텍스트 크기
      lblTotalTime.setForeground(Color.WHITE);
      add(lblTotalTime, gbc);

   // 버튼 패널 추가
      gbc.gridy++; // 다음 행으로 이동
      JPanel buttonPanel = new JPanel(new java.awt.GridLayout(3, 1, 0, 0)); // 버튼 간격 설정
      buttonPanel.setOpaque(false);

      // 버튼 생성 (Hover 지원)
      JButton viewRankingButton = createHoverImageButton("/img/RANK.png", "/img/커서RANK.png", 0.6, e -> viewRanking());
      JButton storeButton = createHoverImageButton("/img/STORE.png", "/img/커서STORE.png", 0.6, e -> openStore());
      JButton backButton = createHoverImageButton("/img/BACK.png", "/img/커서BACK.png", 0.6, e -> goBack());
      buttonPanel.add(viewRankingButton);
      buttonPanel.add(storeButton);
      buttonPanel.add(backButton);
      add(buttonPanel, gbc);

      // 포인트와 총 플레이시간 업데이트
      updatePoints();
      updateTotalTime(playerName);
      
      revalidate();
      repaint();
   }
   
// 이미지 버튼 생성
private JButton createImageButton(String imagePath, java.awt.event.ActionListener actionListener, int width, int height) {
    JButton button = new JButton();
    try {
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(imagePath));
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); // 크기 조정
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        // 버튼 설정
        button.setIcon(scaledIcon);
        button.setContentAreaFilled(false); // 버튼 배경 제거
        button.setBorderPainted(false); // 버튼 테두리 제거
        button.setFocusPainted(false); // 포커스 테두리 제거
        button.setPreferredSize(new java.awt.Dimension(width, height)); // 버튼 크기 제한
        button.addActionListener(actionListener);
    } catch (Exception e) {
        System.err.println("이미지 버튼 로드 실패: " + e.getMessage());
    }
    return button;
}
private JButton createHoverImageButton(String defaultImagePath, String hoverImagePath, double scale, java.awt.event.ActionListener actionListener) {
    JButton button = new JButton();
    try {
        // 기본 이미지 설정
        ImageIcon defaultIcon = new ImageIcon(getClass().getResource(defaultImagePath));
        int newWidth = (int) (defaultIcon.getIconWidth() * scale);
        int newHeight = (int) (defaultIcon.getIconHeight() * scale);
        Image defaultResizedImage = defaultIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(defaultResizedImage));

        // Hover 이미지 설정
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
    button.addActionListener(actionListener); // 클릭 리스너 추가
    return button;
}


   private void updatePoints() {
      if (player == null) {
         System.err.println("[DEBUG] player 객체가 null입니다. 포인트를 업데이트할 수 없습니다.");
         return; // null 객체로 인한 NullPointerException 방지
      }
      lblPoints.setText("💎 POINT 💎 : " + player.getPoint());
   }

   public void refresh() {
      System.out.println("[DEBUG] MyPage 데이터 새로고침 중...");
      updateTotalTime(playerName); // 총 플레이 시간 업데이트
      updatePoints(); // 포인트 업데이트
   }

   private void viewRanking() {
	    removeAll(); // 기존 화면을 비우고 새로운 내용을 추가

	    // 배경 이미지 대신 검정색 배경 설정
	    JPanel backgroundPanel = new JPanel() {
	        private static final long serialVersionUID = 1L;

	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            // 검정 배경 설정
	            g.setColor(Color.BLACK);
	            g.fillRect(0, 0, getWidth(), getHeight());
	        }
	    };
	    backgroundPanel.setLayout(new BorderLayout());

	    // RANKING 로고 추가 (가운데 상단)
	    try {
	        ImageIcon rankingIcon = new ImageIcon(getClass().getResource("/img/RANKING로고.png"));
	        Image scaledImage = rankingIcon.getImage().getScaledInstance(300, 100, Image.SCALE_SMOOTH); // 크기 조정
	        JLabel rankingLogoLabel = new JLabel(new ImageIcon(scaledImage));
	        rankingLogoLabel.setHorizontalAlignment(JLabel.CENTER);
	        rankingLogoLabel.setBorder(new EmptyBorder(20, 0, 20, 0)); // 상단 여백
	        backgroundPanel.add(rankingLogoLabel, BorderLayout.NORTH);
	    } catch (Exception e) {
	        System.err.println("RANKING 로고 이미지 로드 실패: " + e.getMessage());
	    }

	    // 랭킹 패널 설정
	    JPanel rankingPanel = new JPanel();
	    rankingPanel.setLayout(new BoxLayout(rankingPanel, BoxLayout.Y_AXIS));
	    rankingPanel.setOpaque(false); // 투명으로 설정하여 배경 검정 유지
	    
	 // ** 테두리 추가: 흰색 설정 **
	    rankingPanel.setBorder(BorderFactory.createLineBorder(new Color(0,255,255), 4)); // 흰색 테두리, 두께 2px

	    // 랭킹 리스트 가져오기
	    List<String> rankings = scoreManager.getGlobalRankings();

	    // 순위별 스타일 적용
	    for (int i = 0; i < rankings.size(); i++) {
	        String playerName = rankings.get(i);
	        JPanel entryPanel = new JPanel();
	        entryPanel.setOpaque(false);
	        entryPanel.setLayout(new java.awt.GridBagLayout());
	        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();

	        // 순위별 아이콘 및 텍스트
	        if (i == 0) { // 1등
	            addRankingWithIcon(entryPanel, playerName, "/img/금.png", true);
	        } else if (i == 1) { // 2등
	            addRankingWithIcon(entryPanel, playerName, "/img/은.png", false);
	        } else if (i == 2) { // 3등
	            addRankingWithIcon(entryPanel, playerName, "/img/동.png", false);
	        } else { // 4등 이후
	            JLabel rankLabel = new JLabel((i + 1) + "등. " + playerName);
	            rankLabel.setFont(new Font("SansSerif", Font.BOLD, 22)); // 금 메달 기준
	            rankLabel.setForeground(Color.WHITE);

	            // 중앙 정렬
	            gbc.gridx = 0;
	            gbc.gridy = 0;
	            gbc.anchor = java.awt.GridBagConstraints.CENTER;
	            entryPanel.add(rankLabel, gbc);
	        }

	        rankingPanel.add(entryPanel);
	        rankingPanel.add(Box.createVerticalStrut(15)); // 각 순위 간격
	    }

	    // 스크롤 설정
	    JScrollPane scrollPane = new JScrollPane(rankingPanel);
	    scrollPane.setOpaque(false);
	    scrollPane.getViewport().setOpaque(false);
	    backgroundPanel.add(scrollPane, BorderLayout.CENTER);

	    // 뒤로가기 버튼
	    JButton backToMyPageButton = createHoverImageButton(
	    	    "/img/BACK.png",        // 기본 이미지 경로
	    	    "/img/커서BACK.png",     // 마우스 호버 이미지 경로
	    	    0.6,                   // 버튼 크기 비율 (213/250 = 0.85)
	    	    e -> {
	    	        initializeMainView(); // 마이페이지로 돌아가기
	    	        revalidate();
	    	        repaint();
	    	    }
	    	);


	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setOpaque(false);
	    buttonPanel.setLayout(new java.awt.GridBagLayout()); // 중앙 정렬을 위한 레이아웃
	    java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.anchor = java.awt.GridBagConstraints.CENTER;
	    buttonPanel.add(backToMyPageButton, gbc);

	    backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

	    // 화면에 배경 패널 추가
	    add(backgroundPanel);

	    revalidate();
	    repaint();
	}

   private void addRankingWithIcon(JPanel entryPanel, String playerName, String iconPath, boolean isTop) {
	    try {
	        // 순위 아이콘 이미지 설정
	        int iconWidth = 50; // 금 메달 크기
	        int iconHeight = 50;

	        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
	        Image scaledImage = icon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
	        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));

	        // 기본 폰트 설정
	        JLabel nameLabel = new JLabel(playerName);
	        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
	        nameLabel.setForeground(Color.WHITE);

	        // 내부 패널 생성 및 중앙 정렬
	        JPanel innerPanel = new JPanel(new java.awt.GridBagLayout());
	        innerPanel.setOpaque(false);
	        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();

	        if (isTop) {
	            ImageIcon boardIcon = new ImageIcon(getClass().getResource("/img/보드.png"));
	            Image boardImage = boardIcon.getImage().getScaledInstance(350, 190, Image.SCALE_SMOOTH); // 높이 160으로 조정
	            JLabel boardLabel = new JLabel(new ImageIcon(boardImage));
	            
	            gbc.gridx = 0;
	            gbc.gridy = 0;
	            gbc.gridwidth = 2; // 아이콘과 텍스트를 함께 포함하기 위해 설정
	            gbc.insets = new java.awt.Insets(0, 0, 15, 0); // 아래 간격
	            gbc.anchor = java.awt.GridBagConstraints.CENTER;
	            innerPanel.add(boardLabel, gbc);

	            // 다음 요소는 한 줄 아래에 배치
	            gbc.gridwidth = 1;
	        }
	        // 순위 아이콘 + 이름 및 점수를 하나로 묶어 배치
	        JPanel rankAndTextPanel = new JPanel(new BorderLayout());
	        rankAndTextPanel.setOpaque(false);

	        // 순위 아이콘 추가 (왼쪽)
	        rankAndTextPanel.add(iconLabel, BorderLayout.WEST);

	        // 이름 및 점수 추가 (오른쪽)
	        nameLabel.setHorizontalAlignment(JLabel.LEFT);
	        rankAndTextPanel.add(nameLabel, BorderLayout.CENTER);

	        // 아이콘과 텍스트를 innerPanel에 추가
	        gbc.gridx = 0;
	        gbc.gridy++;
	        gbc.gridwidth = 2; // 아이콘과 텍스트를 같이 묶음
	        gbc.insets = new java.awt.Insets(10, 0, 10, 0); // 상하 간격
	        gbc.anchor = java.awt.GridBagConstraints.CENTER;
	        innerPanel.add(rankAndTextPanel, gbc);

	        // 패널을 entryPanel에 추가
	        entryPanel.add(innerPanel, BorderLayout.CENTER);
	    } catch (Exception e) {
	        // System.err.println(" " + e.getMessage());
	    }
	}

   
   public void updateTotalTime(String playerName) {
      System.out.println("[DEBUG] updateTotalTime 호출 - playerName: " + playerName);

      if (timeManager != null) {
         System.out.println("[DEBUG] timeManager 객체 확인됨");

         // 플레이어의 총 플레이 시간 가져오기
         int totalTime = timeManager.getTotalPlayTime(playerName);
         int hours = totalTime / 3600; // 전체 초에서 시간 계산
         int minutes = (totalTime % 3600) / 60; // 남은 초에서 분 계산
         int seconds = totalTime % 60; // 남은 초에서 초 계산
         System.out.println("[DEBUG] 가져온 총 플레이 시간: " + totalTime + "초");

         // 총 플레이 시간 라벨 업데이트
         lblTotalTime.setText("🐾 PLAY TIME 🐾 : " + hours + "시간" + minutes + "분" + seconds + "초");
         System.out.println("[DEBUG] lblTotalTime 업데이트 완료");
      } else {
         System.out.println("[DEBUG] timeManager 객체가 null입니다");
         lblTotalTime.setText("총 플레이 시간을 불러올 수 없습니다.");
      }
   }

   // 구현 필요
   private void openStore() {
      Store storePanel = new Store(parentFrame, mainFrame, scoreManager, gamePanel, player, timeManager);
      parentFrame.setContentPane(storePanel);
      parentFrame.revalidate();
      parentFrame.repaint();
   }

   private void goBack() {
      if (parentFrame == null) {
         System.err.println("Error: parentFrame is null");
         return;
      }
      if (mainFrame == null) {
         System.err.println("Error: mainFrame is null");
         return;
      }

      // MainScreen으로 화면 전환
      parentFrame.setContentPane(new MainScreen(mainFrame, scoreManager, gamePanel, player, playerName));
      parentFrame.revalidate();
      parentFrame.repaint();
   }

}