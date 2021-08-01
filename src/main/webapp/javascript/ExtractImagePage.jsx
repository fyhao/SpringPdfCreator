import React, { Component } from "react";
import ReactMarkdown from 'react-markdown'
import gfm from 'remark-gfm'
import { Input, Form, FormGroup, Label, Button } from 'reactstrap';
import ee from './EventManager';
class ExtractImagePage extends Component {
  state = {
	selectedFile:null,
	url:''
  }	
  constructor(props) {
    super(props);
    this.handleGeneratePdfClick = this.handleGeneratePdfClick.bind(this);arguments
	this.handleUrlChange = this.handleUrlChange.bind(this);
	this.prevalidate = this.prevalidate.bind(this);
  }	
  componentDidMount() {
	
  }
  
  handleGeneratePdfClick() {
	if(!this.prevalidate()) {
	    return false;
    }
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
  prevalidate() {
	if(this.state.url == null || this.state.url.trim() == '') {
		ee.emit('infomsg', {msg:'URL must not be blank',infocolor:'danger'});
		return false;
	}
	
	return true;
  }
  render() {
    return (
      <div>
        <Form>
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

export default ExtractImagePage