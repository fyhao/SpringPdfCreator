import React, { Component } from "react";
import ReactMarkdown from 'react-markdown'
import gfm from 'remark-gfm'
import { Input } from 'reactstrap';
class ExtractImagePage extends Component {
  state = {
	selectedFile:null,
	url:null
  }	
  constructor(props) {
    super(props);
    this.handleGeneratePdfClick = this.handleGeneratePdfClick.bind(this);arguments
	this.handleUrlChange = this.handleUrlChange.bind(this);
  }	
  componentDidMount() {
	
  }
  
  handleGeneratePdfClick() {
	var json = {
		"type":"extractfromurl",
		"url" :this.state.url
	};
	fetch('/pdf/extractimagefrompdf', {
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
  handleUploadChange(e) { 
	  e.preventDefault();
      var file = e.target.files[0];
	  const formData = new FormData();
	  formData.append('file', file);
	  fetch('pdf/uploadpdfextractimage', {
	  	method: 'post',
	  	body: formData
	  }).then(r => r.blob())
	.then(blob => {
		var a = document.createElement('a')
		var b = URL.createObjectURL(blob);
		a.href = b;
		a.target = '_blank';
		a.dispatchEvent(new MouseEvent('click'))
	});
  }
  handleUrlChange(e) {
	this.setState({url:e.target.value});
  }
  render() {
    return (
      <div>
        
		<p><Input type="file" name="fileupload" id="fileupload" onChange={this.handleUploadChange} /></p>
		<p>
		<Input type="text" name="url" id="url" onChange={this.handleUrlChange} value={this.state.url}/>
		<button onClick={this.handleGeneratePdfClick}>Generate PDF</button>
		</p>
		
      </div>
    );
  }
}

export default ExtractImagePage