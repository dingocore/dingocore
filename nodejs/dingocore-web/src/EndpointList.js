
import { connect } from 'react-redux'; 

import React, { Component } from 'react';
import Endpoint from './Endpoint';


const EndpointList = ({endpoints})=>{
    console.log( 'render endpoints list', endpoints);
    return (
      <div>
        {endpoints.map( e=>{
            return (
                <Endpoint key={e.id} id={e.id} name={e.name}></Endpoint>
            )
        })}
      </div>

    )
}

export default connect(
    (state)=>{
        console.log( "mapping", state);
        return {
            endpoints: state.automation.endpoints
        }
    }
)(EndpointList);