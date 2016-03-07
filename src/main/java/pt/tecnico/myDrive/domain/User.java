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
	@Override
	public void setUmask(String permission){
		if(permission.length() != 8) return ;
		Mask mask[] = Mask.values();
		//String mask = "rwxdrwxd";
		for(int i = 0; i < 8; i++)
			//if(permission.charAt(i) != mask.charAt(i) && permission.charAt(i) != '-')
			if(permission.charAt(i) != mask[4 % i].getValue() && permission.charAt(i) != '-')
				return ;
		super.setUmask(permission);
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
