package edu.cmu_ucla.minuet.apiTest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TranscriberDemo {

	public static void main(String[] args) throws Exception {
		Thread audioThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while(true){
						try {
							Process p = Runtime.getRuntime().exec("python /Users/runchangkang/Documents/Minuet/pySpeech/liveTest.py");
							p.waitFor(60, TimeUnit.SECONDS);  // let the process run for 5 seconds
							p.destroy();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		audioThread.start();

	}
}
