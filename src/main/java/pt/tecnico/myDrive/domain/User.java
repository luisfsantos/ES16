package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import org.jdom2.DataConversionException;

public class User extends User_Base {

    public User() {
        super();
    }

	/*
	public User(String username, String password, String name, String umask, Manager manager, Directory home ) {
		super();
		this.setUsername(username);
		this.setPassword(password);
		this.setName(name);
		this.setUmask(umask);
		this.setManager(manager);
		this.addFile(home);
	}
	*/

	public Element xmlExport() {
        Element element = new Element("user");
        element.setAttribute("username", getUsername());
        
        Element passwordElement = new Element("password");
        passwordElement.setText(getPassword()); // metodo getPassword() (ou semelhante) tem de ser acessivel ao User
        element.addContent(passwordElement);

        Element nameElement = new Element("name");
        nameElement.setText(getName());
        element.addContent(nameElement);

        Element homeElement = new Element("home");
        homeElement.setText(getHome().getAbsolutePath()); // absolute path necessario para conhecer localizacao dum directorio
        element.addContent(homeElement);

        Element maskElement = new Element("mask");
        maskElement.setText(getMask());
        element.addContent(maskElement);

        return element;
    }
	
}
