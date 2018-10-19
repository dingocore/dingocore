import React, { Component } from 'react';
import Connection from './Connection';
import { createStore, combineReducers } from 'redux'

import connectionsReducer from 'dingocore-redux/dist/reducers/connections';
import endpointsReducer from 'dingocore-redux/dist/reducers/endpoints';
import servicesReducer from 'dingocore-redux/dist/reducers/services';
import propertiesReducer from 'dingocore-redux/dist/reducers/properties';

//import logo from './logo.svg';
import './App.css';

import '@patternfly/react-core/dist/styles/base.css';

import { Nav, NavItem, Page, PageHeader, PageSection, PageSidebar } from '@patternfly/react-core';

const rootReducer = combineReducers({
  automation: combineReducers( {
    connections: connectionsReducer,
    endpoints: endpointsReducer,
    services: servicesReducer,
    properties: propertiesReducer,
  })
})

const store = createStore(
  rootReducer,
  window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
)

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
      <Page sidebar={Sidebar} header={Header}>
        <Connection store={store} url="ws://localhost:9001/">
          <PageSection>Connected</PageSection>
        </Connection>
      </Page>
    );
  }
}

export default App;
