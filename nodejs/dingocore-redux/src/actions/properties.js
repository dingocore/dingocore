function add_property(connection_id, endpoint_id, service_id, id) {
    return {
        type: 'ADD_PROPERTY',
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
        type: 'UPDATE_PROPERTY_NAME',
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
        type: 'UPDATE_PROPERTY_DATATYPE',
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
        type: 'UPDATE_PROPERTY_SETTABLE',
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
        type: 'UPDATE_PROPERTY_VALUE',
        payload: {
            connection_id,
            endpoint_id,
            service_id,
            id,
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
}