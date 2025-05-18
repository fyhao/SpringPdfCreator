import React, { Component } from "react";
import ReactMarkdown from 'react-markdown'
import gfm from 'remark-gfm'

import { Button } from 'reactstrap';
import ee from './EventManager';
class MarkdownEditorPage extends Component {
  state = {
    text: localStorage.getItem('md_text') || `> A block quote with ~strikethrough~ and a URL: https://reactjs.org.\n\n* Lists\n* [ ] todo\n* [x] done\n\nA table:\n\n| a | b |\n| - | - |`,
    author: localStorage.getItem('md_author') || '',
    subject: localStorage.getItem('md_subject') || '',
    title: localStorage.getItem('md_title') || '',
    snippet: '',
    snippets: [
      { label: 'Hello World', value: '```js\nconsole.log("Hello, world!");\n```' },
      { label: 'Sample Table', value: '| Name | Age |\n|------|-----|\n| John | 30  |' },
      { label: 'Blockquote', value: '> This is a blockquote.' }
    ]
  }	
  constructor(props) {
    super(props);
    
	this.handleTextChange = this.handleTextChange.bind(this);
	this.handleGetWorkflowJsonClick = this.handleGetWorkflowJsonClick.bind(this);
	this.generateWorkflowJson = this.generateWorkflowJson.bind(this);
	this.handleGeneratePdfClick = this.handleGeneratePdfClick.bind(this);
  }	
  componentDidMount() {
    // Load from localStorage if available
    this.setState({
      text: localStorage.getItem('md_text') || this.state.text,
      author: localStorage.getItem('md_author') || this.state.author,
      subject: localStorage.getItem('md_subject') || this.state.subject,
      title: localStorage.getItem('md_title') || this.state.title
    });
  }
  handleTextChange(e) {
    var text = e.target.value;
    this.setState({text:text});
    localStorage.setItem('md_text', text);
  }
  handleMetaChange = (e) => {
    const { name, value } = e.target;
    this.setState({ [name]: value });
    localStorage.setItem('md_' + name, value);
  }
  handleSnippetChange = (e) => {
    const snippet = e.target.value;
    if (snippet) {
      this.setState({ text: this.state.text + '\n' + snippet, snippet: '' }, () => {
        localStorage.setItem('md_text', this.state.text);
      });
    }
  }
  handleGetWorkflowJsonClick(e) {
	var json = this.generateWorkflowJson();
	alert(JSON.stringify(json));
  }
  generateWorkflowJson() {
    var text = this.state.text;
    var html = document.getElementById('result').innerHTML;
    var json = {
      metadata: {
        author: this.state.author,
        subject: this.state.subject,
        title: this.state.title
      },
      steps: [
        { action: 'setVar', name: 'html', value: '<html><body>' + html + '</body></html>' },
        { action: 'generate' }
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
		<Button onClick={this.handleGetWorkflowJsonClick}>Get Workflow JSON</Button>
		<Button onClick={this.handleGeneratePdfClick}>Generate PDF</Button>
		<div style={{ margin: '10px 0' }}>
          <input type="text" name="author" placeholder="Author" value={this.state.author} onChange={this.handleMetaChange} style={{marginRight:'5px'}} />
          <input type="text" name="subject" placeholder="Subject" value={this.state.subject} onChange={this.handleMetaChange} style={{marginRight:'5px'}} />
          <input type="text" name="title" placeholder="Title" value={this.state.title} onChange={this.handleMetaChange} style={{marginRight:'5px'}} />
        </div>
        <div style={{ margin: '10px 0' }}>
          <select value={this.state.snippet} onChange={e => this.handleSnippetChange({target: {value: e.target.value}})}>
            <option value="">Insert snippet...</option>
            {this.state.snippets.map((s, i) => (
              <option key={i} value={s.value}>{s.label}</option>
            ))}
          </select>
        </div>
        <table border="0" cellPadding="5">
          <tbody>
            <tr>
              <td valign="top">
                <textarea rows="10" cols="50" onChange={this.handleTextChange} value={this.state.text}></textarea>
              </td>
              <td id="result">
                <ReactMarkdown remarkPlugins={[gfm]} children={this.state.text}></ReactMarkdown>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    );
  }
}

export default MarkdownEditorPage