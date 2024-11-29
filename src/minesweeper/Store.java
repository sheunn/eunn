package minesweeper;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import login.Player;
import login.ScoreManager;
import login.TimeManager;

public class Store extends JPanel {
   private static final long serialVersionUID = 1L;
   private JLabel lblStoreTitle, lblPoints, lblPreviews, lblTimeAdds;
   private JButton btnBuyPreview, btnBuyTimeAdd, backButton;
   private Main mainFrame;
   private GamePanel gamePanel;
   private TimeManager timeManager;
   private ScoreManager scoreManager;
   private Player player;
   private JFrame parentFrame;
   private int previewUsageCount;
   private int timeAddUsageCount;
   private Image backgroundImage; // 배경 이미지 추가
   private Image storeImage; // 상단 "스토어.png" 이미지 추가
   
   public Store(JFrame parentFrame, Main mainFrame, ScoreManager scoreManager, GamePanel gamePanel, Player player,
         TimeManager timeManager) {
      this.parentFrame = parentFrame;
      this.mainFrame = mainFrame;
      this.gamePanel = gamePanel;
      this.timeManager = timeManager;
      this.scoreManager = scoreManager;
      this.player = player;
      this.previewUsageCount = mainFrame.getPreviewUsageCount();
      this.timeAddUsageCount = mainFrame.getTimeAddUsageCount();


      // 배경 이미지 로드
      try {
          backgroundImage = new ImageIcon(getClass().getResource("/img/블러배경.png")).getImage();
      } catch (Exception e) {
          System.err.println("배경 이미지 로드 실패: " + e.getMessage());
      }

      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // 수직 배치

      add(Box.createVerticalGlue()); // 상단 여백
      
   // 스토어 이미지
      try {
         ImageIcon storeIcon = new ImageIcon(getClass().getResource("/img/스토어.png"));
         Image scaledImage = storeIcon.getImage().getScaledInstance(440, 110, Image.SCALE_SMOOTH);
         JLabel lblStoreTitle = new JLabel(new ImageIcon(scaledImage));
         lblStoreTitle.setAlignmentX(CENTER_ALIGNMENT);
         add(lblStoreTitle);
      } catch (Exception e) {
         System.err.println("스토어 이미지 로드 실패: " + e.getMessage());
      }

      add(Box.createVerticalStrut(20)); // 간격 조정

   // 현재 포인트 라벨
      lblPoints = new JLabel("💎 POINT 💎 : " + player.getPoint());
      lblPoints.setFont(new Font("SansSerif", Font.BOLD, 30));
      lblPoints.setForeground(new java.awt.Color(0, 255, 255)); 
      lblPoints.setAlignmentX(CENTER_ALIGNMENT);
      add(lblPoints);

      add(Box.createVerticalStrut(15)); // 간격 조정
      
      // 미리보기 보유 라벨
      lblPreviews = new JLabel("미리보기 🔍 : " + previewUsageCount + "개");
      lblPreviews.setFont(new Font("SansSerif", Font.BOLD, 23));
      lblPreviews.setForeground(java.awt.Color.WHITE);
      lblPreviews.setAlignmentX(CENTER_ALIGNMENT);
      add(lblPreviews);

      add(Box.createVerticalStrut(10)); // 간격 조정


      // 시간추가 보유 라벨
      lblTimeAdds = new JLabel("시간추가 ⏰ : " + timeAddUsageCount + "개");
      lblTimeAdds.setFont(new Font("SansSerif", Font.BOLD, 23));
      lblTimeAdds.setForeground(java.awt.Color.WHITE);
      lblTimeAdds.setAlignmentX(CENTER_ALIGNMENT);
      add(lblTimeAdds);

      add(Box.createVerticalStrut(10)); // 간격 조정


   // 미리보기 구매 버튼 (Hover 적용)
      btnBuyPreview = createHoverImageButton(
         "/img/노누끼미리보기.png",
         "/img/노누끼커서미리보기.png",
         e -> buyPreview()
      );
      btnBuyPreview.setAlignmentX(CENTER_ALIGNMENT);
      add(btnBuyPreview);

      add(Box.createVerticalStrut(10)); // 간격 조정

      // 시간추가 구매 버튼 (Hover 적용)
      btnBuyTimeAdd = createHoverImageButton(
         "/img/노누끼시간추가.png",
         "/img/노누끼커서시간추가.png",
         e -> buyTimeAdd()
      );
      btnBuyTimeAdd.setAlignmentX(CENTER_ALIGNMENT);
      add(btnBuyTimeAdd);

      add(Box.createVerticalStrut(10)); // 간격 조정

      // 뒤로가기 버튼 (Hover 적용)
      backButton = createHoverImageButton(
         "/img/BACK.png",
         "/img/커서BACK.png",
         e -> goBack()
      );
      backButton.setAlignmentX(CENTER_ALIGNMENT);
      add(backButton);

      add(Box.createVerticalGlue()); // 하단 여백
   }
   

