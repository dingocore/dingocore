import actionTypes from '../constants/actionTypes';

function add_property(connection_id, endpoint_id, service_id, id) {
    return {
        type: actionTypes.ADD_PROPERTY,
        payload: {
            connection_id,
            endpoint_id,
            service_id,
            id
        }
    }
}

function update_property_name(connection_id, endpoint_id, service_id, id, name) {
    return {
        type: actionTypes.UPDATE_PROPERTY_NAME,
        payload: {
            connection_id,
            endpoint_id,
            service_id,
            id,
            name
        }
    }
}

function update_property_datatype(connection_id, endpoint_id, service_id, id, datatype) {
    return {
        type: actionTypes.UPDATE_PROPERTY_DATATYPE,
        payload: {
            connection_id,
            endpoint_id,
            service_id,
            id,
            datatype
        }
    }
}

function update_property_settable(connection_id, endpoint_id, service_id, id, settable) {
    return {
        type: actionTypes.UPDATE_PROPERTY_SETTABLE,
        payload: {
            connection_id,
            endpoint_id,
            service_id,
            id,
            settable
        }
    }
}

function update_property_value(connection_id, endpoint_id, service_id, id, value) {
    return {
        type: actionTypes.UPDATE_PROPERTY_VALUE,
        payload: {
            connection_id,
            endpoint_id,
            service_id,
            id,
            value
        }
    }
}

function push_update_property_value(property, value) {
    return {
        type: actionTypes.PUSH_UPDATE_PROPERTY_VALUE,
        payload: {
            property,
            value
        }
    }
}

export {
    add_property,
    update_property_name,
    update_property_datatype,
    update_property_settable,
    update_property_value,
    push_update_property_value,
}