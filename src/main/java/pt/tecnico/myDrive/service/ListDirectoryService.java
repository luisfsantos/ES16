package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.dto.FileDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ListDirectoryService extends TokenValidationService {

    private List<FileDto> lsDir;

    public ListDirectoryService(long token) {
        super(token);
    }

    @Override
    protected void dispatch() throws MyDriveException {
        super.dispatch();
        Directory currDir = session.getCurrentDir();
        Directory parent = currDir.getParent();
        lsDir = new ArrayList<>();

        FileDto newDto = new FileDto(
                "Directory",
                currDir.getPermissions(),
                currDir.getSize(),
                currDir.getOwner().getName(),
                currDir.getId(),
                currDir.getLastModified().toString("dd-MM-YYYY-HH:mm:ss"),
                ".");
        lsDir.add(newDto);

        newDto = new FileDto(
                "Directory",
                parent.getPermissions(),
                parent.getSize(),
                parent.getOwner().getName(),
                parent.getId(),
                parent.getLastModified().toString("dd-MM-YYYY-HH:mm:ss"),
                "..");
        lsDir.add(newDto);

        Set<File> dirContent = currDir.getFileSet(session.getCurrentUser());
        for (File f : dirContent) {
            String type;

            if (f instanceof App) type = "App";
            else if (f instanceof Link) type = "Link";
            else if (f instanceof PlainFile) type = "PlainFile";
            else type = "Directory";

            newDto = new FileDto(
                    type,
                    f.getPermissions(),
                    f.getSize(),
                    f.getOwner().getName(),
                    f.getId(),
                    f.getLastModified().toString("dd-MM-YYYY-HH:mm:ss"),
                    f.getName());
            lsDir.add(newDto);
        }

        Collections.sort(lsDir);
    }

    public final List<FileDto> result() {
        return lsDir;
    }
}