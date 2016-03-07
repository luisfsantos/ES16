package pt.tecnico.myDrive;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.File;

public class Main {

	public static void main(String[] args) {
		System.out.println("Welcome to MyDrive!");
		
		try {
			if (args.length == 0) {
				setup();
			}
			else {
				// import
			}
			
		} finally { FenixFramework.shutdown(); }
		
	}

    @Atomic
    public static void setup() {
        Manager m = Manager.getInstance();
        File file = new File();
        User user = new User();

        file.setManager(m);
        file.setUser(user);
        user.setManager(m);
    }

}