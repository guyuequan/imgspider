package com.hq.imgspider;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Imglistspider implements Runnable{
	
	private String hostString;
	
	private List<String>  urlList;
	
	public Imglistspider(List<String> urlList,String hostString) {
		// TODO Auto-generated constructor stub
		this.urlList = urlList;
		this.hostString = hostString;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ExecutorService executorService =Executors.newFixedThreadPool(Imgutil.ALL_THEAD_COUNT);
		for (String urlString : urlList) {
			
	//		String[] tmpStrings = urlString.split(".");
			String filename = Imgutil.getMD5(urlString);
			Imgspider imgspider = new Imgspider(urlString, hostString, Imgutil.SAVE_DIR+filename+".jpg");
			executorService.execute(imgspider);
			//imgspider.run();
		}
		
		executorService.shutdown();
		try {
			while(!executorService.awaitTermination(1, TimeUnit.SECONDS)){
				//do nothing
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//single img download over
		System.out.println("whole img list donwload over");
	}
	
	

}
