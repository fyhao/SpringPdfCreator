import React, { Component } from "react";
import ReactMarkdown from 'react-markdown'
import gfm from 'remark-gfm'
import { Input } from 'reactstrap';
class PasswordprotectPage extends Component {
  state = {
	selectedFile:null,
	url:null,
	pwd:null
  }	
  constructor(props) {
    super(props);
    this.handleGeneratePdfClick = this.handleGeneratePdfClick.bind(this);
	this.handleUrlChange = this.handleUrlChange.bind(this);
	this.handlePwdChange = this.handlePwdChange.bind(this);
	this.handleUploadChange = this.handleUploadChange.bind(this);
  }	
  componentDidMount() {
	
  }
  
  handleGeneratePdfClick() {
	var json = {
		"type":"extractfromurl",
		"url" :this.state.url,
		"pwd": this.state.pwd
	};
	fetch('/pdf/passwordprotectfrompdf', {
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
	  formData.append('pwd', this.state.pwd);
	  fetch('pdf/uploadpdfpasswordprotect', {
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
  handlePwdChange(e) {
	this.setState({pwd:e.target.value});
  }
  render() {
    return (
      <div>
        <p><Input type="text" name="pwd" id="url" onChange={this.handlePwdChange} value={this.state.pwd} /></p>
		<p><Input type="file" name="fileupload" id="fileupload" onChange={this.handleUploadChange} /></p>
		<p>
		<Input type="text" name="url" id="url" onChange={this.handleUrlChange} value={this.state.url}/>
		<button onClick={this.handleGeneratePdfClick}>Generate PDF</button>
		</p>
		
      </div>
    );
  }
}

export default PasswordprotectPage