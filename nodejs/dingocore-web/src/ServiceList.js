
import { connect } from 'react-redux'; 
import Service from './Service';

import React, { Component } from 'react';

const ServiceList = ({endpoint_id, services})=>{
    console.log( 'render', services);
    return (
      <div>
        {services.map( e=>{
            return (
                <Service key={e.id} service={e}></Service>
            )
        })}
      </div>

    )
}

export default connect(
    (state, props)=>{
        console.log( "mapping for ", props, state);
        return {
            services: state.automation.services.filter(e=>e.endpoint_id==props.endpoint_id)
        }
    }
)(ServiceList);