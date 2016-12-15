package com.project.snake;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javafx.animation.Animation.Status;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class View implements Initializable {

	@FXML
	private GridPane grid;
	
	@FXML
	private VBox vbox;
	
	@FXML
	private Label Slabel;
	
	@FXML
	private Label Blabel;
	
	int xCnt = 20;
	int yCnt = 20;

	int x = 0;
	int y = 0;

	int ranX = 0;
	int ranY = 0;
	int ranC = 0;
	int cnt = 0;

	int rect_size = 20;

	int eat = 0;

	int height = 420;
	int width = 600;
	
	Rectangle[][] panel;
	Point random;
	Point head;
	TimeThread tt;
	Paint randomcolor;
	
	Color bg_color = Color.BLACK;
	Color bord_color = Color.WHITE;
	Color hd_color = Color.YELLOW;
	
	LinkedList<Point> headlist;
	ArrayList<Color> randomlist;
	
	Timeline timeline;
	int time;
	
	public enum Direction {
		Up, Down, Right, Left
	}
	Direction direction;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		/*
		Rectangle rect = new Rectangle(20,20);
		grid.add(rect, 0, 0);
		
		//뷰 변경가능
		rect = new Rectangle(20,20);
		rect.setFill(Color.RED);
		vbox.getChildren().add(rect);
		*/
		
		tt = new TimeThread(this);
		tt.start();
		
		Blabel.setStyle("-fx-font-size:25");
		Slabel.setStyle("-fx-font-size:25");
		
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
		
		grid.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.UP) {
				// move(-1, 0);
				if(direction == Direction.Down){
					return;
				} else {
					direction = Direction.Up;
				}
			}
			if (e.getCode() == KeyCode.DOWN) {
				// move(1, 0);
				if(direction == Direction.Up){
					return;
				} else {
					direction = Direction.Down;
				}
			}
			if (e.getCode() == KeyCode.RIGHT) {
				// move(0, 1);
				if(direction == Direction.Left){
					return;
				} else {
					direction = Direction.Right;
				}
			}
			if (e.getCode() == KeyCode.LEFT) {
				// move(0, -1);
				if(direction == Direction.Right){
					return;
				} else {
					direction = Direction.Left;
				}
			}
			if (e.getCode() == KeyCode.SPACE) {
			}
			if (e.getCode() == KeyCode.ENTER) {
				startgame();
				tt.bonusCnt = 101;
				if (timeline.getStatus() == Status.STOPPED) {
					timeline.play();
				}
			}
			if (e.getCode() == KeyCode.P) {
				if (timeline.getStatus() == Status.RUNNING) {
					timeline.stop();
				} else if (timeline.getStatus() == Status.STOPPED) {
					timeline.play();
				}
			}
		});
		
		
	}
	
	public void startgame(){
		
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
	
	public void move(int off_y, int off_x){
		
	}
	
	public void clear(){
		
	}
	
	public void random(){
		
	}

}
