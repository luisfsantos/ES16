package pt.tecnico.myDrive;

import java.io.File;
import java.io.PrintStream;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.domain.Manager;

public class Main {

	public static void main(String[] args) {
		System.out.println("Welcome to MyDrive!");
		
		try {
			if (args.length == 0) {
				Manager m = Manager.getInstance();
			
			}
			else {
				// import
			}
			
		} finally { FenixFramework.shutdown(); }
		
	}

}