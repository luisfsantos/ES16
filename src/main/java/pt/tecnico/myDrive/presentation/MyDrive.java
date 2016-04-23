package pt.tecnico.myDrive.presentation;

import java.util.Map;
import java.util.HashMap;

public class MyDrive extends Shell {
	protected Map<String, Long> loggedIn = new HashMap<>(); //TODO must users have more than one active session?

	public static void main(String[] args) throws Exception {
		MyDrive sh = new MyDrive();
	    sh.execute();
	  }

	public MyDrive() { // add commands here
	  super("MyDrive");
	  /* eg:
	  new CreateFile(this); // the CreateFile command class has to exist and extend from MyDriveCommand
	   */
	}

	public void addLogin(String username, Long newToken) {
		loggedIn.put(username, newToken);
	}

	public void getUserToken(String username) {
		loggedIn.get(username);
	}
}