import actionTypes from '../constants/actionTypes';

function add_service(connection_id, endpoint_id, id) {
    return {
        type: actionTypes.ADD_SERVICE,
        payload: {
            connection_id,
            endpoint_id,
            id,
        }
    }
}

function update_service_name(connection_id, endpoint_id, id, name) {
    return {
        type: actionTypes.UPDATE_SERVICE_NAME,
        payload: {
            connection_id,
            endpoint_id,
            id,
            name
        }
    }
}

function update_service_type(connection_id, endpoint_id, id, type) {
    return {
        type: actionTypes.UPDATE_SERVICE_TYPE,
        payload: {
            connection_id,
            endpoint_id,
            id,
            type
        }
    }
}

export {
    add_service,
    update_service_name,
    update_service_type,
}