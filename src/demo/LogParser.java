package demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LogParser {

	public static void main(String[] args) {
		try {
			(new LogParser()).getUserQrcode();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private HashMap<String, List<String>> ipLogsMap = new HashMap<String, List<String>>();

	private List<UserQr> userQrList = new ArrayList<UserQr>();

	public List<UserQr> getUserQrcode() throws IOException {
		File logDir = new File("log");
		File[] logArray = logDir.listFiles();

		for (File logFile : logArray) {
			System.out.println("parse log:" + logFile.getName());

			dellLogFile(logFile);

			System.out.println("parse log:" + logFile.getName() + " finish");
		}
		
		return userQrList;
	}

	private void dellLogFile(File logFile) throws IOException {

		ipLogsMap.clear();

		BufferedReader logReader = new BufferedReader(new FileReader(logFile));
		String line = null;

		while ((line = logReader.readLine()) != null) {
			String ipStr = getIp(line);
			
			if (ipLogsMap.containsKey(ipStr)) {
				if (line.contains("/userinfo/save?")) {
					ipLogsMap.get(ipStr).add(line);
				}
			}
			
			if (line.contains("qrcode") && line.contains("clientSession=&")) {
				List<String> logList = null;
				if (ipLogsMap.containsKey(ipStr)) {
					logList = ipLogsMap.get(ipStr);
					System.out.println(ipStr + "!!!!!!!!!!!!!!!!!! 不应该出现 ！！！！！！！！！！！！！！！");
					continue;
				} else {
					logList = new ArrayList<String>();
				}
				logList.add(line);
				ipLogsMap.put(ipStr, logList);
			}
		}
		
		UserQr userQr = null;
		for(String ip : ipLogsMap.keySet()) {
			List<String> logList = ipLogsMap.get(ip);
			if(logList.size() <= 1) {
				continue;
			}
			for(String log : logList) {
				
				if(log.contains("qrcode") && log.contains("clientSession=&")){
					int start = log.indexOf("scene=") + 6;
					int end = log.indexOf(" HTTP");

					String qr = log.substring(start, end);

					userQr = new UserQr();
					userQr.qrcode = qr;
				}
				
				if (log.contains("/userinfo/save?")) {
					String nickName = getNickName(log);
					if (userQr != null) {
						userQr.nickName = nickName;
					}
					userQrList.add(userQr);
					userQr = null;
					
					break;
				}
			}
		}

	}

	private String getNickName(String nameLine) throws UnsupportedEncodingException {
		int start = nameLine.indexOf("nickName") + 8;
		int end = nameLine.indexOf("gender");

		String nickName = nameLine.substring(start, end);

		nickName = URLDecoder.decode(nickName, "utf-8");
		nickName = URLDecoder.decode(nickName, "utf-8");

		start = nickName.indexOf(":\\\"") + 3;
		end = nickName.indexOf("\\\",");

		nickName = nickName.substring(start, end);

		return nickName;
	}

	private String getIp(String line) {
		int end = line.indexOf(" - -");
		return line.substring(0, end);
	}

}
