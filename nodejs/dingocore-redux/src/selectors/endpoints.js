const endpointsForConnection = (state, connection) => {
    console.log( "SELECT ENDPOINTS", connection, state.automation.endpoints );
    return state.automation.endpoints.filter(e => {
        return e.connection_id === connection.id
    })
}

export {
    endpointsForConnection
}