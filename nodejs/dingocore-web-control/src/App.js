import React, { Component } from 'react';
//import logo from './logo.svg';
import { Connector } from 'mqtt-react';
import './App.css';
import { subscribe } from 'mqtt-react';

import dingo from 'dingocore-surface';

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


class App extends Component {
  render() {
    return (
      <Connector mqttProps="ws://localhost:9001/">
        <div className="App">
          <header className="App-header">
            <h1 className="App-title">Welcome</h1>
          </header>
          <DebugSubscribed/>
        </div>
      </Connector>
    );
  }
}

export default App;
