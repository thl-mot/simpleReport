package com.lauerbach.pdf;

import java.util.HashMap;
import java.util.Iterator;

public class ComponentContext {
	HashMap<String, Object> localData = new HashMap<String, Object>();
	
	Iterator<?> iterator;

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

}
