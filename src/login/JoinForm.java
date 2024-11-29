package login;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class JoinForm extends JDialog { // 회원가입 클래스
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LoginForm owner;
	private PlayerDataSet players;

	private JLabel lblTitle;
	private JLabel lblId; // 아이디
	private JLabel lblPw; // 비밀번호
	private JLabel lblRe; // 비밀번호 재입력
	private JLabel lblName; // 이름

	// 텍스트/비밀번호 필드
	private JTextField tfId;
	private JPasswordField tfPw;
	private JPasswordField tfRe;
	private JTextField tfName;

	// 가입/취소 버튼
	private JButton btnJoin;
	private JButton btnCancel;

	public JoinForm(LoginForm owner) {
		super(owner, "Join", true);
		this.owner = owner;
		players = owner.getUsers();

		init();
		setDisplay();
		addListeners();
		showFrame();
	}

	private void init() { // 크기 고정
		int tfSize = 10;
		Dimension lblSize = new Dimension(80, 35);
		Dimension btnSize = new Dimension(100, 25);

		lblTitle = new JLabel("정보를 입력해주세요");
		lblTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		lblId = new JLabel("아이디", JLabel.LEFT);
		lblId.setPreferredSize(lblSize);
		lblPw = new JLabel("비밀번호", JLabel.LEFT);
		lblPw.setPreferredSize(lblSize);
		lblRe = new JLabel("재확인", JLabel.LEFT);
		lblRe.setPreferredSize(lblSize);
		lblName = new JLabel("이름", JLabel.LEFT);
		lblName.setPreferredSize(lblSize);

		tfId = new JTextField(tfSize);
		tfPw = new JPasswordField(tfSize);
		tfRe = new JPasswordField(tfSize);
		tfName = new JTextField(tfSize);

		// ButtonGroup group = new ButtonGroup();

		btnJoin = new JButton("회원가입");
		btnJoin.setPreferredSize(btnSize);
		btnCancel = new JButton("취소");
		btnCancel.setPreferredSize(btnSize);

	}

	private void setDisplay() { // 화면 구성
		// FlowLayout 왼쪽 정렬
		FlowLayout flowLeft = new FlowLayout(FlowLayout.LEFT);

		// pnlMain(pnlMNorth / pnlMCenter / pnlMSouth)
		JPanel pnlMain = new JPanel(new BorderLayout());

		// pnlMNorth(lblTitle)
		JPanel pnlMNorth = new JPanel(flowLeft);
		pnlMNorth.add(lblTitle);

		// pnlMCenter(pnlId / pnlPw / pnlRe / pnlName / pnlNickName)
		JPanel pnlMCenter = new JPanel(new GridLayout(0, 1));
		JPanel pnlId = new JPanel(flowLeft);
		pnlId.add(lblId);
		pnlId.add(tfId);

		JPanel pnlPw = new JPanel(flowLeft);
		pnlPw.add(lblPw);
		pnlPw.add(tfPw);

		JPanel pnlRe = new JPanel(flowLeft);
		pnlRe.add(lblRe);
		pnlRe.add(tfRe);

		JPanel pnlName = new JPanel(flowLeft);
		pnlName.add(lblName);
		pnlName.add(tfName);

		pnlMCenter.add(pnlId);
		pnlMCenter.add(pnlPw);
		pnlMCenter.add(pnlRe);
		pnlMCenter.add(pnlName);

		// pnlMain
		pnlMain.add(pnlMNorth, BorderLayout.NORTH);
		pnlMain.add(pnlMCenter, BorderLayout.CENTER);

		// pnlSouth(btnJoin / btnCancel)
		JPanel pnlSouth = new JPanel();
		pnlSouth.add(btnJoin);
		pnlSouth.add(btnCancel);

		// 화면 테두리의 간격을 주기 위해 설정 (insets 사용 가능)
		pnlMain.setBorder(new EmptyBorder(0, 20, 0, 20));
		pnlSouth.setBorder(new EmptyBorder(0, 0, 10, 0));

		add(pnlMain, BorderLayout.NORTH);
		add(pnlSouth, BorderLayout.SOUTH);
	}

	private void addListeners() { // 회원가입 창이 닫힐 때
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				dispose();
				owner.setVisible(true);
			}
		});
		// 창을 닫고 LoginForm 창 띄우기
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				dispose();
				owner.setVisible(true);
			}
		});
		// 입력된 정보를 검증
		btnJoin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (isBlank()) {
					JOptionPane.showMessageDialog(JoinForm.this, "모든 정보를 입력해주세요.");
				} else {
					if (players.isIdOverlap(tfId.getText())) {
						JOptionPane.showMessageDialog(JoinForm.this, "이미 존재하는 Id입니다.");
						tfId.requestFocus();
					} else if (!String.valueOf(tfPw.getPassword()).equals(String.valueOf(tfRe.getPassword()))) {
						JOptionPane.showMessageDialog(JoinForm.this, "비밀번호가 일치하지 않습니다.");
						tfPw.requestFocus();
					} else {
						// 사용자 정보 추가
						players.addUsers(new Player(tfId.getText(), String.valueOf(tfPw.getPassword()),
								tfName.getText(), 0, 0, 0));

						// 저장 확인을 위한 디버그 메시지
						System.out.println("회원가입 성공: " + tfId.getText());

						JOptionPane.showMessageDialog(JoinForm.this, "회원가입을 완료했습니다!");
						dispose();
						owner.setVisible(true);
					}
				}
			}
		});
	}

	public boolean isBlank() { // 입력 필드가 비어 있는지 확인
		boolean result = false;
		if (tfId.getText().isEmpty()) {
			tfId.requestFocus();
			return true;
		}
		if (String.valueOf(tfPw.getPassword()).isEmpty()) {
			tfPw.requestFocus();
			return true;
		}
		if (String.valueOf(tfRe.getPassword()).isEmpty()) {
			tfRe.requestFocus();
			return true;
		}
		if (tfName.getText().isEmpty()) {
			tfName.requestFocus();
			return true;
		}
		return result;
	}

	private void showFrame() { // 화면에 표시
		pack();
		setLocationRelativeTo(owner);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
}