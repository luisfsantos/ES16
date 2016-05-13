package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.LoginService;

import java.util.Map;
import java.util.HashMap;


public class MyDrive extends Shell {
	private Long activeToken;
	private String activeUser;
	private Map<String, Long> loggedIn = new HashMap<String, Long>();

	public static void main(String[] args) throws Exception {
		MyDrive sh = new MyDrive();
		if (args.length == 1)
			new ImportCommand(sh).execute(args);
		sh.setupGuestUser();
		sh.execute();

	  }

	
	public MyDrive() { 
		super("MyDrive");
		new LoginCommand(this);
		new ListCommand(this);
		new ChangeWorkingDirectoryCommand(this);
		new WriteCommand(this);
		new KeyCommand(this);
		new ExecuteCommand(this);
		new EnvironmentCommand(this);
		new ImportCommand(this);
		new QuitCommand(this);

	}
	
	private void setupGuestUser() {
        LoginService guestLogin = new LoginService("nobody", "");
        guestLogin.execute();
        activeToken = guestLogin.result();
        this.addLogin("nobody", activeToken);
	}


	public Long logoutGuestUser () {
		Long guestToken = loggedIn.get("nobody");
		if (guestToken != null) {
			loggedIn.remove("nobody");
		}
		return guestToken;
	}

	public void addLogin(String username, Long newToken) {
		loggedIn.put(username, newToken);
		activeToken = newToken;
		activeUser = username;
	}

	public boolean swapUser(String username) {
		if (loggedIn.containsKey(username)) {
			activeUser = username;
			activeToken = loggedIn.get(username);
			return true;
		} else {
			return false;
		}
	}

	public Long getActiveToken() {
		return activeToken;
	}

	public String getActiveUser() {
		return activeUser;
	}
}