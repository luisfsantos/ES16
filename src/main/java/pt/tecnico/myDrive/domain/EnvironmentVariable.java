package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.exception.InvalidEnvironmentVarNameException;
import pt.tecnico.myDrive.exception.InvalidEnvironmentVarValueException;

public class EnvironmentVariable extends EnvironmentVariable_Base {
    
    public EnvironmentVariable(Login login, String name,String value) {
    	this.setLogin(login);
    	this.setName(name);
    	this.setValue(value);
    }

	public void setName(String name) {
		if (name.equals("")) {
			throw new InvalidEnvironmentVarNameException("empty String");
		}
		super.setName(name);
	}

	public void setValue(String value) {
		if (value == null) {
			throw new InvalidEnvironmentVarValueException("null");
		}
		super.setValue(value);
	}
}
