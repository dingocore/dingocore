
import Endpoint from './Endpoint.js';
const Decoder = new TextDecoder("UTF-8");

export default class Connection {
  constructor(id) {
    this.id = id;
    this.endpoints = {};
  }

  process(topic, message) {
    console.log( "connection process", this.id,  topic, Decoder.decode(message) );
    if ( topic.length == 1 ) {
      if ( topic[0] == '$state' ) {
        this.state = Decoder.decode(message);
      }
    } 
    if ( topic.length >= 2 ) {
      if ( ! topic[0].startsWith('$') ) {
        var endpoint = this.endpoints[topic[0]];
        if ( ! endpoint ) {
          console.log( "CREATE ENDPOINT", topic[0]);
          endpoint = new Endpoint(topic[0]);
          this.endpoints[topic[0]] = endpoint;
        }
        endpoint.process(topic.slice(1), message);
      }
    }
  }
}
