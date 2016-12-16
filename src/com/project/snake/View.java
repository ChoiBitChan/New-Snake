package com.project.snake;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class View implements Initializable {

	@FXML
	private HBox hbox;
	@FXML
	private GridPane grid;
	@FXML
	private VBox vbox;
	@FXML
	private Label Slabel; // ���� ���̺�
	@FXML
	private Label Blabel; // ���ʽ� ���̺�
	@FXML
	private Label Elabel; // ���� ���� ���̺�

	int xCnt = 20; // ĭ ����
	int yCnt = 20; // ĭ ����

	int x = 0; // ��ǥ
	int y = 0; // ��ǥ

	int ranX = 0; // ���� ��ǥ
	int ranY = 0; // ���� ��ǥ
	int ranC = 0; // random color
	
	int itemX = 0;
	int itemY = 0;
	int eatTime;

	int rect_size = 20; // ������

	int pScore;
	int pBonus;
	int pEat;
	boolean pKey = true;
	boolean eatItem = false;

	int bonusCnt = 101;
	int score = 0;
	int eat = 0;

	Rectangle[][] panel;
	Point random; // �������� ������ ���� ��ǥ
	Point head; // ��� ��ǥ
	Point item;
	// CountThread CntTh; // ������
	Paint randomcolor; // �������� ������ ���� ����

	Color bg_color = Color.BLACK; // ��� ��
	Color bord_color = Color.WHITE; // �׵θ� ��
	Color hd_color = Color.YELLOW; // ��� ��

	LinkedList<Point> headlist; // ��� ����Ʈ
	ArrayList<Color> bodylist; // �������� ������ ���� ����Ʈ
	ColorClass colorclass; // �÷� ����Ʈ

	Timeline goingTL;
	Timeline countTL;
	int time;

	// ����
	public enum Direction {
		Up, Down, Right, Left // ������ ����
	}

	Direction direction;

	@Override
	public void initialize(URL location, ResourceBundle resources) { // �ʱ� ����

		grid.setFocusTraversable(true);
		grid.requestFocus();

		/*
		 * Rectangle rect = new Rectangle(20,20); grid.add(rect, 0, 0);
		 * 
		 * //�� ���氡�� rect = new Rectangle(20,20); rect.setFill(Color.RED);
		 * vbox.getChildren().add(rect);
		 */

		// CntTh = new CountThread(this); // ������ ��ü ����
		// CntTh.start(); // ������ ����

		Blabel.setStyle("-fx-font-size:25"); // ���ʽ� ���̺� ��Ʈ ������ ����
		Slabel.setStyle("-fx-font-size:25"); // ���ھ� ���̺� ��Ʈ ������ ����
		Elabel.setStyle("-fx-font-size:25");

		// �гλ���
		panel = new Rectangle[yCnt][xCnt];
		for (int i = 0; i < yCnt; i++) {
			for (int j = 0; j < xCnt; j++) {
				Rectangle rect = new Rectangle(rect_size, rect_size);
				rect.setFill(bg_color);
				rect.setStroke(bord_color);
				panel[i][j] = rect;

				grid.add(rect, j, i);

			}
		}

		colorclass = new ColorClass(); // �÷� ����Ʈ ��ü ����
		colorclass.createColor();

		grid.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.UP) { // ����Ű ���� ������ ��
				if (direction == Direction.Down) { // ���� ���� ���°� �Ʒ����̸�
					return; // ����Ű ������ ������ �ʰ� ����������
				} else { // �� �ܿ��� ����Ű�� ���� �� �ִ�
					direction = Direction.Up;
				}
			}
			if (e.getCode() == KeyCode.DOWN) {
				if (direction == Direction.Up) {
					return;
				} else {
					direction = Direction.Down;
				}
			}
			if (e.getCode() == KeyCode.RIGHT) {
				if (direction == Direction.Left) {
					return;
				} else {
					direction = Direction.Right;
				}
			}
			if (e.getCode() == KeyCode.LEFT) {
				if (direction == Direction.Right) {
					return;
				} else {
					direction = Direction.Left;
				}
			}
			if (e.getCode() == KeyCode.SPACE) {
			}
			if (e.getCode() == KeyCode.ENTER) { // ���͸� ������
				startGame(); // ������ ����ȴ�

				// if(CntTh.timeOnOff == false){
				// CntTh = new CountThread(this);
				// CntTh.timeOnOff = true;
				// CntTh.start();
				// }
				
				if (goingTL.getStatus() == Status.STOPPED) { // ���� ���� ���°� ���� �ִٸ�
					goingTL.play(); // Ÿ�Ӷ��� ����
					countTL.play();
				}
			}
			if (pKey) {
				if (e.getCode() == KeyCode.P) { // �Ͻ����� ��ư
					if (goingTL.getStatus() == Status.RUNNING) { 
						// ���� ���� ���°� ���� ���̶��
						goingTL.stop(); // �Ͻ� ����
						countTL.stop();

						// CntTh.timeOnOff = false; // ������ ����

						// pBonus = CntTh.bonusCnt;
						// pScore = CntTh.score;
						// pEat = CntTh.eat;

					} else if (goingTL.getStatus() == Status.STOPPED) {
						// ���� ���� ���°� ���� �ִٸ�
						
						goingTL.play(); // Ÿ�ζ��� �ٽ� ����
						countTL.play();

						// CntTh = new CountThread(this);
						// CntTh.timeOnOff = true;
						// CntTh.start();

						// CntTh.bonusCnt = pBonus;
						// CntTh.score = pScore;
						// CntTh.eat = pEat;

					}
				}
			}
		});

	}

	public void startGame() { // ���� ����
		for (int i = 0; i < yCnt; i++) { // ���͸� ������ ������ ���� �ǹǷ� �ǳڸ� ����ش�
			for (int j = 0; j < xCnt; j++) {
				panel[i][j].setFill(bg_color); // ȭ�� ��ĥ
			}
		}

		setHead(); // ��� ����
		setGoingTL(); // �ڵ� ������ ����
		setCountTL();
		random(); // ���� ����
	}

	public void GameOver() {
		goingTL.stop(); // ���� ����
		countTL.stop();
		pKey = false;
	}

	public void going() { // �ð���� ����

		if (direction == Direction.Up) {
			move(-1, 0);
		}
		if (direction == Direction.Right) {
			move(0, 1);
		}
		if (direction == Direction.Down) {
			move(1, 0);
		}
		if (direction == Direction.Left) {
			move(0, -1);
		}
	}

	public void timer() {
		Slabel.setText("SCORE : " + Integer.toString(score));
		Blabel.setText("BONUS : " + Integer.toString(bonusCnt));
		Elabel.setText("EAT : " + Integer.toString(eat));
	}

	public void move(int off_y, int off_x) { // �Լ��� �����Ѵ�

		// �̸� ������ ���� ���̳� �ڱ��ڽ��� �ִٸ� ���߰�, ������ �ִٸ� �������.
		int temp_y = head.getY() + off_y;
		int temp_x = head.getX() + off_x;
		if (temp_y < 0 || // ���ʿ� �ε����ų�
				temp_x < 0 || // ����
				temp_y > yCnt - 1 || // �Ʒ���
				temp_x > xCnt - 1) { // ������
			System.out.println("���ӿ���");
			GameOver();
			// CntTh.bonusCnt = 101; // ���ʽ� ī��Ʈ �ʱ�ȭ

			return;
		}
		for (int i = 0; i < headlist.size(); i++) { // ������ ���̸�ŭ �ݺ�
			int x = headlist.get(i).getX(); // ������� ��ǥ�� �־��ش�
			int y = headlist.get(i).getY();
			if ((temp_y == y) && (temp_x == x)) { // �̸� ���� ��ǥ�� ������ ��ġ�ϰ� �Ǹ� �״´�
				System.out.println("���ӿ���");
				GameOver();
				// CntTh.bonusCnt = 101;
			}
		}
		if (temp_y == random.getY() && temp_x == random.getX()) { // �̸� ���� ��ǥ��
																	// ���� �� ��
			// �Դ� ���� -> ������ ���ÿ� ��尡 �����
			headlist.add(random); // ���Ÿ� ���뿡 �߰�
			head = random; // ���Ÿ� �Ӹ��� �����
			score = score + bonusCnt; // ���� ���
			bonusCnt = 101; // �ٽ� �ʱ�ȭ
			eat++;
			System.out.println(eat);

			random(); // �԰� ���� ���ο� �Լ� ����
			// System.out.println("������� : " + headlist.size());
		}
		if (temp_y == item.getY() && temp_x == item.getX()){
			setItem();
			//�������� �����
			System.out.println("������ ����");
			//isItem = true
			eatItem(true);
			
		} else { // �⺻������ ������ ��
			// �����̴� �� ó�� ���̰� �ϱ� ���ؼ�
			// ���ο� ��带 ����� ���̰�
			Point pt = new Point(); // pt ��ü ����
			pt.setY(head.getY() + off_y);
			pt.setX(head.getX() + off_x);
			headlist.add(pt); // ����Ʈ�� pt �߰�
			head = pt;
			// ������ ����
			headlist.poll(); // ����Ʈ���� ����
		}

		clear(); // ȭ�� �ʱ�ȭ

		// ���� �׸���
		for (int i = headlist.size(); i > 0; i--) {
			int x = headlist.get(i - 1).getX();
			int y = headlist.get(i - 1).getY();
			if (i == headlist.size()) {
				panel[y][x].setFill(hd_color);
				// System.out.println("��常 ��ĥ");
			} else //if (i < headlist.size()) {
			{	
				if (eatItem == false) {
				panel[y][x].setFill(bodylist.get(bodylist.size() - i - 1));
				// System.out.println("������ ��ĥ");
				}
				if (eatItem == true) {
				panel[y][x].setFill(Color.BLACK);	
				}
			}
		}
	}

	public void clear() { // ȭ�� �ʱ�ȭ
		for (int i = 0; i < yCnt; i++) {
			for (int j = 0; j < xCnt; j++) {
				panel[i][j].setFill(bg_color); // ȭ�� ��ĥ
				if (random.getX() == j && random.getY() == i) { // ������ ����
					panel[i][j].setFill(randomcolor); // ȭ���� �������� ������ ���� ��ǥ�� ���� �� �ڸ��� �׷��ش�
				}
				if (item.getX() == j && item.getY() == i) { // ������ ����
					panel[i][j].setFill(Color.WHITE); // ȭ���� �������� ������ ���� ��ǥ�� ���� �� �ڸ��� �׷��ش�
				}
			}
		}
	}

	public void random() { // �������� ���� �����
		random = new Point(); // ��ǥ ��ü ����
		// �÷� ����Ʈ ������ ��ŭ�� ���� �߿� ���� ���� ����
		ranC = (int) (Math.random() * colorclass.colorlist.size());
		randomcolor = colorclass.colorlist.get(ranC); // ���� ���� ��ġ�� �ִ� ������ �����÷��� ����

		ranX = random.getX() + (int) (Math.random() * 20); // ��ǥ ����
		ranY = random.getY() + (int) (Math.random() * 20);
		panel[ranY][ranX].setFill(randomcolor); // ������ ��ǥ�� �����÷� �׸���
		random.setX(ranX); // ��ǥ ����
		random.setY(ranY);

		// �������� ���� ���Ű� �� ���� ���� ����� ���� �ʰ� �ϱ�
		for (int i = 0; i < headlist.size(); i++) { // �� ������ ��ǥ��
			int x = headlist.get(i).getX();
			int y = headlist.get(i).getY();
			if (x == ranX && y == ranY) { // ���� ��ǥ�� ��ġ�ϸ�
				random(); // �ٽ� ����
				return; // ������ �߿�!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			}
		}
		bodylist.add(colorclass.colorlist.get(ranC)); // ����� ����������� �������Ÿ���Ʈ�� �߰�
	}
	
	public void setItem() {
		item = new Point();
		itemX = item.getX() + (int) (Math.random() * 20); // ��ǥ ����
		itemY = item.getY() + (int) (Math.random() * 20);
		panel[itemY][itemX].setFill(Color.WHITE);
		item.setX(itemX); // ��ǥ ����
		item.setY(itemY);
		
		for (int i = 0; i < headlist.size(); i++) { // �� ������ ��ǥ��
			int x = headlist.get(i).getX();
			int y = headlist.get(i).getY();
			if (x == itemX && y == itemY) { // ���� ��ǥ�� ��ġ�ϸ�
				setItem(); // �ٽ� ����
				return; // ������ �߿�!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			}
		}
	}

	public void setHead() { // ��� �ʱ� ����
		head = new Point();

		// ��ġ ����
		x = head.getX() + (xCnt / 2);
		y = head.getY() + (yCnt / 2);
		panel[y][x].setFill(hd_color);
		head.setX(x);
		head.setY(y);

		// �ʱ�ȭ
		headlist = new LinkedList<>();
		bodylist = new ArrayList<>();
		headlist.add(head);
		direction = Direction.Up;
		time = 100;

		bonusCnt = 101; // ���ʽ� ī��Ʈ �ʱ� �� ����
		score = 0;
		eat = 0;
		
		pKey = true;

		// System.out.println(headlist.size());
	}

	public void counting() {
		
		if (bonusCnt > 10) {
			bonusCnt--;
			//�ð��� �þ����..... -> 2300 -> 3�� 30
			//if(isitem){
			//
			//3�ʰ� ������ isitem= false
			//
			//}
			//2300 stop
			timer();
		}
	}
	
	public void eatItem(boolean YesNo) {
		if (YesNo == true) {
		eatItem = true;
		eatTime = bonusCnt;
		}
		else if (YesNo == false) {
			
		}
	}

	public void setGoingTL() {
		if (goingTL == null) { // timeline�� �������� ����ǰ� �����
			goingTL = new Timeline();
			KeyFrame k = new KeyFrame(Duration.millis(time), e -> going());
			goingTL.getKeyFrames().add(k);
			goingTL.setCycleCount(Timeline.INDEFINITE); // �������� ����
			goingTL.play();
		}
	}

	public void setCountTL() {
		if (countTL == null) { // timeline�� �������� ����ǰ� �����
			countTL = new Timeline();
			KeyFrame k = new KeyFrame(Duration.millis(time), e -> counting());
			countTL.getKeyFrames().add(k);
			countTL.setCycleCount(Timeline.INDEFINITE); // �������� ����
			countTL.play();
		}
	}
}
