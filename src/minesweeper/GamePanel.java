package minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import login.ScoreManager;

public class GamePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    Main mainFrame;
    Timer timer;
    private MineGrid mineGrid;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private JLabel mineLabel; // 남은 지뢰 개수 표시 라벨
    private JPanel topPanel;
    private int elapsedTime; // 경과 시간
    private int score;
    private int level;
    private int remainingTime; // 남은 시간 (초 단위)

    public GamePanel(Main mainFrame, String playerName, ScoreManager scoreManager, int level, int gridSize,
            int numMines) {
        setLayout(new BorderLayout());
        setBackground(new Color(18,11,54)); // 전체 배경색
        setBorder(new LineBorder(new Color(0, 255, 255), 6));// 바깥 테두리

        this.mainFrame = mainFrame;
        this.level = level;
        this.score = 0;
        this.elapsedTime = 0;
        this.remainingTime = getInitialTimeForLevel(level);

        // 상단 정보 패널 생성
        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        topPanel.setBackground(new Color(18,11,54));
        topPanel.setBorder(new LineBorder(new Color(110,9,225), 3));

        mineLabel = new JLabel("남은 지뢰: " + numMines + "개");
        mineLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        mineLabel.setForeground(Color.WHITE);

        scoreLabel = new JLabel("점수: 0");
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        scoreLabel.setForeground(Color.WHITE);

        timerLabel = new JLabel("남은 시간: " + remainingTime + "초");
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        timerLabel.setForeground(Color.WHITE);

        // "나가기" 버튼 이미지 설정
        ImageIcon exitIcon = new ImageIcon(getClass().getResource("/img/exit_pixel.png")); // 이미지 경로 설정
        Image exitScaledImage = exitIcon.getImage().getScaledInstance(80, 40, Image.SCALE_SMOOTH); // 이미지 크기 조정
        JButton exitButton = new JButton(new ImageIcon(exitScaledImage)); // 버튼에 이미지 추가
        exitButton.setPreferredSize(new Dimension(80, 40)); // 버튼 크기 설정
        exitButton.setBackground(new Color(18,11,54)); // 배경색 설정
        exitButton.setOpaque(true);
        exitButton.setBorder(BorderFactory.createEmptyBorder()); // 테두리 제거
        exitButton.addActionListener(e -> {
            mainFrame.showMainScreen(); // 메인 화면으로 이동
            timer.stop(); // 타이머 중지
        });

        // 시간 추가 버튼 이미지 적용
        ImageIcon clockIcon = new ImageIcon(getClass().getResource("/img/시계.png")); // 이미지 경로
        Image clockScaledImage = clockIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // 이미지 크기
        JButton addTimeButton = new JButton(new ImageIcon(clockScaledImage));
        addTimeButton.setPreferredSize(new Dimension(50, 50)); // 초기화 버튼과 같은 크기
        addTimeButton.setBackground(new Color(18,11,54));
        addTimeButton.setOpaque(true);
        addTimeButton.setBorder(null); // 테두리 제거
        addTimeButton.addActionListener(e -> {
            if (remainingTime > 60) {
                JOptionPane.showMessageDialog(mainFrame, "아직 시간 추가를 할 수 없습니다.");
            } else {
                remainingTime += 30;
                JOptionPane.showMessageDialog(mainFrame, "30초 추가되었습니다. 남은 횟수: " + mainFrame.getTimeAddUsageCount());
            }
        });

        // 미리보기 버튼 이미지 적용
        ImageIcon previewIcon = new ImageIcon(getClass().getResource("/img/돋보기.png")); // 이미지 경로
        Image previewScaledImage = previewIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH); // 이미지 크기
        JButton previewButton = new JButton(new ImageIcon(previewScaledImage));
        previewButton.setPreferredSize(new Dimension(40, 40)); // 초기화 버튼과 같은 크기
        previewButton.setBackground(new Color(18,11,54)); 
        previewButton.setOpaque(true);
        previewButton.setBorder(null);
        previewButton.addActionListener(e -> {
            if (mainFrame.usePreviewUsageCount() > 0) {
                mineGrid.setPreviewMode(true);
                JOptionPane.showMessageDialog(mainFrame, "미리보기 모드: 칸 하나를 선택하세요! 남은 횟수: " + mainFrame.getPreviewUsageCount());
            } else {
                JOptionPane.showMessageDialog(mainFrame, "미리보기 횟수가 부족합니다!");
            }
        });

        // 리셋 버튼 이미지 적용
        ImageIcon resetIcon = new ImageIcon(getClass().getResource("/img/픽맨픽셀_노랑.png"));
        Image resetScaledImage = resetIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH); // 이미지 크기
        JButton reStartButton = new JButton(new ImageIcon(resetScaledImage));
        reStartButton.setPreferredSize(new Dimension(40, 40));
        reStartButton.setBackground(new Color(18,11,54)); // 배경색 수정
        reStartButton.setOpaque(true);
        reStartButton.setBorder(null);

        JPanel resetButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); // 중앙 정렬
        resetButtonPanel.add(reStartButton);
        
        // 드롭다운 메뉴 생성
        JPopupMenu restartMenu = new JPopupMenu();
        restartMenu.setBackground(new Color(18,11,54)); // 드롭다운 메뉴 배경색 수정
        restartMenu.setBorder(BorderFactory.createLineBorder(new Color(18,11,54), 3)); // 테두리 수정

        JMenuItem level1Item = new JMenuItem("초급");
        level1Item.setBackground(new Color(18,11,54)); // 배경색 수정
        level1Item.setBorder(new LineBorder(new Color(0, 251, 255), 1)); //테두리
        level1Item.setForeground(Color.WHITE); // 텍스트 색상 수정
        level1Item.addActionListener(e -> {
            resetTimer(1);
            mainFrame.startGame(1);
        });

        JMenuItem level2Item = new JMenuItem("중급");
        level2Item.setBackground(new Color(18,11,54)); // 배경색 수정
        level2Item.setBorder(new LineBorder(new Color(0, 251, 255), 1)); // 테두리
        level2Item.setForeground(Color.WHITE); // 텍스트 색상 수정
        level2Item.addActionListener(e -> {
            resetTimer(2);
            mainFrame.startGame(2);
        });

        JMenuItem level3Item = new JMenuItem("상급");
        level3Item.setBackground(new Color(18,11,54)); // 배경색 수정
        level3Item.setBorder(new LineBorder(new Color(0, 251, 255), 1));
        level3Item.setForeground(Color.WHITE); // 텍스트 색상 수정
        level3Item.addActionListener(e -> {
            resetTimer(3);
            mainFrame.startGame(3);
        });

        restartMenu.add(level1Item);
        restartMenu.add(level2Item);
        restartMenu.add(level3Item);

        reStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartMenu.show(reStartButton, reStartButton.getWidth() / 2, reStartButton.getHeight() / 2);
            }
        });

        topPanel.add(addTimeButton);
        topPanel.add(previewButton);
        topPanel.add(scoreLabel);
        topPanel.add(timerLabel);
        topPanel.add(reStartButton);
        topPanel.add(mineLabel);
        topPanel.add(exitButton);

        mineGrid = new MineGrid(gridSize, numMines, mainFrame, this);
        JPanel mineGridPanel = mineGrid.getPanel();
        mineGridPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 251, 255), 4)); // 테두리 수정

        add(topPanel, BorderLayout.NORTH);
        add(mineGridPanel, BorderLayout.CENTER);

        startTimer();
    }


    private int getInitialTimeForLevel(int level) {
        switch (level) {
            case 1:
                return 3 * 60;
            case 2:
                return 5 * 60;
            case 3:
                return 7 * 60;
            default:
                return 3 * 60;
        }
    }

    public void updateMineCount(int remainingMines) {
        mineLabel.setText("남은 지뢰: " + remainingMines + "개");
    }

    // 타이머 시작 메서드
    private void startTimer() {
        timer = new Timer(1000, e -> {
            if (remainingTime > 0) {
                remainingTime--;
                timerLabel.setText("남은 시간: " + remainingTime + "초");
            } else {
                timer.stop();
                JOptionPane.showMessageDialog(this, "시간 초과! GAME OVER!");
                mainFrame.endGame(score, level);
            }
        });
        timer.start();
    }

    public void stopTimer() {
    	if (timer != null) {
			timer.stop();
			timer = null;
		}
    }

    // GamePanel 클래스 내 추가 메서드
 	public void resetTimer(int level) {
 		stopTimer(); // 기존 타이머 중지
 		this.remainingTime = getInitialTimeForLevel(level); // 새로운 레벨에 맞는 시간 설정
 		timerLabel.setText("남은 시간: " + remainingTime + "초");
 		startTimer(); // 타이머 재시작
 	}

    public void updateScore(int score) {
        this.score = score;
        scoreLabel.setText("점수: " + score);
    }
    
    public int getElapsedTime() {
		return elapsedTime;
	}

    public int getLevel() {
        return this.level;
    }

    // 게임이 끝났을 때 호출될 메서드, level을 전달하여 종료
 	public void gameClear() {
 		stopTimer();
 		score += remainingTime; // 남은 시간을 보너스로 추가
 		JOptionPane.showMessageDialog(this, "축하합니다! 게임을 클리어했습니다! 보너스 점수: " + remainingTime);
 		mainFrame.endGame(score, level);
 	}

    public int getScore() {
        return score;
    }
}