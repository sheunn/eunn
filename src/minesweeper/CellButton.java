package minesweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

public class CellButton extends JButton {
    private static final long serialVersionUID = 1L;
    private int row;
    private int col;
    private MineGrid grid;
    private boolean isFlagged = false;

    public CellButton(int row, int col, MineGrid grid) {
        this.row = row;
        this.col = col;
        this.grid = grid;
        
        // 버튼 크기와 스타일 설정
        setPreferredSize(new Dimension(30, 30));
        setFont(new Font("Dialog", Font.BOLD, 22));
        setHorizontalTextPosition(SwingConstants.CENTER);
        setBackground(new Color(16,14,46)); // 어두운 배경색
        setForeground(new Color(0, 251, 255)); // 깃발 색상
        setFocusPainted(false); // 포커스 효과 제거

        // 입체적인 테두리 설정
        setBorder(new BevelBorder(BevelBorder.RAISED)); // Raised 테두리로 입체감 추가
        setBorder(new LineBorder(new Color(97,93,161), 1)); // 테두리 색상과 두께 설정


        // 클릭 이벤트 리스너 추가
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (isFlagged) {
                        grid.updateRemainingMines(false);
                        isFlagged = false;
                        setText("");
                    }
                    if (grid.isPreviewModeActive()) {
                        grid.previewCell(row, col);
                    } else {
                       // 좌클릭 시 버튼 스타일 변경
                        grid.revealCell(row, col); // 셀의 숫자와 스타일 업데이트
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    toggleFlag();
                }
            }
        });
    }

    private void toggleFlag() {
        if (isEnabled() && (grid.remainingMines > 0 || isFlagged)) {
            isFlagged = !isFlagged;
            setText(isFlagged ? "🚩" : "");
            grid.updateRemainingMines(isFlagged ? -1 : 1);
            revalidate();
            repaint();
        }
    }

    // 숫자
    public void revealContent() {
    	int surroundingMines = grid.countSurroundingMines(row, col);
        if (surroundingMines > 0) {
            // 숫자에 맞는 이미지를 설정
            setIcon(getImageForNumber(surroundingMines)); // 숫자별 이미지 설정
        } else {
            setText(""); // 숫자가 없으면 텍스트를 비웁니다.
        }
        setEnabled(false); // 버튼 비활성화
        setBorder(new BevelBorder(BevelBorder.LOWERED)); // 눌린 효과
        setBackground(new Color(85,83,130)); // 배경색 변경
        revalidate();
        repaint();
    }
    
    private ImageIcon getImageForNumber(int number) {
        String imagePath = "/img/" + number + ".png"; // 이미지 경로 설정
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); // 버튼 크기에 맞게 조정
        return new ImageIcon(scaledImage);
    }

    // 지뢰
    public void revealMine() {
        setText("💣");
        setBackground(Color.RED);
        setForeground(Color.YELLOW);
        setEnabled(false);
        revalidate();
        repaint();
     }

}