package com.project.snake;

import java.util.ArrayList;

import javafx.scene.paint.Color;

public class Colorlist {
	
	ArrayList<Color> colorlist;
	
	public Colorlist() {
	}

	public void createColor(){
		colorlist = new ArrayList<>();
		colorlist.add(Color.BLUE);
		colorlist.add(Color.RED);
		colorlist.add(Color.ORANGE);
		colorlist.add(Color.GREEN);
		colorlist.add(Color.GOLD);
		colorlist.add(Color.SILVER);
	}
}
