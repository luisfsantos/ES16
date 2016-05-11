package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.AddVariableService;
import pt.tecnico.myDrive.service.dto.VariableDto;

public class EnvironmentCommand extends MyDriveCommand {

	public EnvironmentCommand(MyDrive sh) {
		super(sh, "env", "create, edit or list environment variables, usage: env [name [value]]");
	}

	@Override
	public void execute(String[] args) {
		MyDrive md = (MyDrive) this.shell();
		AddVariableService avs;
		if (args.length == 0) {
			avs = new AddVariableService (md.getActiveToken(), null, null); 
			avs.execute();
			for (VariableDto var: avs.result()) {
				System.out.println(var.getName() + " = " + var.getValue());
			}
			return;
		}
		if (args.length == 1) {
			avs = new AddVariableService (md.getActiveToken(), args[0], null); 
			avs.execute();
			for (VariableDto var: avs.result()) {
				if (var.getName().equals(args[0])) {
					System.out.println(var.getName() + " = " + var.getValue());
					return;
				}
				
			}
		}
		if (args.length == 2) {
			avs = new AddVariableService (md.getActiveToken(), args[0], args[1]); 
			avs.execute();
			return;
		}
		else {
			println("Wrong arguments!!!\nUSAGE: env [name [value]]");
            return;
		}	
	}



}
