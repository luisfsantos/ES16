package pt.tecnico.myDrive.domain;

public class SuperUser extends User {
    private static SuperUser superUser = new SuperUser();
	
    private SuperUser() {
        super();
    	//super("root", "***", "Super User", "rwxdr-x-", null, null);
    }
    
    public static SuperUser getSuperUser () {
    	return superUser;
    }
    
}

