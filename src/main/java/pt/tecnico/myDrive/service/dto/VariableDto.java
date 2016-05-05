package pt.tecnico.myDrive.service.dto;

public class VariableDto {
	private String name;
	private String value;
	
	
	public VariableDto (String name, String value){
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public int compareTo(VariableDto other) {
		return getName().compareToIgnoreCase(other.getName());
	}

}
