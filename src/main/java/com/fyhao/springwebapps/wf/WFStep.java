package com.fyhao.springwebapps.wf;

import java.util.List;

public class WFStep {

	public String action;
	public String name;
	public String value;
	public String text;
	public String url;
	public String[] urls;
	public List<String> entries;
	public Float opacity;
	public Float rotation;
	
	public void execute(WFContext ctx) {}
}
