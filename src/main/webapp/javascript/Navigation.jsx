import React, { Component } from "react";
import { Navbar, Nav, Form, FormControl, Button } from 'react-bootstrap';
import { Switch, Route, Link } from 'react-router-dom';
import Home from './Home';
import MarkdownEditorPage from './MarkdownEditorPage';
import ExtractImagePage from './ExtractImagePage';
import PasswordprotectPage from './PasswordprotectPage';
import ee from './EventManager';
import { NavLink,NavItem,Alert } from 'reactstrap';
import * as MyConstants from './MyConstants';
class Navigation extends Component {
	
  constructor(props) {
    super(props);
    this.state = {
		currentPage:<PasswordprotectPage />,
		roles : [],
		username:'na',
		infomsg : ''
    };
	
  }
  componentDidMount() {
  }
  componentWillMount() {
	ee.on('navigatePage', this.onNavigatePage, this);
	ee.on('infomsg', this.onInfomsg, this);
  }
  componentWillUnmount() {
	ee.off('navigatePage', this.onNavigatePage);
	ee.off('infomsg', this.onInfomsg);
  }
  onNavigatePage(opts) {
	var me = this;
	me.setState({currentPage:opts.page})
  }
  onInfomsg(opts) {
	var me = this;
	var infocolor = 'success';
	if(opts.infocolor) infocolor = opts.infocolor;
	me.setState({infomsg:opts.msg,infocolor:infocolor});
  }
  handleDismissInfomsg() {
	var me = this;
	ee.emit('infomsg', {msg:''});
  }
  
  handleLogoutClick() {
	  window.location = 'logout';
  }
  render() {
    return (
      <div>
        <div>
          <Navbar className="navbar navbar-expand-lg navbar-dark bg-primary">
            <Navbar.Brand as={Link} to="/" >Portal</Navbar.Brand>
            <Navbar.Collapse>
              <Nav className="mr-auto" style={{ width: "50%" }}>
			  	<NavItem>
					<NavLink onClick={() => {ee.emit('navigatePage',{page:<Home />})}} href="#">
						Home
					</NavLink>
			    </NavItem>
				<NavItem>
					<NavLink onClick={() => {ee.emit('navigatePage',{page:<MarkdownEditorPage />})}} href="#">
						Markdown Editor
					</NavLink>
			    </NavItem>
				<NavItem>
					<NavLink onClick={() => {ee.emit('navigatePage',{page:<ExtractImagePage />})}} href="#">
						Extract Image
					</NavLink>
			    </NavItem>
				<NavItem>
					<NavLink onClick={() => {ee.emit('navigatePage',{page:<PasswordprotectPage />})}} href="#">
						Password Protect
					</NavLink>
			    </NavItem>
              </Nav>
			  <Nav className="ml-auto justify-content-end" style={{ width: "50%" }}>
	 		    <NavItem>
				  <Button variant="outline-success" onClick={this.handleLogoutClick}>Logout</Button>
				</NavItem>
			  </Nav>
            </Navbar.Collapse>
          </Navbar>
        </div>
		
        <div style={{margin:"10px"}}>
			{this.state.currentPage}
        </div>
      </div>
    );
  }
}

export default Navigation