   // 이미지 버튼 생성 메서드
   private JButton createImageButton(String imagePath, java.awt.event.ActionListener actionListener) {
         JButton button = new JButton();
         try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(imagePath));
            Image scaledImage = originalIcon.getImage().getScaledInstance(250, 95, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.addActionListener(actionListener);
         } catch (Exception e) {
            System.err.println("버튼 이미지 로드 실패: " + e.getMessage());
         }
         return button;
      }

      @Override
      protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
         }
      }

      // Hover 이미지 버튼 생성 메서드
      private JButton createHoverImageButton(String defaultImagePath, String hoverImagePath, java.awt.event.ActionListener actionListener) {
         JButton button = new JButton();
         try {
            // 기본 이미지 설정
            ImageIcon defaultIcon = new ImageIcon(getClass().getResource(defaultImagePath));
            Image scaledDefaultImage = defaultIcon.getImage().getScaledInstance(250, 95, Image.SCALE_SMOOTH);

            // Hover 이미지 설정
            ImageIcon hoverIcon = new ImageIcon(getClass().getResource(hoverImagePath));
            Image scaledHoverImage = hoverIcon.getImage().getScaledInstance(250, 95, Image.SCALE_SMOOTH);

            // 버튼 설정
            button.setIcon(new ImageIcon(scaledDefaultImage));
            button.setRolloverIcon(new ImageIcon(scaledHoverImage));
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.addActionListener(actionListener);
         } catch (Exception e) {
            System.err.println("버튼 이미지 로드 실패: " + e.getMessage());
         }
         return button;
      }


   private void buyPreview() {
      if (player.getPoint() < 100) {
         JOptionPane.showMessageDialog(mainFrame, "포인트가 부족합니다.");
      } else {
         previewUsageCount++;
         JOptionPane.showMessageDialog(mainFrame, "구매가 완료되었습니다. 보유개수 : " + previewUsageCount);
         player.setPoint(player.getPoint() - 100);
         lblPoints.setText("💎 POINT 💎 : " + player.getPoint());
         lblPreviews.setText("미리보기 🔍: " + previewUsageCount + "개");
         mainFrame.setPreviewUsageCount(previewUsageCount); // GamePanel에 업데이트
      }
   }

   private void buyTimeAdd() {
      if (player.getPoint() < 100) {
         JOptionPane.showMessageDialog(mainFrame, "포인트가 부족합니다.");
      } else {
         timeAddUsageCount++;
         JOptionPane.showMessageDialog(mainFrame, "구매가 완료되었습니다. 보유개수 : " + timeAddUsageCount);
         player.setPoint(player.getPoint() - 100);
         lblPoints.setText("💎 POINT 💎 : " + player.getPoint());
         lblTimeAdds.setText("시간추가 ⏰ : " + timeAddUsageCount + "개");
         mainFrame.setTimeAddUsageCount(timeAddUsageCount); // GamePanel에 업데이트
      }
   }

   private void goBack() {
       parentFrame.setContentPane(
           new MyPage(parentFrame, mainFrame, mainFrame.getScoreManager(), player, player.getName(), gamePanel, timeManager));
       parentFrame.revalidate();
       parentFrame.repaint();
   }

   public int getPreviewUsageCount() {
      return previewUsageCount;
   }

   public int getTimeAddUsageCount() {
      return timeAddUsageCount;
   }
}