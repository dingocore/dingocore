
const Decoder = new TextDecoder("UTF-8");

export default class Endpoint {
  constructor(id) {
    this.id = id;
    this.services = {};
  }

  process(topic, message) {
    console.log( "Endpoint process", this.id,  topic, Decoder.decode(message) );
    if ( topic.length == 1 ) {
      if ( topic[0] == '$name' ) {
        this.name = Decoder.decode(message);
      } else if ( topic[0] == '$manufacturer' ) {
        this.manufacturer = Decoder.decode(message);
      }
    }
/*
    if ( topic.length >= 2 ) {
      var endpoint = this.endpoints[topic[1]];
      if ( ! endpoint ) {
        endpoint = new Endpoint(topic[1]);
      }
      
    }
*/
  }
}
