import React, { Component } from 'react';
import { Provider } from 'react-redux';
import Automation from './Automation';
import { compose, createStore, combineReducers, applyMiddleware } from 'redux'

import connectionsReducer from 'dingocore-redux/dist/reducers/connections';
import endpointsReducer from 'dingocore-redux/dist/reducers/endpoints';
import servicesReducer from 'dingocore-redux/dist/reducers/services';
import propertiesReducer from 'dingocore-redux/dist/reducers/properties';
import Pusher from 'dingocore-redux/dist/middleware/Pusher';

//import logo from './logo.svg';
import './App.css';

import '@patternfly/react-core/dist/styles/base.css';

import { Nav, NavItem, Grid, GridItem, PageHeader, PageSidebar } from '@patternfly/react-core';
import ConnectionList from './ConnectionList';

const rootReducer = combineReducers({
  automation: combineReducers({
    connections: connectionsReducer,
    endpoints: endpointsReducer,
    services: servicesReducer,
    properties: propertiesReducer,
  }),
  connection: (state={}, action)=>{
    if ( action.type == 'CONNECTED' ) {
      console.log( "--------------> ", action, action.payload.client );
      const newState = Object.assign( {}, state, { client: action.payload.client } );
      console.log( "new state", newState);
      return newState;
    } 
    return state;
  }
})


const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;


const store = createStore(
  rootReducer,
  composeEnhancers(
    applyMiddleware(Pusher)
  )
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
  <PageSidebar nav={Navigation} isNavOpen={true} />
);

/*
class App extends Component {
  render() {
    return (
      <Provider store={store}>
        <Page sidebar={Sidebar} header={Header}>
          <Connection store={store} url="ws://localhost:9001/">
            <PageSection>
              <EndpointList></EndpointList>
             </PageSection>
          </Connection>
        </Page>
      </Provider>
    );
  }
}
*/

class App extends Component {
  render() {
    return (
      <Provider store={store}>
        <Automation store={store} url="ws://localhost:9001/">
          <Grid>
            <GridItem sm={0} xl={4}>
              Navigation
            </GridItem>

            <GridItem sm={12} xl={4}>
              <ConnectionList/>
            </GridItem>
          </Grid>
        </Automation>
      </Provider>
    );
  }
}

export default App;
