package pt.tecnico.myDrive.domain;


import org.jdom2.Element;
import org.joda.time.DateTime;

import pt.tecnico.myDrive.exception.AccessDeniedException;

import pt.tecnico.myDrive.exception.IsNotDirectoryException;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class PlainFile extends PlainFile_Base {

    protected PlainFile() {
        super();
    }

    public PlainFile(String name, User owner, Directory parent, String content) {
        this.initFile(name, owner.getUmask(), owner, parent);
        this.setContent(content);
    }

    public PlainFile(String name, Directory parent, String content) {
        this.initFile(name, Manager.getInstance().getSuperUser().getUmask(), Manager.getInstance().getSuperUser(), parent);
        this.setContent(content);
    }

    public PlainFile(Manager manager, Element plainNode) throws UnsupportedEncodingException {
        String contents = new String(plainNode.getChildText("contents").getBytes("UTF-8"));
        setContent(contents);
        super.xmlImport(manager, plainNode);
    }

    public File lookup(String path, User user) throws IsNotDirectoryException {
        throw new IsNotDirectoryException(this.getName());
    }

    File lookup(String path, User user, int psize) throws IsNotDirectoryException {
        throw new IsNotDirectoryException(this.getName());
    }

    @Override
    public String getContent(){
        throw new AccessDeniedException("read", super.getName());
    }

    @Override
    public int getSize() {
        return viewContent().length();
    }

    @Override
    public String getType() {
        return "PlainFile";
    }

    protected String viewContent() {
    	return super.getContent();
    }

    @Override
    public void setContent(String content){
        super.setContent(content);
        this.setLastModified(new DateTime());
    }

    public String read(User user) {
        if (user.hasPermission(this, Mask.READ)) {
            return super.getContent();
        } else {
            throw new AccessDeniedException("read", super.getName());
        }
    }

    @Override
    public Element xmlExport() {
        Element plainElement = super.xmlExport();
        plainElement.setName("plain");

        Element contentsElement = new Element("contents");
        contentsElement.setText(getContent());
        plainElement.addContent(contentsElement);

        return plainElement;
    }

    public void write(User u, String content){
        if (u.hasPermission(this, Mask.WRITE)) {
            setContent(content);
        }
        else {
            throw new AccessDeniedException("write", super.getName());
        }
    }

	@Override
	public void execute(User user, String[] args) {
		if (user.hasPermission(this, Mask.EXEC)) {
			String[] content = this.viewContent().split(System.getProperty("line.separator"));
			for (String line: content) {
				String[] lineContent = line.split(" ");
				File destFile =  this.getParent().lookup(lineContent[0], user);		// FIXME exception
				String[] arguments = Arrays.copyOfRange(lineContent, 1, lineContent.length);
				destFile.execute(user, arguments);
			}
		}
		else {
            if (hasExtension()) {
                String extension = this.getExtension();
                String [] arg = { this.getName() };
                user.getDefaultApp(extension).execute(user, arg);
            } else {
                throw new AccessDeniedException("execute", this.getName());
            }

		}
	}

    
	
}


