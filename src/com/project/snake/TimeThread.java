package com.project.snake;

import javafx.application.Platform;

public class TimeThread extends Thread {
	
	View snakemain;
	//�ʸ� �������� ���� ����
	int bonusCnt;
	int score;
	//boolean isQuit = false;
	boolean timeOnOff = false;


	
	
	public TimeThread(View snakemain) {
		//�ʸ� �������� ���� �ʱ�ȭ
		this.snakemain = snakemain;
		bonusCnt = 101;
		//isQuit = true;
		timeOnOff = true;
		
	}
	
	
	@Override
	public void run() {
		//while(isQuit){
		//�ʸ�����.
		//}
		try {
			while(timeOnOff){
				
				Thread.sleep(200);
				if(bonusCnt > 10){
					bonusCnt--;
					System.out.println(bonusCnt);
					Platform.runLater(()->{
						//snakemain.label.setText(Integer.toString(timeCnt));
						//snakemain.timer();
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
