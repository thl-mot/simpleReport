package com.lauerbach.pdf;

import java.util.HashMap;
import java.util.Iterator;

public class ComponentContext {
	ComponentContext parent;
	
	HashMap<String, Object> localData = new HashMap<String, Object>();
	
	Iterator<?> iterator;

	public ComponentContext( ComponentContext parent) {
		this.parent= parent;
	}
	
	public void setLocalVariable(String name, Object value) {
		localData.put(name, value);
	}

	public Object getLocalVariable(String name) {
		return localData.get(name);
	}

	public Iterator<?> getIterator() {
		return iterator;
	}

	public void setIterator(Iterator<?> i) {
		this.iterator = i;
	}

	public ComponentContext getParent() {
		return parent;
	}

	public void setParent(ComponentContext parent) {
		this.parent = parent;
	}

	
	
}
