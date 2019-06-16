package demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Text {
	public static void main(String[] args) {
		String file = "/Users/yujinyang11/Desktop/1.txt";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(file)));

			String line = reader.readLine();
			
			HashSet<String> set = new HashSet<String>();
			while (line != null) {
				if (line.startsWith("HTTP")) {
				} else {
					System.out.println("==" + line);
					if(!set.contains(line)) {
						set.add(line);
					}
				}
				line = reader.readLine();
			}
			
			System.out.println("Set size:" + set.size());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
