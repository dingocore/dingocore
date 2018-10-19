import actionTypes from '../constants/actionTypes';

function add_connection(id) {
    return {
        type: actionTypes.ADD_CONNECTION,
        payload: {
            id
        }
    }
}

function update_connection_state(id, state) {
    return {
        type: actionTypes.UPDATE_CONNECTION_STATE,
        payload: {
            id,
            state
        }
    }
}

export {
    add_connection,
    update_connection_state,
}