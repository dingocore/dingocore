

const servicesForEndpoint = (state, endpoint)=>{
    state.automation.services.filter( e=>{
        e.endpoint_id === endpoint &&
        e.connection_id === endpoint.connection_id
    })
}

export {
    servicesForEndpoint
}