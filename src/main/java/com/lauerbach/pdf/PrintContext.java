package com.lauerbach.pdf;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class PrintContext {
	PrintContext parent;

	PdfHelper helper;
	Object currentData;

	HashMap<String, Object> localData = new HashMap<String, Object>();

	public PrintContext(PdfHelper helper, Object currentData) {
		this.helper = helper;
		this.currentData = currentData;
		this.parent = null;
	}

	public PrintContext(PrintContext parentContext) {
		this.parent = parentContext;
		this.helper = parentContext.getHelper();
		this.currentData = currentData;
	}

	public PdfHelper getHelper() {
		return helper;
	}

	public Object getCurrentData() {
		return currentData;
	}

	private Object getRecursiveValue( PrintContext context, Object data, String expression) {
		int pos= expression.indexOf('.');
		String name;
		String subExpression= null;
		if ( pos<0) {
			name= expression;
		} else if (pos==0) {
			// loopks like an error
			return null;
		} else {
			name = expression.substring(0, pos);
			subExpression = expression.substring( pos+1);
		}
		try {
			Object localValue= null;
			if (context!=null) {
				localValue = context.getLocalVariable(name);
			}
			if (localValue != null) {
				// fine
			} else if ( "PARENT".equals(name) && context!=null && context.parent!=null) {
				// referenze parent context
				localValue= getRecursiveValue(context.parent, context.parent.currentData, subExpression);
			} else if (currentData instanceof Map) {
				localValue= ((Map) data).get(name);
			} else {
				localValue=  BeanUtils.getProperty(data, name);
			}
			if (subExpression!=null && subExpression.trim().length()>0) {
				return getRecursiveValue( null, localValue, subExpression);
			} else {
				return localValue;
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			return null;
		}
	}

	public Object getValue(String name) {
		return getRecursiveValue( this, currentData, name);
	}

	public void setLocalVariable(String name, Object value) {
		localData.put(name, value);
	}
	
	public Object getLocalVariable(String name) {
		return localData.get(name);
	}

}
