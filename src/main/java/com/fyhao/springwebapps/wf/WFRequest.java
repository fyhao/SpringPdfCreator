package com.fyhao.springwebapps.wf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WFRequest {

	public List<WFStep> initsteps = new ArrayList<WFStep>();
	public List<WFStep> steps = new ArrayList<WFStep>();
	public Map<String, String> metadata; // author, subject, title
}
