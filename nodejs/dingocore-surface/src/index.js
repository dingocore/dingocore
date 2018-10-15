
import MQTT from 'mqtt';
import Connection from './Connection.js';

const decode = new TextDecoder("UTF-8").decode;

const client = MQTT.connect("ws://localhost:9001/");

const connections = {};

client.on('message', (topic, message)=>{
  const segments = topic.split('/');
  console.log( "RECV", segments, message);
  if ( segments.length >= 3) {
    var connection = connections[ segments[1] ];
    if ( ! connection ) {
      connection = new Connection( segments[1] );
      connections[segments[1]] = connection;
      console.log( "create conn", connection);
    }
    connection.process( segments.slice(2), message );
  }
/*
  if ( segments.length > 1 ) {
    if ( segments[0] == 'dingo' ) {
      if ( segments.length == 3 ) {
        console.log( "connection", segments[1] );
      } else if ( segments.length == 4 ) {
        if ( segments[3] == '$name' ) {
          console.log( "endpoint", new TextDecoder("UTF-8").decode(message));
        }
      } else if ( segments.length == 5 ) {
        if ( segments[4] == '$name' ) {
          console.log( "service", new TextDecoder("UTF-8").decode(message));
        }
      }
    }
  }
*/
  console.log( connections );
})

client.on('connect', ()=>{
  console.log( "connected");
  client.subscribe('#', (err)=>{
  });
});

export default client;
