import React, { Component } from 'react';
import { Table,Button } from 'reactstrap';
import ee from './EventManager';
class ListView extends Component {
  render() {
	 const options = this.props.options;
	 const fields = options.fields;
	 const data = options.data;
    return (
      <div>
		<Table>
			<thead><tr>
		{fields.map((field,i) => {
			return (<th key={i}>{field.heading}</th>)
		})}
			{options.hasEdit && <th>Edit</th>}
		    {options.hasDelete && <th>Delete</th>}
			</tr></thead>
			<tbody>
		{data.map((row,i) => {
	      var rowItems = [];
		  fields.map((field,i) => {
			  var v = row[field.key];
			  if(field.render) {
				  v = field.render(v,row);
			  }
			  if(field.clickable) {
				var clickablestyle = {cursor:'pointer'};
				if(field.clickablestyle) {
					clickablestyle = {...clickablestyle,...field.clickablestyle};
				}
				v = <span style={clickablestyle} onClick={this.onGridCellClick(row, field.key, v)}>{v}</span>
			  }
			  rowItems.push(v)
		  })
		  return (<tr key={i}>
		  {rowItems.map((col,j) => {
			  return (<td key={j}>{col}</td>)
		  })}
		  
		  {options.hasEdit && <td><Button onClick={this.onEdit(row)} outline color="primary">Edit</Button></td>}
		  {options.hasDelete && <td><Button onClick={this.onDelete(row)} outline color="danger">Delete</Button></td>}
		  </tr>)
		})}
		</tbody>
		</Table>
	  
	  </div>
    );
  }
  
  onEdit(row) {
	  const options = this.props.options;
	  return () => {
		  ee.emit('navigatePage', {page:options.editForm(row),row:row});
	  };
  }
  onDelete(row) {
	  const options = this.props.options;
	  return () => {
		  options.handleDelete(row);
	  };
  }
  onGridCellClick(row, fieldkey, fieldvalue) {
	  const options = this.props.options;
	  return () => {
	      options.handleGridCellClick(row, fieldkey, fieldvalue);
	  };
  }
}

export default ListView;