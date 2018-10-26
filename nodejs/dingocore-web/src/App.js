import React, { Component } from 'react';
import { Provider } from 'react-redux';
import Automation from './Automation';
import Sidebar from './Sidebar';
import Header from './Header';

import store from './store.js';


//import logo from './logo.svg';
import './App.css';

import '@patternfly/react-core/dist/styles/base.css';

import { Grid, GridItem, Page, PageHeader, PageSidebar } from '@patternfly/react-core';
import ConnectionList from './ConnectionList';
const l_page__sidebar_BackgroundColor	= '#ccc';

class App extends Component {
  render() {
    return (
      <Provider store={store}>
        <Automation store={store} url="ws://localhost:9001/">
          <Page sidebar={Sidebar} header={Header}>
            <Grid>
              <GridItem>
                <ConnectionList />
              </GridItem>
            </Grid>
          </Page>
        </Automation>
      </Provider>
    );
  }
}

export default App;
