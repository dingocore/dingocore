
import React from 'react';
import ServiceList from './ServiceList';

const Endpoint = ({id, name})=>{
    console.log( "*** ENDPOINT", id, name);
    return (
        <div>
            <span>Endpoint {name}</span>
            <div> my services</div>
            <ServiceList endpoint_id={id}></ServiceList>
        </div>
    )
}

export default Endpoint
