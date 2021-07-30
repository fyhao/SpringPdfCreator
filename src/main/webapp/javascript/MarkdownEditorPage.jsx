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
| - | - |`
  }	
  constructor(props) {
    super(props);
    
	this.handleTextChange = this.handleTextChange.bind(this);
	this.handleGetWorkflowJsonClick = this.handleGetWorkflowJsonClick.bind(this);
	this.generateWorkflowJson = this.generateWorkflowJson.bind(this);
	this.handleGeneratePdfClick = this.handleGeneratePdfClick.bind(this);
  }	
  componentDidMount() {
	
  }
  handleTextChange(e) {
	var text = e.target.value;
	//while(html.indexOf('\n') > -1) html = html.replace('\n','<br>');
	this.setState({text:text});
  }

  handleGetWorkflowJsonClick(e) {
	var json = this.generateWorkflowJson();
	alert(JSON.stringify(json));
  }
  generateWorkflowJson() {
	var text = this.state.text;
	// md to html
	var html = document.getElementById('result').innerHTML;
	var json = {
		"steps" : [
			{"action":"setVar","name":"html","value":"<html><body>" + html + "</body></html>"},
			{"action":"generate"}
		]
	};
	return json;
  }
  handleGeneratePdfClick() {
	var json = this.generateWorkflowJson();
	fetch('/pdf/workflowpdf', {
		method:'POST',
		headers:{'Content-Type':'application/json'},
		body:JSON.stringify(json)
	}).then(r => r.blob())
	.then(blob => {
		var a = document.createElement('a')
		var b = URL.createObjectURL(blob);
		a.href = b;
		a.target = '_blank';
		a.dispatchEvent(new MouseEvent('click'))
	});
  }
  render() {
    return (
      <div>
        <span>Markdown Editor</span>
		<button onClick={this.handleGetWorkflowJsonClick}>Get Workflow JSON</button>
		<button onClick={this.handleGeneratePdfClick}>Generate PDF</button>
		<table border="0" cellpadding="5">
			<tr>
				<td valign="top">
					<textarea rows="10" cols="50" onChange={this.handleTextChange}>{this.state.text}</textarea>
				</td>
				<td id="result">
					<ReactMarkdown remarkPlugins={[gfm]} children={this.state.text}></ReactMarkdown>
				</td>
			</tr>
		</table>
      </div>
    );
  }
}

export default MarkdownEditorPage