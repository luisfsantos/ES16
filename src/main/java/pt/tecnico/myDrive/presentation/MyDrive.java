package pt.tecnico.myDrive.presentation;

public class MyDrive extends Shell {

	public static void main(String[] args) throws Exception {
		MyDrive sh = new MyDrive();
	    sh.execute();
	  }

	  public MyDrive() { // add commands here
	    super("MyDrive");
	  }

}