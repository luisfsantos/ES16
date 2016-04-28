package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.exception.MyDriveException;

public class ExecuteFileService extends TokenValidationService {

    public ExecuteFileService(long token, String path, String[] args) {
        super(token);
    }

    @Override
    protected void dispatch() throws MyDriveException {
        //TODO
    }
}
