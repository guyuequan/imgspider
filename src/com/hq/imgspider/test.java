package com.hq.imgspider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class test {
	
	
	//image download for dmm
	
	//image.txt  是图片url的集合，每一行是一个图片地址
	public static void dmm() throws IOException{
		
		FileReader fileReader = new FileReader(new File("d:/dmm/image.txt"));
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<String> inputList = new ArrayList<>();
		String line=bufferedReader.readLine();
		while(line!=null){
			inputList.add(line);
			line = bufferedReader.readLine();
		}	
		
		Imglistspider imglistspider = new Imglistspider(inputList.subList(2001, 2568), "dmm.hk");
		Imgutil.setSavedir("d:/dmm/images/");
		imglistspider.run();
		
	}
	public static void main(String[] args) throws IOException {
		dmm();
	}
}
