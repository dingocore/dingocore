
import React from 'react';
import { connect } from 'mqtt';

const Connection = ({ connection, endpoints }) => {
    return (
        <div>
            <div>{connection.id}</div>
            <EndpointList endpoints={endpoints}/>
        </div>
    )
}

export default connect(
    (state, props) =>{
        return {
            endpoints: state.automation.endpoints.filter(e=>e.connection_id = props.connection.id)
        }
    }
)