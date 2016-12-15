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
	
	
	int xCnt = 20; // ĭ ����
	int yCnt = 20; // ĭ ����

	int x = 0; // ��ǥ
	int y = 0; // ��ǥ

	int ranX = 0; // ���� ��ǥ
	int ranY = 0; // ���� ��ǥ
	int ranC = 0; // random color

	int rect_size = 20; // ������


	Rectangle[][] panel;
	Point random; // �������� ������ ���� ��ǥ
	Point head; // ��� ��ǥ
	TimeThread tt; // ������
	Paint randomcolor; // �������� ������ ���� ����
	
	Color bg_color = Color.BLACK; // ��� ��
	Color bord_color = Color.WHITE; // �׵θ� ��
	Color hd_color = Color.YELLOW; // ��� ��
	
	LinkedList<Point> headlist; // ��� ����Ʈ
	Colorlist colorlist; // �÷� ����Ʈ
	ArrayList<Color> bodylist; // �������� ������ ���� ����Ʈ
	
	Timeline timeline;
	int time;
	
	// ����
	public enum Direction {
		Up, Down, Right, Left // ������ ����
	}
	Direction direction;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) { // �ʱ� ����

		/*
		Rectangle rect = new Rectangle(20,20);
		grid.add(rect, 0, 0);
		
		//�� ���氡��
		rect = new Rectangle(20,20);
		rect.setFill(Color.RED);
		vbox.getChildren().add(rect);
		*/
		
		tt = new TimeThread(this); // ������ ��ü ����
		tt.start(); // ������ ����
		
		Blabel.setStyle("-fx-font-size:25"); // ���ʽ� ���̺� ��Ʈ ������ ����
		Slabel.setStyle("-fx-font-size:25"); // ���ھ� ���̺� ��Ʈ ������ ����
		
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
		
		colorlist = new Colorlist(); // �÷� ����Ʈ ��ü ����
		
		hbox.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.UP) { // ����Ű ���� ������ ��
				if(direction == Direction.Down){ // ���� ���� ���°� �Ʒ����̸�
					return; // ����Ű ������ ������ �ʰ� ����������
				} else { // �� �ܿ��� ����Ű�� ���� �� �ִ�
					direction = Direction.Up;
				}
			}
			if (e.getCode() == KeyCode.DOWN) {
				if(direction == Direction.Up){
					return;
				} else {
					direction = Direction.Down;
				}
			}
			if (e.getCode() == KeyCode.RIGHT) {
				if(direction == Direction.Left){
					return;
				} else {
					direction = Direction.Right;
				}
			}
			if (e.getCode() == KeyCode.LEFT) {
				if(direction == Direction.Right){
					return;
				} else {
					direction = Direction.Left;
				}
			}
			if (e.getCode() == KeyCode.SPACE) {
			}
			if (e.getCode() == KeyCode.ENTER) { // ���͸� ������
				startGame(); // ������ ����ȴ�
				tt.bonusCnt = 101; // ���ʽ� ī��Ʈ �ʱ� �� ����
				if (timeline.getStatus() == Status.STOPPED) { // ���� ���� ���°� ���� �ִٸ�
					timeline.play(); // Ÿ�Ӷ��� ����
				}
			}
			if (e.getCode() == KeyCode.P) { // �Ͻ����� ��ư
				if (timeline.getStatus() == Status.RUNNING) { // ���� ���� ���°� ���� ���̶��
					timeline.stop(); // �Ͻ� ����
				} else if (timeline.getStatus() == Status.STOPPED) { // ���� ���� ���°� ���� �ִٸ�
					timeline.play(); // Ÿ�ζ��� �ٽ� ����
				}
			}
		});
		
		
	}
	
	public void startGame(){ // ���� ����
		for (int i = 0; i < yCnt; i++) { // ���͸� ������ ������ ���� �ǹǷ� �ǳڸ� ����ش�
			for (int j = 0; j < xCnt; j++) {
				panel[i][j].setFill(bg_color); // ȭ�� ��ĥ
			}
		}
		
		setHead(); // ��� ����
		setAuto(); // �ڵ� ������ ����
		random(); // ���� ����
	}
	
	public void going(){ // �ð���� ����
		
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
	public void timer(){
		Blabel.setText("BONUS : " + Integer.toString(tt.bonusCnt));
		Slabel.setText("SCORE : " + Integer.toString(tt.score));
	}
	
	public void move(int off_y, int off_x){ // �Լ��� �����Ѵ�

		// �̸� ������ ���� ���̳� �ڱ��ڽ��� �ִٸ� ���߰�, ������ �ִٸ� �������.
		int temp_y = head.getY() + off_y;
		int temp_x = head.getX() + off_x;
		if (temp_y < 0 || // ���ʿ� �ε����ų� 
			temp_x < 0 || // ����
			temp_y > yCnt - 1 || // �Ʒ���
			temp_x > xCnt - 1) { // ������
				System.out.println("���ӿ���");
				timeline.stop(); // ���� ����
				tt.bonusCnt = 10; // ���ʽ� ī��Ʈ �ʱ�ȭ
			return;
		}
		for(int i = 0; i<headlist.size(); i++){ // ������ ���̸�ŭ �ݺ�
			int x = headlist.get(i).getX(); // ������� ��ǥ�� �־��ش�
			int y = headlist.get(i).getY();
			if((temp_y == y) && (temp_x == x)){ // �̸� ���� ��ǥ�� ������ ��ġ�ϰ� �Ǹ� �״´�
				System.out.println("���ӿ���");
				timeline.stop();
				tt.bonusCnt = 10;
			}
		}
		if (temp_y == random.getY() && temp_x == random.getX()) { // �̸� ���� ��ǥ�� ���� �� ��
			// �Դ� ���� -> ������ ���ÿ� ��尡 �����
			headlist.add(random); // ���Ÿ� ���뿡 �߰�
			head = random; // ���Ÿ� �Ӹ��� �����
			
			tt.score = tt.score + tt.bonusCnt; // ���� ���
			tt.bonusCnt = 101; // �ٽ� �ʱ�ȭ  
			
			random(); // �԰� ���� ���ο� �Լ� ����
			//System.out.println("������� : " + headlist.size());
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
		for (int i = headlist.size(); i > 0 ; i--) {
			if (i == headlist.size()) {
				int x = headlist.get(i - 1).getX();
				int y = headlist.get(i - 1).getY();
				panel[y][x].setFill(hd_color);
				// System.out.println("��常 ��ĥ");
			} else if (i < headlist.size()) {
				int x = headlist.get(i - 1).getX();
				int y = headlist.get(i - 1).getY();
				panel[y][x].setFill(bodylist.get(i - 1));
				// System.out.println("������ ��ĥ");
			}
		}
		
	
		
	}
	
	public void clear(){ // ȭ�� �ʱ�ȭ
		for (int i = 0; i < yCnt; i++) {
			for (int j = 0; j < xCnt; j++) {
				panel[i][j].setFill(bg_color); // ȭ�� ��ĥ
				if (random.getX() == j && random.getY() == i) { // ������ ����
					panel[i][j].setFill(randomcolor); // ȭ���� �������� ������ ���� ��ǥ�� ���� �� �ڸ��� �׷��ش�

				}
			}
		}
	}
	
	public void random(){ // �������� ���� �����
		random = new Point(); // ��ǥ ��ü ����

		// �÷� ����Ʈ ������ ��ŭ�� ���� �߿� ���� ���� ����
		ranC = (int) (Math.random() * colorlist.colorlist.size()); 
		randomcolor = colorlist.colorlist.get(ranC); // ���� ���� ��ġ�� �ִ� ������ �����÷��� ����
		
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
		bodylist.add(colorlist.colorlist.get(ranC)); // ����� ����������� �������Ÿ���Ʈ�� �߰�
	}
	
	public void setHead(){ // ��� �ʱ� ����
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
		System.out.println(headlist.size());
	}
	
	public void setAuto(){
		if (timeline == null) { // timeline�� �������� ����ǰ� �����
			timeline = new Timeline();
			KeyFrame k = new KeyFrame(Duration.millis(time), e -> going());
			timeline.getKeyFrames().add(k);
			timeline.setCycleCount(Timeline.INDEFINITE); // �������� ����
			timeline.play();
		}
	}
}
