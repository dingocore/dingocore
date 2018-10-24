
import React from 'react';
import { connect } from 'react-redux';
import Connection from './Connection';

const ConnectionList = ({connections})=>{
    return (
        <div>
            {
                connections.map(e=>{
                    <Connection entity={e}/>
                })
            }
        </div>
    )

}

export default connect(
    (state)=>{
        connections: this.state.connections
    }
)(ConnectionList);

