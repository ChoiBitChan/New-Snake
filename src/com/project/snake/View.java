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
	private Label Slabel; // 점수 레이블
	@FXML
	private Label Blabel; // 보너스 레이블
	
	
	int xCnt = 20; // 칸 개수
	int yCnt = 20; // 칸 개수

	int x = 0; // 좌표
	int y = 0; // 좌표

	int ranX = 0; // 랜덤 좌표
	int ranY = 0; // 랜덤 좌표
	int ranC = 0; // random color

	int rect_size = 20; // 사이즈


	Rectangle[][] panel;
	Point random; // 랜덤으로 나오는 과일 좌표
	Point head; // 헤드 좌표
	TimeThread tt; // 쓰레드
	Paint randomcolor; // 랜덤으로 나오는 열매 색깔
	
	Color bg_color = Color.BLACK; // 배경 색
	Color bord_color = Color.WHITE; // 테두리 색
	Color hd_color = Color.YELLOW; // 헤드 색
	
	LinkedList<Point> headlist; // 헤드 리스트
	Colorlist colorlist; // 컬러 리스트
	ArrayList<Color> bodylist; // 랜덤으로 나오는 열매 리스트
	
	Timeline timeline;
	int time;
	
	// 열거
	public enum Direction {
		Up, Down, Right, Left // 방향을 정의
	}
	Direction direction;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) { // 초기 설정

		/*
		Rectangle rect = new Rectangle(20,20);
		grid.add(rect, 0, 0);
		
		//뷰 변경가능
		rect = new Rectangle(20,20);
		rect.setFill(Color.RED);
		vbox.getChildren().add(rect);
		*/
		
		tt = new TimeThread(this); // 쓰레드 객체 생성
		tt.start(); // 쓰레드 시작
		
		Blabel.setStyle("-fx-font-size:25"); // 보너스 레이블 폰트 사이즈 지정
		Slabel.setStyle("-fx-font-size:25"); // 스코어 레이블 폰트 사이즈 지정
		
		// 패널생성
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
		
		colorlist = new Colorlist(); // 컬러 리스트 객체 생성
		
		hbox.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.UP) { // 방향키 위쪽 눌렀을 때
				if(direction == Direction.Down){ // 현재 진행 상태가 아래쪽이면
					return; // 방향키 위쪽을 누르지 않고 빠져나간다
				} else { // 그 외에는 방향키를 누를 수 있다
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
			if (e.getCode() == KeyCode.ENTER) { // 엔터를 누르면
				startGame(); // 게임이 실행된다
				tt.bonusCnt = 101; // 보너스 카운트 초기 값 설정
				if (timeline.getStatus() == Status.STOPPED) { // 현재 게임 상태가 멈춰 있다면
					timeline.play(); // 타임라인 실행
				}
			}
			if (e.getCode() == KeyCode.P) { // 일시정지 버튼
				if (timeline.getStatus() == Status.RUNNING) { // 현재 게임 상태가 진행 중이라면
					timeline.stop(); // 일시 정지
				} else if (timeline.getStatus() == Status.STOPPED) { // 현재 게임 상태가 멈춰 있다면
					timeline.play(); // 타인라인 다시 실행
				}
			}
		});
		
		
	}
	
	public void startGame(){ // 게임 시작
		for (int i = 0; i < yCnt; i++) { // 엔터를 눌러야 게임이 실행 되므로 판넬만 깔아준다
			for (int j = 0; j < xCnt; j++) {
				panel[i][j].setFill(bg_color); // 화면 색칠
			}
		}
		
		setHead(); // 헤드 세팅
		setAuto(); // 자동 움직임 세팅
		random(); // 랜덤 세팅
	}
	
	public void going(){ // 시계방향 순서
		
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
	
	public void move(int off_y, int off_x){ // 함수를 생성한다

		// 미리 움직여 보고 벽이나 자기자신이 있다면 멈추고, 음식이 있다면 길어진다.
		int temp_y = head.getY() + off_y;
		int temp_x = head.getX() + off_x;
		if (temp_y < 0 || // 위쪽에 부딪히거나 
			temp_x < 0 || // 왼쪽
			temp_y > yCnt - 1 || // 아래쪽
			temp_x > xCnt - 1) { // 오른쪽
				System.out.println("게임오버");
				timeline.stop(); // 게임 정지
				tt.bonusCnt = 10; // 보너스 카운트 초기화
			return;
		}
		for(int i = 0; i<headlist.size(); i++){ // 몸통의 길이만큼 반복
			int x = headlist.get(i).getX(); // 순서대로 좌표에 넣어준다
			int y = headlist.get(i).getY();
			if((temp_y == y) && (temp_x == x)){ // 미리 가본 좌표가 내몸과 일치하게 되면 죽는다
				System.out.println("게임오버");
				timeline.stop();
				tt.bonusCnt = 10;
			}
		}
		if (temp_y == random.getY() && temp_x == random.getX()) { // 미리 가본 좌표가 열매 일 때
			// 먹는 로직 -> 먹음과 동시에 헤드가 길어짐
			headlist.add(random); // 열매를 몸통에 추가
			head = random; // 열매를 머리로 만들다
			
			tt.score = tt.score + tt.bonusCnt; // 점수 계산
			tt.bonusCnt = 101; // 다시 초기화  
			
			random(); // 먹고 나서 새로운 함수 생성
			//System.out.println("몸통길이 : " + headlist.size());
		} else { // 기본적으로 움직일 때
			// 움직이는 것 처럼 보이게 하기 위해선
			// 새로운 헤드를 만들어 붙이고
			Point pt = new Point(); // pt 객체 생성
			pt.setY(head.getY() + off_y);
			pt.setX(head.getX() + off_x);
			headlist.add(pt); // 리스트에 pt 추가
			head = pt;
			// 꼬리를 뗀다
			headlist.poll(); // 리스트에서 삭제
		}

		clear(); // 화면 초기화
		
		// 몸통 그리기
		for (int i = headlist.size(); i > 0 ; i--) {
			if (i == headlist.size()) {
				int x = headlist.get(i - 1).getX();
				int y = headlist.get(i - 1).getY();
				panel[y][x].setFill(hd_color);
				// System.out.println("헤드만 색칠");
			} else if (i < headlist.size()) {
				int x = headlist.get(i - 1).getX();
				int y = headlist.get(i - 1).getY();
				panel[y][x].setFill(bodylist.get(i - 1));
				// System.out.println("먹은거 색칠");
			}
		}
		
	
		
	}
	
	public void clear(){ // 화면 초기화
		for (int i = 0; i < yCnt; i++) {
			for (int j = 0; j < xCnt; j++) {
				panel[i][j].setFill(bg_color); // 화면 색칠
				if (random.getX() == j && random.getY() == i) { // 랜덤값 유지
					panel[i][j].setFill(randomcolor); // 화면이 지워져도 생선된 열매 좌표를 구해 그 자리에 그려준다

				}
			}
		}
	}
	
	public void random(){ // 랜덤으로 열매 만들기
		random = new Point(); // 좌표 객체 생성

		// 컬러 리스트 사이즈 만큼의 개수 중에 랜덤 숫자 생성
		ranC = (int) (Math.random() * colorlist.colorlist.size()); 
		randomcolor = colorlist.colorlist.get(ranC); // 랜덤 숫자 위치에 있는 색깔을 랜덤컬러로 지정
		
		ranX = random.getX() + (int) (Math.random() * 20); // 좌표 랜덤
		ranY = random.getY() + (int) (Math.random() * 20);
		panel[ranY][ranX].setFill(randomcolor); // 임의의 좌표에 랜덤컬러 그리기
		random.setX(ranX); // 좌표 세팅
		random.setY(ranY);

		// 랜덤으로 만든 열매가 내 몸통 위에 만들어 지지 않게 하기
		for (int i = 0; i < headlist.size(); i++) { // 내 몸통의 좌표가
			int x = headlist.get(i).getX();
			int y = headlist.get(i).getY();
			if (x == ranX && y == ranY) { // 랜덤 좌표와 일치하면
				random(); // 다시 실행
				return; // 리턴이 중요!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			}
		}
		bodylist.add(colorlist.colorlist.get(ranC)); // 제대로 만들어졌으면 랜덤열매리스트에 추가
	}
	
	public void setHead(){ // 헤드 초기 지정
		head = new Point();
		
		// 위치 지정
		x = head.getX() + (xCnt / 2);
		y = head.getY() + (yCnt / 2);
		panel[y][x].setFill(hd_color);
		head.setX(x);
		head.setY(y);

		// 초기화
		headlist = new LinkedList<>();
		bodylist = new ArrayList<>();
		headlist.add(head);
		direction = Direction.Up;
		time = 100;
		System.out.println(headlist.size());
	}
	
	public void setAuto(){
		if (timeline == null) { // timeline이 없을때만 실행되게 만든다
			timeline = new Timeline();
			KeyFrame k = new KeyFrame(Duration.millis(time), e -> going());
			timeline.getKeyFrames().add(k);
			timeline.setCycleCount(Timeline.INDEFINITE); // 무한으로 돌기
			timeline.play();
		}
	}
}
