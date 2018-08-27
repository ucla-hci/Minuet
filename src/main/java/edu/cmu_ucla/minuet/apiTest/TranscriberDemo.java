package edu.cmu_ucla.minuet.apiTest;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class TranscriberDemo {

	public static void main(String[] args) throws Exception {
//		calculation(new Vector3D(2,1,0),new Vector3D(2,-4,0));
//		Thread audioThread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					while(true){
//						try {
//							Process p = Runtime.getRuntime().exec("python /Users/runchangkang/Documents/Minuet/pySpeech/liveTest.py");
//							p.waitFor(60, TimeUnit.SECONDS);  // let the process run for 5 seconds
//							p.destroy();
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//		audioThread.start();
		List<Integer> numbers = new ArrayList<>();
		for(int i = 0; i<80;i++){numbers.add(i);}
		numbers = shuffleArray(numbers);
		for (int i = 0; i<10;i++){
			for(int j = 0; j<8;j++){
				System.out.print(numbers.get(i*8+j)%20+" ");
				}
				System.out.println(" ");
		}

	}
	public static List<Integer> shuffleArray(List<Integer> a) {
		List<Integer> b = new ArrayList<Integer>();
		while (a.size() != 0) {
			int arrayIndex = (int) (Math.random() * (a.size()));
			b.add(a.get(arrayIndex));
			a.remove(a.get(arrayIndex));
		}
		return b;
	}
	public static void calculation(Vector3D A,Vector3D B){
		System.out.println("degree is :"+Math.toDegrees(Math.atan2(A.getY(),A.getX())));
		double adegree = Math.toDegrees(Math.atan2(A.getY(),A.getX()));
		double bdegree = Math.toDegrees(Math.atan2(B.getY(),B.getX()));
		String result="";
		if(-180<=adegree&&adegree<=0 && -180<=bdegree&&bdegree<=0 ){
			 result = (adegree<=bdegree)?"A":"B";

		}else if (0<=adegree&&adegree<=180 && 0<=bdegree&&bdegree<=180 ){
			result = (adegree>=bdegree)?"A":"B";
		}else if ((adegree*bdegree)<0){
			if(Math.abs(adegree)<=90){
				result = (adegree>=0)?"A":"B";

			}
			else {
				result = (adegree<=0)?"A":"B";
			}
		}
		System.out.println("left is "+result);


	}
}
