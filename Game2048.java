
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

public class Game2048 extends JPanel {
	private static final Color BG_COLOR = new Color(0xbbada0);
	private static final String FONT_NAME = "Arial";
	private static final int TILE_SIZE = 64;
	private static final int TILES_MARGIN = 16;
	//폰트 타일색깔 타일크기 등 설정
	private Tile[] myTiles;
	boolean myWin = false;
	boolean myLose = false;
	int myScore = 0;

	public Game2048() {//게임 드라이버에서 실행할 함수
		setPreferredSize(new Dimension(340, 400));//프레임 설정
		setFocusable(true);//키보드로 입력 받을수 있게함 
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					resetGame();//esc누르면 겜 다시시작
				}
				if (!canMove()) {
					myLose = true;//안줌직여 지면 패배
				}

				if (!myWin && !myLose) {//키 입력에 따른 움직임
					switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						left();
						break;
					case KeyEvent.VK_RIGHT:
						right();
						break;
					case KeyEvent.VK_DOWN:
						down();
						break;
					case KeyEvent.VK_UP:
						up();
						break;
					}
				}

				if (!myWin && !canMove()) {
					myLose = true;//움직여보고 승패 판단
				}

				repaint();
			}
		});
		resetGame();
	}

	public void resetGame() {
		myScore = 0;
		myWin = false;
		myLose = false;
		myTiles = new Tile[4 * 4];
		for (int i = 0; i < myTiles.length; i++) {
			myTiles[i] = new Tile();//전체타일 0으로 추가
		}
		addTile();
		addTile();//타일 2개 추가
	}

	public void left() {//병합과 생성
		boolean needAddTile = false;//꽉차있으면 타일생성하지 못하게 할려고
		for (int i = 0; i < 4; i++) {
			Tile[] line = getLine(i);//4개짜리 1차원 배열 받음 이줄을에서 더할수 있는걸 더함
			Tile[] merged = mergeLine(moveLine(line));
			setLine(i, merged);
			if (!needAddTile && !compare(line, merged)) {
				needAddTile = true;//꽉차있으면 타일생성하지 못하게 할려고
			}
		}

		if (needAddTile) {
			addTile();//랜덤한 위치에 타일생성
		}
	}

	public void right() {
		myTiles = rotate(180);//left 의 180도 바꿈
		left();
		myTiles = rotate(180);//타일 위치를 180도 바꿨기 때문에 다시 원상복귀
	}

	public void up() {
		myTiles = rotate(270);
		left();
		myTiles = rotate(90);
	}

	public void down() {
		myTiles = rotate(90);
		left();
		myTiles = rotate(270);
	}

	private Tile tileAt(int x, int y) {
		return myTiles[x + y * 4];
	}

	private void addTile() {//랜덤한 위치에 타일 생성
		List<Tile> list = availableSpace();
		if (!availableSpace().isEmpty()) {
			int index = (int) (Math.random() * list.size()) % list.size();
			Tile emptyTime = list.get(index);
			emptyTime.value = Math.random() < 0.9 ? 2 : 4;
		}
	}

	private List<Tile> availableSpace() {//현재 myTiles들을 저장하는 16개 리스트 생성이후 타일들을 하나씩 쌓아서 리스트 반환
		final List<Tile> list = new ArrayList<Tile>(16);
		for (Tile t : myTiles) {
			if (t.isEmpty()) {
				list.add(t);
			}
		}
		return list;
	}

	private boolean isFull() {//꽉찼는지 안찼는지 반환
		return availableSpace().size() == 0;
	}

	boolean canMove() {//움직일수있나없나 확인 tileAt으로 한줄씩 보고 12 23 34 이런식을 봐서 합쳐질수 없다 하면 false
		if (!isFull()) {
			return true;
		}
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				Tile t = tileAt(x, y);
				if ((x < 3 && t.value == tileAt(x + 1, y).value) || ((y < 3) && t.value == tileAt(x, y + 1).value)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean compare(Tile[] line1, Tile[] line2) {
		if (line1 == line2) {
			return true;
		} else if (line1.length != line2.length) {
			return false;
		}

		for (int i = 0; i < line1.length; i++) {
			if (line1[i].value != line2[i].value) {
				return false;
			}
		}
		return true;
	}

	private Tile[] rotate(int angle) {//위에 좌우상하 할때 일일이 하기 귀찮아서 우상하를 할때 아에 게임판을 돌려버림
		Tile[] newTiles = new Tile[4 * 4];
		int offsetX = 3, offsetY = 3;
		if (angle == 90) {
			offsetY = 0;//2차원 배열에서 x,y 의 y 값 변화  없음
		} else if (angle == 270) {
			offsetX = 0;//2차원 배열에서 x,y 의 x 값 변화  없음
		}

		double rad = Math.toRadians(angle);
		int cos = (int) Math.cos(rad);
		int sin = (int) Math.sin(rad);
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				int newX = (x * cos) - (y * sin) + offsetX;
				int newY = (x * sin) + (y * cos) + offsetY;
				newTiles[(newX) + (newY) * 4] = tileAt(x, y);
			}
		}//일일이 위치를 바꿈 예로 1,1은 1,4로
		return newTiles;
	}

	private Tile[] moveLine(Tile[] oldLine) {//안합쳐지면 움직여야하니까
		LinkedList<Tile> l = new LinkedList<Tile>();
		for (int i = 0; i < 4; i++) {
			if (!oldLine[i].isEmpty())
				l.addLast(oldLine[i]);
		}
		if (l.size() == 0) {
			return oldLine;
		} else {
			Tile[] newLine = new Tile[4];
			ensureSize(l, 4);
			for (int i = 0; i < 4; i++) {
				newLine[i] = l.removeFirst();//합쳐진거는 삭제 
			}
			return newLine;
		}
	}

	private Tile[] mergeLine(Tile[] oldLine) {//같은숫자 합치기
		LinkedList<Tile> list = new LinkedList<Tile>();
		for (int i = 0; i < 4 && !oldLine[i].isEmpty(); i++) {
			int num = oldLine[i].value;
			if (i < 3 && oldLine[i].value == oldLine[i + 1].value) {
				num *= 2;
				myScore += num;//스코어에 더해줌
				int ourTarget = 2048;
				if (num == ourTarget) {
					myWin = true;//합치고 보니 2048이면 승리
				}
				i++;
			}
			list.add(new Tile(num));
		}
		if (list.size() == 0) {
			return oldLine;
		} else {
			ensureSize(list, 4);
			return list.toArray(new Tile[4]);//링크리스트를 1차원 배열로 바꿈
		}
	}

	private static void ensureSize(java.util.List<Tile> l, int s) {//빈칸에 0타일 넣음
		while (l.size() != s) {
			l.add(new Tile());
		}
	}

	private Tile[] getLine(int index) {//한줄받기
		Tile[] result = new Tile[4];
		for (int i = 0; i < 4; i++) {
			result[i] = tileAt(i, index);
		}
		return result;
	}

	private void setLine(int index, Tile[] re) {
		System.arraycopy(re, 0, myTiles, index * 4, 4);
	}

	@Override
	public void paint(Graphics g) {//그리기
		super.paint(g);
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, this.getSize().width, this.getSize().height);
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				drawTile(g, myTiles[x + y * 4], x, y);
			}
		}
	}

	private void drawTile(Graphics g2, Tile tile, int x, int y) {//타일 승패 문구  그외 배경을 제외한 모든 그래픽
		Graphics2D g = ((Graphics2D) g2);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		int value = tile.value;
		int xOffset = offsetCoors(x);
		int yOffset = offsetCoors(y);
		g.setColor(tile.getBackground());
		g.fillRoundRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE, 14, 14);
		g.setColor(tile.getForeground());
		final int size = value < 100 ? 36 : value < 1000 ? 32 : 24;
		final Font font = new Font(FONT_NAME, Font.BOLD, size);
		g.setFont(font);

		String s = String.valueOf(value);
		final FontMetrics fm = getFontMetrics(font);

		final int w = fm.stringWidth(s);
		final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];

		if (value != 0)
			g.drawString(s, xOffset + (TILE_SIZE - w) / 2, yOffset + TILE_SIZE - (TILE_SIZE - h) / 2 - 2);

		if (myWin || myLose) {
			g.setColor(new Color(255, 255, 255, 30));
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(new Color(78, 139, 202));
			g.setFont(new Font(FONT_NAME, Font.BOLD, 48));
			if (myWin) {
				g.drawString("You won!", 68, 150);
			}
			if (myLose) {
				g.drawString("Game over!", 50, 130);
				g.drawString("You lose!", 64, 200);
			}
			if (myWin || myLose) {
				g.setFont(new Font(FONT_NAME, Font.PLAIN, 16));
				g.setColor(new Color(128, 128, 128, 128));
				g.drawString("Press ESC to play again", 80, getHeight() - 40);
			}
		}
		g.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
		g.drawString("Score: " + myScore, 200, 365);

	}

	private static int offsetCoors(int arg) {
		return arg * (TILES_MARGIN + TILE_SIZE) + TILES_MARGIN;
	}
}
