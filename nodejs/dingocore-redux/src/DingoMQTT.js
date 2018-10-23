import MQTT from 'mqtt';
import { DingoProcessor } from '../dist/MessageProcessors';

const settableTopic = (property)=>{
    return `dingo/${property.connection_id}/${property.endpoint_id}/${property.service_id}/${property.id}/set`;
}

class DingoMQTT {

    constructor(url, store) {
        this._url = url;
        this._store = store;
    }

    connect(callback) {
        this._callback = callback;
        this._processor = new DingoProcessor(this._store);
        this.client = MQTT.connect(this._url);
        this.client.on( 'connect', this._on_connect.bind(this));
        this.client.on( 'message', this._on_message.bind(this));
    }

    pushPropertyValue(property, value) {
        console.log( "publish to " + settableTopic(property), value);
        this.client.publish(settableTopic(property), value);
    }

    _on_connect() {
        this._callback();
        this.client.subscribe('#', (err)=>{
            if ( err ) { 
              console.log("error subscribing", err);
            }
        });
    }

    _on_message(topic, message) {
        console.log( "received:", topic, message);
        this._processor.process(topic, message);
    }

    _to_action(topic, message) {
        const segments = topic.split("/");
        if ( segments.length >= 1 ) {
            if ( segments[0] != 'dingo' ) {
                console.log("not a dingo message");
                return;
            }
        }
    }
}

export default DingoMQTT;

/*
message on dingo/16:BD:8D:FE:CC:B6/$state // lost
message on dingo/16:BD:8D:FE:CC:B6/1/$name // TEST
message on dingo/16:BD:8D:FE:CC:B6/1/$manufacturer // iDevices LLC
message on dingo/16:BD:8D:FE:CC:B6/1/$model // IDEV0001
message on dingo/16:BD:8D:FE:CC:B6/1/$hw/revision // 1
message on dingo/16:BD:8D:FE:CC:B6/1/10/$name // Switch
message on dingo/16:BD:8D:FE:CC:B6/1/10/$type // switch
message on dingo/16:BD:8D:FE:CC:B6/1/10/On // false
message on dingo/16:BD:8D:FE:CC:B6/1/10/On/$name // On
message on dingo/16:BD:8D:FE:CC:B6/1/10/On/$datatype // boolean
message on dingo/16:BD:8D:FE:CC:B6/1/10/On/$settable // true
message on dingo/16:BD:8D:FE:CC:B6/1/20/$name // Night Light
message on dingo/16:BD:8D:FE:CC:B6/1/20/$type // switch
message on dingo/16:BD:8D:FE:CC:B6/1/20/On // true
message on dingo/16:BD:8D:FE:CC:B6/1/20/On/$name // On
message on dingo/16:BD:8D:FE:CC:B6/1/20/On/$datatype // boolean
message on dingo/16:BD:8D:FE:CC:B6/1/20/On/$settable // true
message on dingo/16:BD:8D:FE:CC:B6/1/20/Hue // 4.0
message on dingo/16:BD:8D:FE:CC:B6/1/20/Hue/$name // Hue
message on dingo/16:BD:8D:FE:CC:B6/1/20/Hue/$datatype // float
message on dingo/16:BD:8D:FE:CC:B6/1/20/Hue/$settable // true
message on dingo/16:BD:8D:FE:CC:B6/1/20/Saturation // 100.0
message on dingo/16:BD:8D:FE:CC:B6/1/20/Saturation/$name // Saturation
message on dingo/16:BD:8D:FE:CC:B6/1/20/Saturation/$datatype // float
message on dingo/16:BD:8D:FE:CC:B6/1/20/Saturation/$settable // true
*/