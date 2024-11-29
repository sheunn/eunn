package minesweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class MineGrid {
	private int gridSize;
	int numMines;
	int remainingMines; // 남은 지뢰 개수 변수
	private JPanel panel;
	private CellButton[][] buttons;
	private boolean[][] mines;
	private Main mainFrame;
	private GamePanel gamePanel;
	private boolean previewMode = false; // 미리보기 모드 상태
	private boolean isFirstClick = true; // 첫 클릭 여부를 저장
	private int score;
	private int remainingCells;

	public MineGrid(int gridSize, int numMines, Main mainFrame, GamePanel gamePanel) {
		this.gridSize = gridSize;
		this.numMines = numMines;
		this.mainFrame = mainFrame;
		this.gamePanel = gamePanel;
		this.buttons = new CellButton[gridSize][gridSize];
		this.mines = new boolean[gridSize][gridSize];
		this.panel = new JPanel(new GridLayout(gridSize, gridSize));
		this.score = 0;
		this.remainingCells = gridSize * gridSize - numMines;
		this.remainingMines = numMines;
		
		panel = new JPanel(new GridLayout(gridSize, gridSize));
        panel.setBackground(new Color(76,64,139)); // 배경 색상
        
		initializeGrid();
	}

	public JPanel getPanel() {
		return panel;
	}

	public boolean isPreviewModeActive() {
		return previewMode;
	}

	public void setPreviewMode(boolean previewMode) {
		this.previewMode = previewMode;
	}

	public int getRemainingMines() {
		return remainingMines;
	}

	public void updateRemainingMines(boolean isFlagSet) {
		if (isFlagSet) {
			remainingMines--;
		} else {
			remainingMines++;
		}
		gamePanel.updateMineCount(remainingMines); // GamePanel에 업데이트 요청
	}

	public void reset() {
		panel.removeAll();
		mines = new boolean[gridSize][gridSize];
		initializeGrid();
		score = 0;
		remainingCells = gridSize * gridSize - numMines;
		gamePanel.updateScore(score);
		panel.revalidate();
		panel.repaint();
		isFirstClick = true; // 첫 클릭 플래그 초기화
	}

	// 격자 초기화
	private void initializeGrid() {
	    for (int row = 0; row < gridSize; row++) {
	        for (int col = 0; col < gridSize; col++) {
	            CellButton button = new CellButton(row, col, this);
	            buttons[row][col] = button;
	            panel.add(button); // 버튼을 패널에 추가
	        }
	    }

	    // 패널의 크기와 레이아웃 설정
	    panel.setPreferredSize(new Dimension(gridSize * 40, gridSize * 40)); // 버튼 크기와 일치
	    panel.revalidate();
	    panel.repaint();
	}

	private void placeMines(int safeRow, int safeCol) {
		Random rand = new Random();
		int minesPlaced = 0;
		
		while (minesPlaced < numMines) {
			int row = rand.nextInt(gridSize);
			int col = rand.nextInt(gridSize);
			
			// 안전 지대는 지뢰를 배치하지 않음
            if (!mines[row][col] && !(row == safeRow && col == safeCol)) {
                mines[row][col] = true;
                minesPlaced++;
            }
		}
	}

	public void previewCell(int row, int col) {
		if (previewMode) {
			CellButton button = buttons[row][col];
			if (button.isEnabled()) {
				String originalText = button.getText();
				Color originalColor = button.getBackground();
				Border originalBorder = button.getBorder(); // 기존 테두리 저장

				// 칸의 내용과 스타일 설정
				if (isMine(row, col)) {
					button.setBackground(Color.RED);
					button.setText("💣");
				} else {
					int surroundingMines = countSurroundingMines(row, col);
					button.setText(surroundingMines == 0 ? "" : String.valueOf(surroundingMines)); // 순수 텍스트 사용
					button.setBackground(Color.GRAY); // 미리보기 시 회색 표시
					button.setFocusPainted(false); // 포커스 페인팅 제거
				}

				// 칸을 3초 후에 원래 상태로 되돌림
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						SwingUtilities.invokeLater(() -> {
							button.setText(originalText);
							button.setBackground(originalColor);
							button.setBorder(originalBorder); // 원래 테두리 복구
							button.setEnabled(true);
						});
					}
				}, 3000);
				previewMode = false; // 미리보기 모드 해제
			}
		}
	}

	public int countSurroundingMines(int row, int col) {
		int count = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				int newRow = row + i;
				int newCol = col + j;
				
				if (newRow >= 0 && newRow < gridSize && newCol >= 0 && newCol < gridSize && mines[newRow][newCol]) {
					count++;
				}
			}
		}
		return count;
	}
	
	public void revealAllMines() {
		for (int row = 0; row < gridSize; row++) {
			for (int col = 0; col < gridSize; col++) {
				if (mines[row][col]) {
					buttons[row][col].revealMine();
				}
			}
		}
	}

	public boolean isMine(int row, int col) {
		return mines[row][col];
	}

	public void incrementScore() {
		score += 10;
		gamePanel.updateScore(score);
	}

	public void revealCell(int row, int col) {
        // 첫 클릭 시 지뢰 배치
        if (isFirstClick) {
            placeMines(row, col);
            isFirstClick = false; // 첫 클릭 플래그 해제
        }

        // 이미 공개된 셀은 무시
        if (!buttons[row][col].isEnabled()) {
            return;
        }

        if (mines[row][col]) { // 지뢰 클릭 시
            revealAllMines(); // 모든 지뢰 공개
            gamePanel.stopTimer();
            JOptionPane.showMessageDialog(gamePanel, "지뢰를 클릭했습니다! 게임 오버!");
            gamePanel.mainFrame.endGame(gamePanel.getScore(), gamePanel.getLevel());
        } else {
            int mineCount = countSurroundingMines(row, col);
            buttons[row][col].revealContent();
            remainingCells--;
            incrementScore();
            checkGameClear();

            // 주변 지뢰 수가 0인 경우 주변 셀도 자동 공개
            if (mineCount == 0) {
                for (int i = row - 1; i <= row + 1; i++) {
                    for (int j = col - 1; j <= col + 1; j++) {
                        if (i >= 0 && i < gridSize && j >= 0 && j < gridSize && !(i == row && j == col)) {
                            revealCell(i, j);
                        }
                    }
                }
            }
        }
    }


	public void updateRemainingMines(int change) {
		remainingMines += change;
		gamePanel.updateMineCount(remainingMines); // GamePanel에서 남은 지뢰 개수를 갱신
	}

	private void checkGameClear() {
		if (remainingCells == 0) {
			gameClear();
		}
	}

	private void gameClear() {
		gamePanel.gameClear();
	}

	public Main getMainFrame() {
		return mainFrame;
	}

	public GamePanel getGamePanel() {
		return gamePanel;
	}

	public int getScore() {
		return score;
	}

	public int getGridSize() {
		return gridSize;
	}
}