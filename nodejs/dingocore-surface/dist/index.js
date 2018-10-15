'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _mqtt = require('mqtt');

var _mqtt2 = _interopRequireDefault(_mqtt);

var _Connection = require('./Connection.js');

var _Connection2 = _interopRequireDefault(_Connection);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

const decode = new TextDecoder("UTF-8").decode;

const client = _mqtt2.default.connect("ws://localhost:9001/");

const connections = {};

client.on('message', (topic, message) => {
  const segments = topic.split('/');
  console.log("RECV", segments, message);
  if (segments.length >= 3) {
    var connection = connections[segments[1]];
    if (!connection) {
      connection = new _Connection2.default(segments[1]);
      connections[segments[1]] = connection;
      console.log("create conn", connection);
    }
    connection.process(segments.slice(2), message);
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
  console.log(connections);
});

client.on('connect', () => {
  console.log("connected");
  client.subscribe('#', err => {});
});

exports.default = client;
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uL3NyYy9pbmRleC5qcyJdLCJuYW1lcyI6WyJkZWNvZGUiLCJUZXh0RGVjb2RlciIsImNsaWVudCIsIk1RVFQiLCJjb25uZWN0IiwiY29ubmVjdGlvbnMiLCJvbiIsInRvcGljIiwibWVzc2FnZSIsInNlZ21lbnRzIiwic3BsaXQiLCJjb25zb2xlIiwibG9nIiwibGVuZ3RoIiwiY29ubmVjdGlvbiIsIkNvbm5lY3Rpb24iLCJwcm9jZXNzIiwic2xpY2UiLCJzdWJzY3JpYmUiLCJlcnIiXSwibWFwcGluZ3MiOiI7Ozs7OztBQUNBOzs7O0FBQ0E7Ozs7OztBQUVBLE1BQU1BLFNBQVMsSUFBSUMsV0FBSixDQUFnQixPQUFoQixFQUF5QkQsTUFBeEM7O0FBRUEsTUFBTUUsU0FBU0MsZUFBS0MsT0FBTCxDQUFhLHNCQUFiLENBQWY7O0FBRUEsTUFBTUMsY0FBYyxFQUFwQjs7QUFFQUgsT0FBT0ksRUFBUCxDQUFVLFNBQVYsRUFBcUIsQ0FBQ0MsS0FBRCxFQUFRQyxPQUFSLEtBQWtCO0FBQ3JDLFFBQU1DLFdBQVdGLE1BQU1HLEtBQU4sQ0FBWSxHQUFaLENBQWpCO0FBQ0FDLFVBQVFDLEdBQVIsQ0FBYSxNQUFiLEVBQXFCSCxRQUFyQixFQUErQkQsT0FBL0I7QUFDQSxNQUFLQyxTQUFTSSxNQUFULElBQW1CLENBQXhCLEVBQTJCO0FBQ3pCLFFBQUlDLGFBQWFULFlBQWFJLFNBQVMsQ0FBVCxDQUFiLENBQWpCO0FBQ0EsUUFBSyxDQUFFSyxVQUFQLEVBQW9CO0FBQ2xCQSxtQkFBYSxJQUFJQyxvQkFBSixDQUFnQk4sU0FBUyxDQUFULENBQWhCLENBQWI7QUFDQUosa0JBQVlJLFNBQVMsQ0FBVCxDQUFaLElBQTJCSyxVQUEzQjtBQUNBSCxjQUFRQyxHQUFSLENBQWEsYUFBYixFQUE0QkUsVUFBNUI7QUFDRDtBQUNEQSxlQUFXRSxPQUFYLENBQW9CUCxTQUFTUSxLQUFULENBQWUsQ0FBZixDQUFwQixFQUF1Q1QsT0FBdkM7QUFDRDtBQUNIOzs7Ozs7Ozs7Ozs7Ozs7OztBQWlCRUcsVUFBUUMsR0FBUixDQUFhUCxXQUFiO0FBQ0QsQ0E5QkQ7O0FBZ0NBSCxPQUFPSSxFQUFQLENBQVUsU0FBVixFQUFxQixNQUFJO0FBQ3ZCSyxVQUFRQyxHQUFSLENBQWEsV0FBYjtBQUNBVixTQUFPZ0IsU0FBUCxDQUFpQixHQUFqQixFQUF1QkMsR0FBRCxJQUFPLENBQzVCLENBREQ7QUFFRCxDQUpEOztrQkFNZWpCLE0iLCJmaWxlIjoiaW5kZXguanMiLCJzb3VyY2VzQ29udGVudCI6WyJcbmltcG9ydCBNUVRUIGZyb20gJ21xdHQnO1xuaW1wb3J0IENvbm5lY3Rpb24gZnJvbSAnLi9Db25uZWN0aW9uLmpzJztcblxuY29uc3QgZGVjb2RlID0gbmV3IFRleHREZWNvZGVyKFwiVVRGLThcIikuZGVjb2RlO1xuXG5jb25zdCBjbGllbnQgPSBNUVRULmNvbm5lY3QoXCJ3czovL2xvY2FsaG9zdDo5MDAxL1wiKTtcblxuY29uc3QgY29ubmVjdGlvbnMgPSB7fTtcblxuY2xpZW50Lm9uKCdtZXNzYWdlJywgKHRvcGljLCBtZXNzYWdlKT0+e1xuICBjb25zdCBzZWdtZW50cyA9IHRvcGljLnNwbGl0KCcvJyk7XG4gIGNvbnNvbGUubG9nKCBcIlJFQ1ZcIiwgc2VnbWVudHMsIG1lc3NhZ2UpO1xuICBpZiAoIHNlZ21lbnRzLmxlbmd0aCA+PSAzKSB7XG4gICAgdmFyIGNvbm5lY3Rpb24gPSBjb25uZWN0aW9uc1sgc2VnbWVudHNbMV0gXTtcbiAgICBpZiAoICEgY29ubmVjdGlvbiApIHtcbiAgICAgIGNvbm5lY3Rpb24gPSBuZXcgQ29ubmVjdGlvbiggc2VnbWVudHNbMV0gKTtcbiAgICAgIGNvbm5lY3Rpb25zW3NlZ21lbnRzWzFdXSA9IGNvbm5lY3Rpb247XG4gICAgICBjb25zb2xlLmxvZyggXCJjcmVhdGUgY29ublwiLCBjb25uZWN0aW9uKTtcbiAgICB9XG4gICAgY29ubmVjdGlvbi5wcm9jZXNzKCBzZWdtZW50cy5zbGljZSgyKSwgbWVzc2FnZSApO1xuICB9XG4vKlxuICBpZiAoIHNlZ21lbnRzLmxlbmd0aCA+IDEgKSB7XG4gICAgaWYgKCBzZWdtZW50c1swXSA9PSAnZGluZ28nICkge1xuICAgICAgaWYgKCBzZWdtZW50cy5sZW5ndGggPT0gMyApIHtcbiAgICAgICAgY29uc29sZS5sb2coIFwiY29ubmVjdGlvblwiLCBzZWdtZW50c1sxXSApO1xuICAgICAgfSBlbHNlIGlmICggc2VnbWVudHMubGVuZ3RoID09IDQgKSB7XG4gICAgICAgIGlmICggc2VnbWVudHNbM10gPT0gJyRuYW1lJyApIHtcbiAgICAgICAgICBjb25zb2xlLmxvZyggXCJlbmRwb2ludFwiLCBuZXcgVGV4dERlY29kZXIoXCJVVEYtOFwiKS5kZWNvZGUobWVzc2FnZSkpO1xuICAgICAgICB9XG4gICAgICB9IGVsc2UgaWYgKCBzZWdtZW50cy5sZW5ndGggPT0gNSApIHtcbiAgICAgICAgaWYgKCBzZWdtZW50c1s0XSA9PSAnJG5hbWUnICkge1xuICAgICAgICAgIGNvbnNvbGUubG9nKCBcInNlcnZpY2VcIiwgbmV3IFRleHREZWNvZGVyKFwiVVRGLThcIikuZGVjb2RlKG1lc3NhZ2UpKTtcbiAgICAgICAgfVxuICAgICAgfVxuICAgIH1cbiAgfVxuKi9cbiAgY29uc29sZS5sb2coIGNvbm5lY3Rpb25zICk7XG59KVxuXG5jbGllbnQub24oJ2Nvbm5lY3QnLCAoKT0+e1xuICBjb25zb2xlLmxvZyggXCJjb25uZWN0ZWRcIik7XG4gIGNsaWVudC5zdWJzY3JpYmUoJyMnLCAoZXJyKT0+e1xuICB9KTtcbn0pO1xuXG5leHBvcnQgZGVmYXVsdCBjbGllbnQ7XG4iXX0=