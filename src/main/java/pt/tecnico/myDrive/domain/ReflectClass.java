package pt.tecnico.myDrive.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectClass {

	  public static void run(String name, String[] args) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		    Class<?> cls;
		    Method meth;
		    try { // name is a class: call main()
		      cls = Class.forName(name);
		      meth = cls.getMethod("main", String[].class);
		    } catch (ClassNotFoundException cnfe) { // name is a method
		      int pos;
		      if ((pos = name.lastIndexOf('.')) < 0) throw cnfe;
		      cls = Class.forName(name.substring(0, pos));
		      meth = cls.getMethod(name.substring(pos+1), String[].class);
		    }
		    meth.invoke(null, (Object)args); // static method (ignore return)
		  }
	  	
}
