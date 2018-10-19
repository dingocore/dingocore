import actionTypes from '../constants/actionTypes';

function add_endpoint(connection_id, endpoint_id) {
    return {
        type: actionTypes.ADD_ENDPOINT,
        payload: {
            connection_id,
            endpoint_id
        }
    }
}

function update_endpoint_name(connection_id, endpoint_id, name) {
    return  {
        type: actionTypes.UPDATE_ENDPOINT_NAME,
        payload: {
            connection_id,
            endpoint_id,
            name
        }
    }
}

function update_endpoint_model(connection_id, endpoint_id, model) {
    return  {
        type: actionTypes.UPDATE_ENDPOINT_MODEL,
        payload: {
            connection_id,
            endpoint_id,
            model
        }
    }
}

function update_endpoint_manufacturer(connection_id, endpoint_id, manufacturer) {
    return  {
        type: actionTypes.UPDATE_ENDPOINT_MANUFACTURER,
        payload: {
            connection_id,
            endpoint_id,
            manufacturer
        }
    }
}

function update_hardware_revision(connection_id, endpoint_id, revision) {
    return  {
        type: actionTypes.UPDATE_HARDWARE_REVISION,
        payload: {
            connection_id,
            endpoint_id,
            revision
        }
    }
}

export {
    add_endpoint,
    update_endpoint_name,
    update_endpoint_model,
    update_endpoint_manufacturer,
    update_hardware_revision,
}