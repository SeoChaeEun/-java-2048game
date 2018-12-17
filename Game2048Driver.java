import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Game2048Driver {


	public static void main(String[] args) {
		JFrame game = new JFrame();
		game.setTitle("2048 Game");//상단바에 뜨게함
		game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);// 종료이후에 프로세스 완전히 끄기 
		game.setSize(350, 450); //창크기
		game.setResizable(false); //창 크기 조절 여기선 false라서 불가능 늘이기 줄이기

		game.add(new Game2048());// 창안에 게임 추가 위에서는 틀만 만들어놈
 
		game.setVisible(true);//프레임 띄우기
	}
}

