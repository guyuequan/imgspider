package com.hq.imgspider;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Connector {

	private CloseableHttpClient httpclient ;
	private HttpGet httpGet;
	private long contentLength;
	boolean isMultiDownload = false;
	private String urlString;
	private String hostString=null;
	/**
	 * 配置http请求头信息
	 * @author huqian.hq
	 **/	
	public Map<String, String> getHeader(){	
		Map<String, String> mp = new HashMap<>();
		mp.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		mp.put("Accept-Encoding","gzip, deflate");
		mp.put("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		mp.put("Cache-Control","max-age=0");
		mp.put("Connection","keep-alive");	
		mp.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
		if(hostString!=null)
			mp.put("Host", this.hostString);
		return mp;
	}
	
	public void setHost(String hostString){
		this.hostString = hostString;
	}
	public Connector(String urlString) throws Exception {
		// TODO Auto-generated constructor stub
	    httpclient = HttpClients.createDefault();
	    httpGet = new HttpGet(urlString);
	    this.urlString = urlString;
	}
	/**
	 * 检测长度，是否能多线程下载
	 * @throws Exception
	 */

	
	public void detect() throws Exception {

		  HttpHead httpHead = new HttpHead(urlString);  
			
			Map<String, String> headers = getHeader();
			for (String key : headers.keySet()) {
			    String value = headers.get(key);
			    httpHead.addHeader(key, value);		
			}			
		    HttpResponse response = httpclient.execute(httpHead);  
		    //获取HTTP状态码  
		    int statusCode = response.getStatusLine().getStatusCode();  
		  
		    if(statusCode != 200) throw new Exception(urlString+" 资源不存在!"+statusCode);  
		    //Content-Length  
		    
		    
		    Header[] headers1 = response.getHeaders("Content-Length");  
		    if(headers1.length > 0)  
		        contentLength = Long.valueOf(headers1[0].getValue());  
		  
		    httpHead.abort();  
		      
		    httpHead = new HttpHead(urlString);  
		    httpHead.addHeader("Range", "bytes=0-"+(contentLength-1));  
		    response = httpclient.execute(httpHead);  
		    if(response.getStatusLine().getStatusCode() == 206){  
		        isMultiDownload = true;  
		    }  
		    httpHead.abort(); 

	}
	
	
	/**
	 * get the content length of this picture
	 */
	public long getContentLength(){
		return this.contentLength;
	}
	
	/**
	 * make sure it can be multidownloaded
	 * @return
	 */
	public boolean isMulti(){
		return this.isMultiDownload;
	}
	
	
	/**
	 * 
	 * @param urlString
	 * @param start
	 * @param end
	 * @return  the result of the range
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public BufferedInputStream getResultByRange(Long start,Long end) throws ClientProtocolException, IOException{
		
		Map<String, String> headers = getHeader();
		for (String key : headers.keySet()) {
		    String value = headers.get(key);
		    httpGet.addHeader(key, value);		
		}			
		httpGet.addHeader("Range", "bytes="+start+"-"+end);
		HttpResponse response = httpclient.execute(httpGet);
		return new  BufferedInputStream(response.getEntity().getContent());
	}
	
	public BufferedInputStream getResultWithoutRange() throws IllegalStateException, IOException {
		Map<String, String> headers = getHeader();
		for (String key : headers.keySet()) {
		    String value = headers.get(key);
		    httpGet.addHeader(key, value);		
		}
		
		HttpResponse response = httpclient.execute(httpGet);
		return new  BufferedInputStream(response.getEntity().getContent());
	}
	

	
	
}
