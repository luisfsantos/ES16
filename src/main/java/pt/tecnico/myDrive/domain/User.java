package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import org.jdom2.DataConversionException;

public class User extends User_Base {

    
    public User(String username, String password, String name, String umask) {
    	super();
        this.setUsername(username);
		this.setPassword(password);
		this.setName(name);
		this.setUmask(umask);
    }
    
    public User(String username) {
        this(username, username, username, "rwxd----");
    }
    
    public User() {
    	this("root", "***", "Super User", "rwxdr-x-");
    }


	public void xmlImport(Element userNode){
			//manager nao preciso, certo??
    try {
            setUsername(new String(userNode.getAttribute("username").getValue().getBytes("UTF-8")));
	} catch (UnsupportedEncodingException e){}//DO SOMETHING

	try {
            setPassword(new String(userNode.getChild("password").getValue().getBytes("UTF-8")));
	} catch (UnsupportedEncodingException e){}

	try {
            setName(new String(userNode.getChild("name").getValue().getBytes("UTF-8")));
	} catch (UnsupportedEncodingException e){}

	try {
            addFile(new Directory(userNode.getChild("home").getValue().getBytes("UTF-8")));  //ASSUMO QUE DIRECTORY RECEBE STRING
	} catch (UnsupportedEncodingException e){}

	try {
            setUmask(new String(userNode.getChild("mask").getValue().getBytes("UTF-8")));
	} catch (UnsupportedEncodingException e){}
	
	}

	public boolean hasPermission(File file, Mask mask){
		if(this.equals(file.getUser())) return ownerHasPermission(file, mask);
		else { return allHasPermission(file, mask);}
	}

	public boolean ownerHasPermission(File file, Mask mask){
		switch(mask){
			case READ:
				return mask.getValue() == file.getPermissions().charAt(0);
			case WRITE:
				return mask.getValue() == file.getPermissions().charAt(1);
			case EXEC:
				return mask.getValue() == file.getPermissions().charAt(2);
			case DELETE:
				return mask.getValue() == file.getPermissions().charAt(3);
			default:
				return false;
		}
	}

	public boolean allHasPermission(File file, Mask mask){
		switch(mask){
			case READ:
				return mask.getValue() == file.getPermissions().charAt(4);
			case WRITE:
				return mask.getValue() == file.getPermissions().charAt(5);
			case EXEC:
				return mask.getValue() == file.getPermissions().charAt(6);
			case DELETE:
				return mask.getValue() == file.getPermissions().charAt(7);
			default:
				return false;
		}
	}

	public boolean equals(User user){
		return this.getUsername() == user.getUsername();
	}

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
