package pt.tecnico.myDrive.presentation;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import pt.tecnico.myDrive.service.ImportService;

import java.io.File;

public class ImportCommand extends MyDriveCommand {
    public ImportCommand(MyDrive sh) {
        super(sh, "import", "import myDrive domain. usage: import pathToFile or resourceFile");
    }

    @Override
    void execute(String[] args) {
        if (args.length < 1) {
            println("Wrong arguments!!!\nUSAGE: import pathToFile or resourceFile");
            return;
        }
        try {
            SAXBuilder builder = new SAXBuilder();
            File file = (args[0].startsWith(".") || args[0].startsWith("/")) ? new File(args[0]) : resourceFile(args[0]);
            Document doc = builder.build(file);
            new ImportService(doc).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private File resourceFile(String filename) {
        log.trace("Resource: "+filename);
        ClassLoader classLoader = getClass().getClassLoader();
        if (classLoader.getResource(filename) == null) return null;
        return new java.io.File(classLoader.getResource(filename).getFile());
    }
}
