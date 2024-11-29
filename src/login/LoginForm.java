package login;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import minesweeper.Main;

public class LoginForm extends JFrame { // 로그인 클래스

	private static final long serialVersionUID = -6181795004496391657L;
	// 사용자의 아이디와 비밀번호 정보를 저장하는 데이터 베이스 역할
	private PlayerDataSet players;
	private JLabel lblId;
	private JLabel lblPw;
	private JTextField tfId;
	private JPasswordField tfPw;
	private JButton btnLogin;
	private JButton btnJoin;

	private Main mainApp;

	public LoginForm(Main mainApp, PlayerDataSet playerDataSet) {
		this.mainApp = mainApp;
		this.players = playerDataSet;

		init();
		setDisplay();
		addListeners();
		showFrame();
	}

	public void init() {
		// 사이즈 통합
		Dimension lblSize = new Dimension(80, 30);
		int tfSize = 10;
		Dimension btnSize = new Dimension(100, 25);

		lblId = new JLabel("아이디");
		lblId.setPreferredSize(lblSize);
		lblPw = new JLabel("비밀번호");
		lblPw.setPreferredSize(lblSize);

		tfId = new JTextField(tfSize);
		tfPw = new JPasswordField(tfSize);

		btnLogin = new JButton("로그인");
		btnLogin.setPreferredSize(btnSize);
		btnJoin = new JButton("회원가입");
		btnJoin.setPreferredSize(btnSize);

	}

	public PlayerDataSet getUsers() { // 사용자 데이터베이스
		return players;
	}

	public String getTfId() { // 텍스트 필드의 입력값 반환
		return tfId.getText();
	}

	public void setDisplay() { // 화면 구성
		// FlowLayout 왼쪽 정렬
		FlowLayout flowLeft = new FlowLayout(FlowLayout.LEFT);

		// pnlNorth(pnlId, pnlPw)
		JPanel pnlNorth = new JPanel(new GridLayout(0, 1));

		JPanel pnlId = new JPanel(flowLeft);
		pnlId.add(lblId);
		pnlId.add(tfId);

		JPanel pnlPw = new JPanel(flowLeft);
		pnlPw.add(lblPw);
		pnlPw.add(tfPw);

		pnlNorth.add(pnlId);
		pnlNorth.add(pnlPw);

		JPanel pnlSouth = new JPanel();
		pnlSouth.add(btnLogin);
		pnlSouth.add(btnJoin);

		pnlNorth.setBorder(new EmptyBorder(0, 20, 0, 20));
		pnlSouth.setBorder(new EmptyBorder(0, 0, 10, 0));

		add(pnlNorth, BorderLayout.NORTH);
		add(pnlSouth, BorderLayout.SOUTH);

	}

	public void addListeners() {

		btnJoin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new JoinForm(LoginForm.this);
				tfId.setText("");
				tfPw.setText("");
			}
		});
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = tfId.getText();
				String password = String.valueOf(tfPw.getPassword());

				if (id.isEmpty()) {
					JOptionPane.showMessageDialog(LoginForm.this, "아이디를 입력하세요.", "로그인 폼", JOptionPane.WARNING_MESSAGE);
				} else if (players.contains(new Player(id))) {
					Player player = players.getUser(id); // 해당 id의 Player 객체 가져오기
					if (password.isEmpty()) {
						JOptionPane.showMessageDialog(LoginForm.this, "비밀번호를 입력하세요.", "로그인 폼",
								JOptionPane.WARNING_MESSAGE);
					} else if (!player.getPw().equals(password)) {
						JOptionPane.showMessageDialog(LoginForm.this, "비밀번호가 일치하지 않습니다.");
					} else {
						System.out.println("로그인 성공: " + player.getName());
						mainApp.loginSuccessful(player); // Player 객체를 전달
						setVisible(false);
					}
				} else {
					JOptionPane.showMessageDialog(LoginForm.this, "존재하지 않는 아이디입니다.");
				}
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				int choice = JOptionPane.showConfirmDialog(LoginForm.this, "로그인 프로그램을 종료합니다.", "종료",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (choice == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
	}

	public void showFrame() {
		setTitle("Login");
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
}