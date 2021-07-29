import React, { Component } from "react";
import ReactMarkdown from 'react-markdown'
import gfm from 'remark-gfm'

class MarkdownEditorPage extends Component {
  state = {
	text:`> A block quote with ~strikethrough~ and a URL: https://reactjs.org.

* Lists
* [ ] todo
* [x] done

A table:

| a | b |
| - | - |`,
	html:''
  }	
  constructor(props) {
    super(props);
    
	this.handleTextChange = this.handleTextChange.bind(this);
	this.handleGetWorkflowJsonClick = this.handleGetWorkflowJsonClick.bind(this);
	this.generateWorkflowJson = this.generateWorkflowJson.bind(this);
  }	
  handleTextChange(e) {
	var text = e.target.value;
	var html = text;
	//while(html.indexOf('\n') > -1) html = html.replace('\n','<br>');
	this.setState({text:text,html:html});
  }

  handleGetWorkflowJsonClick(e) {
	var json = this.generateWorkflowJson();
	alert(JSON.stringify(json));
  }
  generateWorkflowJson() {
	var text = this.state.text;
	var json = {
		"steps" : [
			{"action":"setVar","name":"html","value":"<html><body>" + text + "</body></html>"},
			{"action":"generate"}
		]
	};
	return json;
  }
  render() {
    return (
      <div>
        <span>Markdown Editor</span>
		<button onClick={this.handleGetWorkflowJsonClick}>Get Workflow JSON</button>
		<table border="0" cellpadding="5">
			<tr>
				<td>
					<textarea rows="10" cols="50" onChange={this.handleTextChange}>{this.state.text}</textarea>
				</td>
				<td>
					<ReactMarkdown remarkPlugins={[gfm]} children={this.state.html}></ReactMarkdown>
				</td>
			</tr>
		</table>
      </div>
    );
  }
}

export default MarkdownEditorPage