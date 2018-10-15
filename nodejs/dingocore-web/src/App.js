import React, { Component } from 'react';
//import logo from './logo.svg';
import { Connector } from 'mqtt-react';
import './App.css';
import { subscribe } from 'mqtt-react';

import dingo from 'dingocore-surface';

import '@patternfly/react-core/dist/styles/base.css';

import { Nav, NavItem, Page, PageHeader, PageSection, PageSidebar } from '@patternfly/react-core';


console.log( dingo );

const Debug = (props) => (
  <ul>
    {props.data.map( message => <li>{message}</li>)}
  </ul>
);

const customDispatch = (topic, message)=>{
  console.log( topic, new TextDecoder("UTF-8").decode(message) );
}

const DebugSubscribed = subscribe({
  topic: '#',
  dispatch: customDispatch
})(Debug);

const Header = (
  <PageHeader logo="Logo" toolbar="Toolbar" avatar=" | Avatar" />
);

const Navigation = (
  <Nav>
    <NavItem>One</NavItem>
    <NavItem>Two</NavItem>
    <NavItem>Three</NavItem>
  </Nav>
);

const Sidebar = (
  <PageSidebar nav={Navigation} isNavOpen={true}/>
);



class App extends Component {
  render() {
    return (
      <Connector mqttProps="ws://localhost:9001/">
        <Page sidebar={Sidebar} header={Header}>
          <PageSection>Hi</PageSection>
        </Page>
      </Connector>
    );
  }
}

        //<div className="App">
          //<header className="App-header">
            //<h1 className="App-title">Welcome</h1>
          //</header>
          //<DebugSubscribed/>
        //</div>
export default App;
