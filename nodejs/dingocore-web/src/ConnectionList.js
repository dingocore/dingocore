
import React from 'react';
import { connect } from 'react-redux';
import Connection from './Connection';
import { allConnections } from 'dingocore-redux/dist/selectors/connections'

const ConnectionList = ({ connections }) => {
    return (
        <div>
            {
                connections.map(e => {
                    return (<Connection connection={e} />);
                })
            }
        </div>
    )

}

export default connect(
    (state) => {
        return {
            connections: allConnections(state)
        }
    }
)(ConnectionList);

