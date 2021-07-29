import React, { Component } from "react";
import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom';
import { MemoryRouter } from 'react-router'
//import "bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import './Main.scss';
import '../css/Main.css';

import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';
import Navigation from './Navigation';


class Main extends Component {
    render() {
        return (
            <div id="main">
                <Navigation />
            </div>
        );
    }
}

ReactDOM.render(
    <MemoryRouter>
		<Main />
	</MemoryRouter>,
    document.getElementById('react-mountpoint')
);
/*

webpack serve --mode development --entry ./src/main/webapp/javascript/Main.jsx --output-path ./src/main/resources/static/dist --port 8081 --liveReload
webpack --mode development --entry ./src/main/webapp/javascript/Main.jsx --output-path ./src/main/resources/static/dist --stats-error-details


file upload spring boot react
https://www.devglan.com/react-js/file-upload-react-spring-rest
*/