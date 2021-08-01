import React, { Component } from "react";
import ReactMarkdown from 'react-markdown'
import gfm from 'remark-gfm'
import { Input, Form, FormGroup, Label, Button } from 'reactstrap';
import ee from './EventManager';
class PasswordprotectPage extends Component {
  state = {
	selectedFile:null,
	url:'',
	pwd:''
  }	
  constructor(props) {
    super(props);
    this.handleGeneratePdfClick = this.handleGeneratePdfClick.bind(this);
	this.handleUrlChange = this.handleUrlChange.bind(this);
	this.handlePwdChange = this.handlePwdChange.bind(this);
	this.handleUploadChange = this.handleUploadChange.bind(this);
	this.prevalidate = this.prevalidate.bind(this);
  }	
  componentDidMount() {
	
  }
  
  handleGeneratePdfClick() {
	if(!this.prevalidate(1)) {
		return false;
	}
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
	  if(!this.prevalidate(2)) {
		  return false;
	  }
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
  prevalidate(type) {
	if(this.state.pwd == null || this.state.pwd.trim() == '') {
		ee.emit('infomsg', {msg:'Password must not be blank',infocolor:'danger'});
		return false;
	}
	if(type == 1) {
		if(this.state.url == null || this.state.url.trim() == '') {
			ee.emit('infomsg', {msg:'URL must not be blank',infocolor:'danger'});
			return false;
		}
	}
	return true;
  }
  render() {
    return (
      <div>
		<Form>
			<FormGroup>
				<Label>Password</Label>
				<Input type="text" name="pwd" id="url" onChange={this.handlePwdChange} value={this.state.pwd} />
			</FormGroup>
			<FormGroup>
				<Label>Upload File</Label>
				<Input type="file" name="fileupload" id="fileupload" onChange={this.handleUploadChange} />
			</FormGroup>
			<FormGroup>
				<Label>URL</Label>
				<Input type="text" name="url" id="url" onChange={this.handleUrlChange} value={this.state.url}/>
				<Button onClick={this.handleGeneratePdfClick}>Generate PDF</Button>
			</FormGroup>
		</Form>
      </div>
    );
  }
}

export default PasswordprotectPage