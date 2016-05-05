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
	private Map<String, List<Long>> loggedIn = new HashMap<String, List<Long>>();

	public static void main(String[] args) throws Exception {
		if (args.length == 1) {
			xmlScan(new java.io.File(args[0]));
		}

		MyDrive sh = new MyDrive();
		sh.setupGuestUser();
	    sh.execute();
	  }

	
	public MyDrive() { // add commands here
		super("MyDrive");
		new LoginCommand(this);
		new ListCommand(this);
		new WriteCommand(this);
	}
	
	private void setupGuestUser() {
        LoginService guestLogin = new LoginService("nobody", ""); 
        guestLogin.execute();
        activeToken = guestLogin.result();
        this.addLogin("nobody", activeToken);
	}

	public void addLogin(String username, Long newToken) {
		if (loggedIn.containsKey(username)) {
			loggedIn.get(username).add(newToken);
		} else {
			ArrayList<Long> sessions = new ArrayList<Long>();
			sessions.add(newToken);
			loggedIn.put(username, sessions);
		}
		
	}

	public List<Long> getUserToken(String username) {
		return loggedIn.get(username);
	}

	public Long getActiveToken() {
		return activeToken;
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