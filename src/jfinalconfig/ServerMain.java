package jfinalconfig;

import com.jfinal.core.JFinal;

public class ServerMain {

	public static void main(String[] args) {
		System.out.println("main");
		JFinal.start("WebRoot", 8080, "/");
	}
}
