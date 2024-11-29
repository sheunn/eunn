package login;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable { // 사용자 정보
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String pw;
	private String name;
	private int score;
	private int point;
	int totalPlayTime;
	private int previewUsageCount;
	private int timeAddUsageCount;

	public Player(String id, String pw, String name, int totalPlayTime, int previewUsageCount, int timeAddUsageCount) {
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.score = 0; // 기본 점수 초기화
		this.totalPlayTime = totalPlayTime;
		this.previewUsageCount = previewUsageCount;
		this.timeAddUsageCount = timeAddUsageCount;
	}

	public Player(String name, int score) {
		this.name = name;
		this.score = score;
	}

	public Player(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public int getPoint() {
		return point;
	}

	public void addPoint(int point) {
		this.point += point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getTotalPlayTime() {
		return totalPlayTime;
	}

	public void addPlayTime(int time) {
		this.totalPlayTime += time;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Player player = (Player) o;
		return id.equals(player.id); // 아이디로 비교
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public int getPreviewUsageCount() {
		return previewUsageCount;
	}

	public void setPreviewUsageCount(int previewUsageCount) {
		this.previewUsageCount = previewUsageCount;
	}

	public int getTimeAddUsageCount() {
		return timeAddUsageCount;
	}

	public void setTimeAddUsageCount(int timeAddUsageCount) {
		this.timeAddUsageCount = timeAddUsageCount;
	}
}