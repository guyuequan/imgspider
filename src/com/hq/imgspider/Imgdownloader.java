package com.hq.imgspider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

public class Imgdownloader implements Runnable {

	
	
	private String urlString;
	private String savePathString;
	private long start = 0;
	private long end = 0;
	private String hostString = null;
	
	public Imgdownloader(String urlString,String hostString,long start,long end,String savePathString){
		this.urlString = urlString;
		this.savePathString = savePathString;
		this.start = start;
		this.hostString = hostString;
		this.end = end;
	}
	
	public Imgdownloader(String urlString,String hostString,String savePathString){
		this.urlString = urlString;
		this.hostString = hostString;
		this.savePathString = savePathString;
	}
	
	public void setHost(String hostString) {
		this.hostString = hostString;
	}
	
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
			
		
		try {
			Connector connector = new Connector(urlString);
			connector.setHost(hostString);
			RandomAccessFile myFile = new RandomAccessFile(this.savePathString, "rw");
			byte[]  tmpBuffer = new byte[Imgutil.BUFFER_SIZE];	
			myFile.seek(start);
			if(this.start == this.end && this.start == 0){
				//单线程
				BufferedInputStream resultStream = connector.getResultWithoutRange();
				FileOutputStream fos = new FileOutputStream(new File(this.savePathString));  
	            byte[] buf = new byte[Imgutil.BUFFER_SIZE];  
	            int len = 0;  
	            while ((len = resultStream.read(buf)) != -1) {  
	                fos.write(buf, 0, len);  
	            }  
	            fos.flush();  
	            fos.close();  
			}else {
				//多线程
				BufferedInputStream resultStream = connector.getResultByRange(start, end);
				int len = 0;	
				while(len<(this.end-this.start+1)){
					int tmpLen = resultStream.read(tmpBuffer,0,Imgutil.BUFFER_SIZE);
					if(tmpLen == -1)
						break; //error
					myFile.write(tmpBuffer,0,tmpLen);
					len += tmpLen;
				}
			}
			myFile.close();
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(Imgutil.SMALL_SLEEP);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	

}
