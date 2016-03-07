package pt.tecnico.myDrive.domain;

public class User extends User_Base {
		
	public User(String username, String password, String name, String umask, Manager manager, Directory home ) {
		super();
		this.setUsername(username);
		this.setPassword(password);
		this.setName(name);
		this.setUmask(umask);
		this.setManager(manager);
		this.addFile(home);
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
	
}
