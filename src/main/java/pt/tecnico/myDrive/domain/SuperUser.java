package pt.tecnico.myDrive.domain;

public class SuperUser extends SuperUser_Base {
    
    public SuperUser(Manager manager) {
		this.setUsername("root");
    	this.setManager(manager);
		this.setPassword("***");
		this.setName("Super User");
		this.setUmask("rwxdr-x-");
    }

	public boolean hasPermission(File file, Mask mask){
		return true;
	}
}
