import React, { Component } from "react";

class MarkdownEditorPage extends Component {
  state = {
	text:'',
	html:''
  }	
  constructor(props) {
    super(props);
	this.handleTextChange = this.handleTextChange.bind(this);
	this.markdownToHTML = this.markdownToHTML.bind(this);
  }	
  handleTextChange(e) {
	var text = e.target.value;
	var html = this.markdownToHTML(text);
	this.setState({text:text, html:html});
  }
  markdownToHTML(text) {
	var html = text;
	return html;
  }
  render() {
    return (
      <div>
        <span>Markdown Editor</span>
		<table border="0" cellpadding="5">
			<tr>
				<td>
					<textarea rows="10" cols="50" onChange={this.handleTextChange}>{this.state.text}</textarea>
				</td>
				<td>
					{this.state.html}
				</td>
			</tr>
		</table>
      </div>
    );
  }
}

export default MarkdownEditorPage