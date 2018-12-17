import java.awt.Color;

class Tile {
	int value;//

	public Tile() {
		this(0);//빈타일 게임에서 안보임
	}

	public Tile(int num) {
		value = num;
	}

	public boolean isEmpty() {
		return value == 0;//타일 value가 0이면 빈걸로 판단
	}

	public Color getForeground() {
		return value < 16 ? new Color(0x776e65) : new Color(0xf9f6f2);//글자 폰트색 16부터 색이 진해져서 가독성이 떨어짐
	}

	public Color getBackground() {
		switch (value) {
		case 2:
			return new Color(0xeee4da);
		case 4:
			return new Color(0xede0c8);
		case 8:
			return new Color(0xf2b179);
		case 16:
			return new Color(0xf59563);
		case 32:
			return new Color(0xf67c5f);
		case 64:
			return new Color(0xf65e3b);
		case 128:
			return new Color(0xedcf72);
		case 256:
			return new Color(0xedcc61);
		case 512:
			return new Color(0xedc850);
		case 1024:
			return new Color(0xedc53f);
		case 2048:
			return new Color(0xedc22e);
		}
		return new Color(0xcdc1b4);
	}
}
