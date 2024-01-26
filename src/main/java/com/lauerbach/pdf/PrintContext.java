package com.lauerbach.pdf;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

public class PrintContext {
	PdfHelper helper;
	Object    currentData;

	public PrintContext(PdfHelper helper, Object currentData) {
		this.helper = helper;
	}

	public PdfHelper getHelper() {
		return helper;
	}

	public Object getCurrentData() {
		return currentData;
	}

	public Object getValue( String name) {
		try {
			return BeanUtils.getProperty(currentData,name);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			return null;
		}
	}
	
}
