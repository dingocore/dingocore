
const propertiesForService = (state, service)=>{
    state.automation.properties.filter( e=>{
        e.service_id === service.id &&
        e.endpoint_id === service.endpoint_id &&
        e.connection_id === service.connection_id
    })
}

export {
    propertiesForService
}