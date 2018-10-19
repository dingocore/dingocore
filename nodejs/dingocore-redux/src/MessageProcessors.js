import { add_connection, update_connection_state } from "./actions/connections";
import { add_endpoint, update_hardware_revision, update_endpoint_name, update_endpoint_manufacturer, update_endpoint_model } from "./actions/endpoints";
import { add_service, update_service_name, update_service_type } from "./actions/services";
import { add_property, update_property_name, update_property_datatype, update_property_settable, update_property_value } from "./actions/properties";

import encoding from 'text-encoding';
const Decoder = new encoding.TextDecoder('UTF-8');
//const decode = Decoder.decode.bind( Decoder );
const decode = (message)=>{
    const result = Decoder.decode(message);
    console.log( "decode", message, result);
    return result;
}

class DingoProcessor {
    constructor(store) {
        this._store = store;
        this._connections = {};
    }

    process(topic, message) {
        let segments = topic.split('/');
        this.process_root(segments, message);
        this.process_connection(segments, message);
    }

    process_root(segments, message) {
        if ( segments.length > 0 ) {
            if ( segments[0] != 'dingo' ) {
                return
            }
            segments.shift();
        }
        this.process_connection(segments, message);
    }

    process_connection(segments, message) {
        if ( segments.length > 0 ) {
            const connection_id = segments.shift();
            let connection = this._connections[connection_id];
            if ( ! connection ) {
                connection = new ConnectionProcessor(this, connection_id);
                this._connections[connection_id] = connection;
                this.dispatch(add_connection(connection_id));
            }
            connection.process(segments, message);
        }
    }

    dispatch(action) {
        this._store.dispatch(action);
    }
}

class ConnectionProcessor {
    constructor(parent, id) {
        this._parent = parent;
        this._id = id;
        this._endpoints = {};
    }

    connection_id() {
        return this._id;
    }

    process(segments, message) {
        if ( segments.length == 0 ) {
            return;
        }
        this.process_internal(segments, message);
        this.process_endpoint(segments, message);
    }

    process_internal(segments, message) {
        if ( segments[0].startsWith('$')) {
            const prop = segments.shift();
            if (prop == '$state') {
                this.dispatch( update_connection_state( this.connection_id(), decode(message)));
            }
        }
    }

    process_endpoint(segments, message) {
        if ( segments.length == 0 ) {
            return;
        }

        const endpoint_id = segments.shift();
        let endpoint = this._endpoints[endpoint_id];
        if ( ! endpoint ) {
            endpoint = new EndpointProcessor(this, endpoint_id);
            this._endpoints[endpoint_id] = endpoint;
            this.dispatch(add_endpoint( this.connection_id(), endpoint_id ));
        }
        endpoint.process(segments, message);
    }

    dispatch(action) {
        this._parent.dispatch(action);
    }
}

class EndpointProcessor {
    constructor(parent, id) {
        this._parent = parent;
        this._id = id;
        this._services = {};
    }

    connection_id() {
        return this._parent.connection_id();
    }

    endpoint_id() {
        return this._id;
    }

    process(segments, message) {
        if ( segments.length == 0 ) {
            return;
        }

        this.process_internal(segments, message);
        this.process_service(segments, message);
    }

    process_internal(segments, message) {
        if ( segments.length == 0 ) {
            return;
        }
        if ( segments[0].startsWith('$')) {
            const prop = segments.shift();
            if ( prop == '$name' ) {
                this.dispatch(update_endpoint_name( this.connection_id(), this.endpoint_id(), decode(message)));
            } else if ( prop == '$model' ) {
                this.dispatch(update_endpoint_model( this.connection_id(), this.endpoint_id(), decode(message)));
            } else if ( prop == '$manufacturer' ) {
                this.dispatch(update_endpoint_manufacturer( this.connection_id(), this.endpoint_id(), decode(message)));
            } else if ( prop == '$hw' ) {
                if ( segments.length == 0 ) {
                    return;
                }
                const subprop = segments.shift();
                if ( subprop == 'revision' ) {
                    this.dispatch(update_hardware_revision( this.connection_id(), this.endpoint_id(), decode(message)));
                }
            }
        }
    }

    process_service(segments, message) {
        if ( segments.length == 0 ) {
            return;
        }

        const service_id = segments.shift();
        let service = this._services[service_id];
        if ( !service ) {
            service = new ServiceProcessor(this, service_id);
            this._services[service_id] = service;
            this.dispatch(add_service(this.connection_id(), this.endpoint_id(), service_id));
        }

        service.process(segments, message);

    }

    dispatch(action) {
        this._parent.dispatch(action);
    }
}

class ServiceProcessor {
    constructor(parent, id) {
        this._parent = parent;
        this._id = id;
        this._properties = {};
    }

    connection_id() {
        return this._parent.connection_id();
    }

    endpoint_id() {
        return this._parent.endpoint_id();
    }

    service_id() {
        return this._id;
    }

    process(segments, message) {
        if ( segments.length == 0 ) {
            return;
        }

        this.process_internal(segments, message);
        this.process_property(segments, message);
    }

    process_internal(segments, message) {
        if ( segments.length == 0 ) {
            return;
        }

        if ( segments[0].startsWith('$') ) {
            const prop = segments.shift();

            if ( prop == '$name' ) {
                this.dispatch(update_service_name(this.connection_id(), this.endpoint_id(), this.service_id(), decode(message)));
            } else if ( prop == '$type') {
                this.dispatch(update_service_type(this.connection_id(), this.endpoint_id(), this.service_id(), decode(message)));
            }
        }
    }

    process_property(segments, message) {
        if ( segments.length == 0 ) {
            return;
        }

        const property_id = segments.shift();
        let property = this._properties[property_id];
        if ( ! property ) {
            property = new PropertyProcessor(this, property_id);
            this._properties[property_id] = property;
            this.dispatch(add_property(this.connection_id(), this.endpoint_id(),  this.service_id(), property_id ));
        }

        property.process(segments, message);
    }

    dispatch(action) {
        this._parent.dispatch(action);
    }
}

class PropertyProcessor {
    constructor(parent, id) {
        this._parent = parent;
        this._id = id;
    }

    connection_id() {
        return this._parent.connection_id();
    }

    endpoint_id() {
        return this._parent.endpoint_id();
    }

    service_id() {
        return this._parent.service_id();
    }

    property_id() {
        return this._id;
    }

    process(segments, message) {
        if ( segments.length == 0 ) {
            this.dispatch( update_property_value(this.connection_id(), this.endpoint_id(), this.service_id(), this.property_id(), decode(message)));
            return;
        }

        this.process_internal(segments, message);
    }

    process_internal(segments, message) {
        if ( segments.length == 0 ) {
            return;
        }

        if (segments[0].startsWith('$')) {
            const name = segments.shift();
            if ( name == '$name' ) {
                this.dispatch( update_property_name(this.connection_id(), this.endpoint_id(), this.service_id(), this.property_id(), decode(message)));
            } else if ( name == '$datatype' ) {
                this.dispatch( update_property_datatype(this.connection_id(), this.endpoint_id(), this.service_id(), this.property_id(), decode(message)));
            } else if ( name == '$settable' ) {
                this.dispatch( update_property_settable(this.connection_id(), this.endpoint_id(), this.service_id(), this.property_id(), decode(message)));
            }
        }
    }

    dispatch(action) {
        this._parent.dispatch(action);
    }
}

export {
    DingoProcessor,
    ConnectionProcessor,
    EndpointProcessor,
}