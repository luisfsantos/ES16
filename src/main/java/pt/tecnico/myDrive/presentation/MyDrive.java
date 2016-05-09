package pt.tecnico.myDrive.presentation;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.service.LoginService;

import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyDrive extends Shell {
	private Long activeToken;
	private String activeUser;
	private Map<String, Long> loggedIn = new HashMap<String, Long>();

	public static void main(String[] args) throws Exception {
		if (args.length == 1) {
			xmlScan(new java.io.File(args[0])); //service will have to create an empty database...
		}
		MyDrive sh = new MyDrive();
		sh.setupGuestUser();
		sh.execute();
		

	  }

	
	public MyDrive() { // add commands here
		super("MyDrive");
		new LoginCommand(this);
		new ListCommand(this);
		new ChangeWorkingDirectoryCommand(this);
		new WriteCommand(this);
		new KeyCommand(this);
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

	@Atomic
	public static void xmlScan(java.io.File file) {
		log.trace("xmlScan: " + FenixFramework.getDomainRoot());
		Manager manager = Manager.getInstance();
		SAXBuilder builder = new SAXBuilder();
		try {
			Document document = (Document)builder.build(file);
			manager.xmlImport(document.getRootElement());
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
	}
}