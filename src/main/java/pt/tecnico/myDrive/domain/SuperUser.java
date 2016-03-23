package pt.tecnico.myDrive.domain;

public class SuperUser extends SuperUser_Base {
    
    public SuperUser(Manager manager) {
    	this.setManager(manager);
		this.setUsername("root");
		this.setPassword("***");
		this.setName("Super User");
		this.setUmask("rwxdr-x-");
    }

	public boolean hasPermission(File file, Mask mask){
		return true;
	}
}
