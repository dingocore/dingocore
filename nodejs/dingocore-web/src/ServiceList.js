
import { connect } from 'react-redux'; 
import Service from './Service';

import React, { Component } from 'react';

const ServiceList = ({services})=>{
    return (
      <div>
        {services.map( e=>{
            return (
                <Service service={e}></Service>
            )
        })}
      </div>

    )
}

export default ServiceList;
