
import React from 'react';
import EndpointList from './EndpointList';
import { connect } from 'react-redux';
import { endpointsForConnection } from 'dingocore-redux/dist/selectors/endpoints'

import './Connection.css';

const Connection = ({ connection, endpoints }) => {
    console.log( "RENDER CONNECTION", endpoints);
    return (
        <div className="Connection">
            <div>{connection.id}</div>
            <EndpointList endpoints={endpoints}/>
        </div>
    )
}

export default connect(
    (state, props) =>{
        console.log( "map state for Connection", endpointsForConnection(state, props.connection) );
        return {
            endpoints: endpointsForConnection(state, props.connection)
        }
    }
)(Connection)