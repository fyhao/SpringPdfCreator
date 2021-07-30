package com.fyhao.springwebapps.wf.step;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.fyhao.springwebapps.wf.WFStep;

public class StepFactory {

	static Map<String, Class<?>> stepClasses = new HashMap<String, Class<?>>();
	
	static {
		stepClasses.put("setVar", SetVarStep.class);
		stepClasses.put("add", AddStep.class);
		stepClasses.put("httpget", HttpGetStep.class);
		stepClasses.put("generate", GenerateStep.class);
		stepClasses.put("metadata", MetadataStep.class);
		stepClasses.put("barcode", BarcodeStep.class);
		stepClasses.put("setWatermark", SetWaterMarkStep.class);
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
