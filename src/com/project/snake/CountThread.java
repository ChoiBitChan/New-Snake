package com.project.snake;

import javafx.application.Platform;

public class CountThread extends Thread {
	
	View snakemain;
	//�ʸ� �������� ���� ����
	int bonusCnt;
	int score;
	boolean timeOnOff = false;
	
	public CountThread(View snakemain) {
		//�ʸ� �������� ���� �ʱ�ȭ
		this.snakemain = snakemain;
		bonusCnt = 101;
		timeOnOff = true;
		
	}
	
	
	@Override
	public void run() {
		try {
			while(timeOnOff){
				
				Thread.sleep(200);
				if(bonusCnt > 10){
					bonusCnt--;
					System.out.println(bonusCnt);
					Platform.runLater(()->{
						//snakemain.label.setText(Integer.toString(timeCnt));
						snakemain.timer();
					});
					
				}
				else {
					timeOnOff = false;
				}
				
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	

	

}
