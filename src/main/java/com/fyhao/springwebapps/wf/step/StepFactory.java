package com.fyhao.springwebapps.wf.step;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import com.fyhao.springwebapps.wf.WFStep;

public class StepFactory {
	
	static final String PACKAGE_NAME = "com.fyhao.springwebapps.wf.step";

	static Map<String, Class<?>> stepClasses = new HashMap<String, Class<?>>();
	
	static {
		// create scanner and disable default filters (that is the 'false' argument)
		final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		// add include filters which matches all the classes (or use your own)
		provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));

		// get matching classes defined in the package
		final Set<BeanDefinition> classes = provider.findCandidateComponents(PACKAGE_NAME);

		// this is how you can load the class type from BeanDefinition instance
		for (BeanDefinition bean: classes) {
		    Class<?> clazz;
			try {
				clazz = Class.forName(bean.getBeanClassName());
				if(clazz.getSuperclass().equals(WFStep.class)) {
					String stepName = deriveStepName(clazz.getCanonicalName());
					stepClasses.put(stepName, clazz);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		}
		
	}
	
	public static String deriveStepName(String clazzName) {
		String t = clazzName.replace(PACKAGE_NAME + ".", "");
		t = t.replace("Step", "");
		t = Character.toLowerCase(t.charAt(0)) + t.substring(1);
		return t;
	}
	
	public static WFStep createStep(WFStep step) {
		Class<?> targetClass = stepClasses.get(step.action);
		Object obj = null;
		try {
			obj = targetClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(obj == null) return null;
		Field[] sourceFields = step.getClass().getDeclaredFields();
		for(Field field : sourceFields) {
			field.setAccessible(true);
			try {
				field.set(obj, field.get(step));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return (WFStep)obj;
	}
	
	public static void main(String args[]) {
		WFStep s = new WFStep();
		s.action = "setVar";
		WFStep r = StepFactory.createStep(s);
		System.out.println("is r setVarStep: " + (r instanceof SetVarStep));
	}
}
