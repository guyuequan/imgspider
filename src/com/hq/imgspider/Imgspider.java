package com.hq.imgspider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * single picture download spider
 * @author huqian.hq
 * @input: url host savePath
 *
 */
public class Imgspider implements Runnable{
	
	private String urlString;
	private String savePathString;
	private String hostString;
	
	public Imgspider(String urlString,String hostString,String savePathString) {
		// TODO Auto-generated constructor stub
		this.urlString = urlString;
		this.hostString = hostString;
		this.savePathString = savePathString;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		ExecutorService executorService = null;
		try {
			Connector connector = new Connector(urlString);
			connector.setHost(hostString);
			connector.detect();//检测
			//可以多线程下载
			if(connector.isMultiDownload){
				executorService= Executors.newFixedThreadPool(Imgutil.SINGLE_THREAD_COUNT);
				long allLength = connector.getContentLength() ;
				long eachLength = allLength / Imgutil.SINGLE_THREAD_COUNT;
				for (int i = 0; i < Imgutil.SINGLE_THREAD_COUNT; i++) {
					long start = i*eachLength;
					long end = (i+1)*eachLength-1;
					Imgdownloader imgdownloader = new Imgdownloader(urlString, hostString,start, end, savePathString);
					executorService.execute(imgdownloader);
				}
			}else{
				//单线程下载:
				executorService= Executors.newFixedThreadPool(1);
				Imgdownloader imgdownloader = new Imgdownloader(urlString,hostString, savePathString);
				executorService.execute(imgdownloader);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(urlString);
			//e.printStackTrace();
		}		
		executorService.shutdown();
		try {
			while(!executorService.awaitTermination(1, TimeUnit.SECONDS)){
				//do nothing
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.err.println(urlString);
			e.printStackTrace();
		}
		
		System.out.println(urlString+" download over!");
		
		//single pic download over
	}

}
