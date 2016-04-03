package pt.tecnico.myDrive.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pt.ist.fenixframework.Atomic;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.exception.MyDriveException;

public abstract class MyDriveService {

	protected static final Logger log = LogManager.getRootLogger();

    @Atomic
    public final void execute() throws MyDriveException {
        dispatch();
    }

    static Manager getManager() {
        return Manager.getInstance();
    }

    protected abstract void dispatch() throws MyDriveException;

}
