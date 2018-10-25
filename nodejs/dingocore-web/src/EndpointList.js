
import { connect } from 'react-redux'; 

import React, { Component } from 'react';
import Endpoint from './Endpoint';


const EndpointList = ({endpoints=[]})=>{
    console.log( 'render endpoints list', endpoints);
    return (
      <div>
        {endpoints.map( e=>{
            return (
                <Endpoint endpoint={e}/>
            )
        })}
      </div>

    )
}

/*
export default connect(
    (state)=>{
        return {
            endpoints: state.automation.endpoints
        }
    }
)(EndpointList);
*/
export default EndpointList;