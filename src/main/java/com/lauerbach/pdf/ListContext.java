package com.lauerbach.pdf;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class ListContext {
	String id;
	ListContext parent;
	
	HashMap<String, Object> localData = new HashMap<String, Object>();
	
	Iterator<Object> iterator= null;
	Object item= null;
	
	public ListContext( ListContext parent, String id, Collection<Object> repeatedData) {
		this.parent= parent;
		this.id= id;
		this.iterator= repeatedData.iterator();
	}
	
	public void setLocalVariable(String name, Object value) {
		localData.put(name, value);
	}

	public Object getLocalVariable(String name) {
		return localData.get(name);
	}

	public ListContext getParent() {
		return parent;
	}

	public void setParent(ListContext parent) {
		this.parent = parent;
	}

	public String getId() {
		return id;
	}

	public Iterator<Object> getIterator() {
		return iterator;
	}

	public boolean hashNext() {
		return iterator.hasNext();
	}

	public Object next() {
		item= iterator.next();
		return item;
	}
	
	public Object getItem() {
		return item;
	}
	
}
