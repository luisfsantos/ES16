package pt.tecnico.myDrive.domain;

public class Directory extends Directory_Base {
	
	public Directory() {
		super();
	}

	public File getFile(String name) throws NoSuchFileInThisDirectoryException{
		Iterator iterator = getFileSet().iterator();
		while(iterator.hasNext()){
			File file = iterator.next();
			if(file.getName() == name) return file;
		}
		throw new NoSuchFileInThisDirectoryException(name);
	}

	public boolean hasFile(String name){
		Iterator iterator = getFileSet().iterator();
		while(iterator.hasNext()){
			File file = iterator.next();
			if(file.getName() == name) return true;
		}
		return false;
	}

	public File lookup(String path){
		String name;
		int i;
		if(!path.contains('/'))
			return getFile(path);
		name = path.subString(1, path.indexOf("/", 1));
		path = path.subString(path.indexOf("/", 1) + 1);
		return lookup(path).getFile(name);
	}
	
}
