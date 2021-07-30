import React, { Component } from "react";
import ReactMarkdown from 'react-markdown'
import gfm from 'remark-gfm'

class ExtractImagePage extends Component {
  state = {
	
  }	
  constructor(props) {
    super(props);
    this.handleGeneratePdfClick = this.handleGeneratePdfClick.bind(this);
  }	
  componentDidMount() {
	
  }
  
  handleGeneratePdfClick() {
	var json = {
		"type":"extractfromurl",
		"url" :"https://springpdfcreator.herokuapp.com/pdf/getpdf?url=https://springpdfcreator.herokuapp.com/test/testrequest2"
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
  render() {
    return (
      <div>
        
		<button onClick={this.handleGeneratePdfClick}>Generate PDF</button>
		
      </div>
    );
  }
}

export default ExtractImagePage