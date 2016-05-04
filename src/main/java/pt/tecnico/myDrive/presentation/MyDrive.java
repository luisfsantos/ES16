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
import java.util.HashMap;

public class MyDrive extends Shell {
	protected Long activeToken;
	protected Map<String, Long> loggedIn = new HashMap<>();

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
	  /* eg:
	  new CreateFile(this); // the CreateFile command class has to exist and extend from MyDriveCommand
	   */
	}
	
	public void setupGuestUser() {
        LoginService guestLogin = new LoginService("root", "***"); // ("nobody", "") cause nullPointerException because nobody has password == NULL
        guestLogin.execute();
        activeToken = guestLogin.result();
        this.addLogin("nobody", activeToken);
	}

	public void addLogin(String username, Long newToken) {
		loggedIn.put(username, newToken);
	}

	public void getUserToken(String username) {
		loggedIn.get(username);
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