"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

const Decoder = new TextDecoder("UTF-8");

class Endpoint {
  constructor(id) {
    this.id = id;
    this.services = {};
  }

  process(topic, message) {
    console.log("Endpoint process", this.id, topic, Decoder.decode(message));
    if (topic.length == 1) {
      if (topic[0] == '$name') {
        this.name = Decoder.decode(message);
      } else if (topic[0] == '$manufacturer') {
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
exports.default = Endpoint;
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uL3NyYy9FbmRwb2ludC5qcyJdLCJuYW1lcyI6WyJEZWNvZGVyIiwiVGV4dERlY29kZXIiLCJFbmRwb2ludCIsImNvbnN0cnVjdG9yIiwiaWQiLCJzZXJ2aWNlcyIsInByb2Nlc3MiLCJ0b3BpYyIsIm1lc3NhZ2UiLCJjb25zb2xlIiwibG9nIiwiZGVjb2RlIiwibGVuZ3RoIiwibmFtZSIsIm1hbnVmYWN0dXJlciJdLCJtYXBwaW5ncyI6Ijs7Ozs7O0FBQ0EsTUFBTUEsVUFBVSxJQUFJQyxXQUFKLENBQWdCLE9BQWhCLENBQWhCOztBQUVlLE1BQU1DLFFBQU4sQ0FBZTtBQUM1QkMsY0FBWUMsRUFBWixFQUFnQjtBQUNkLFNBQUtBLEVBQUwsR0FBVUEsRUFBVjtBQUNBLFNBQUtDLFFBQUwsR0FBZ0IsRUFBaEI7QUFDRDs7QUFFREMsVUFBUUMsS0FBUixFQUFlQyxPQUFmLEVBQXdCO0FBQ3RCQyxZQUFRQyxHQUFSLENBQWEsa0JBQWIsRUFBaUMsS0FBS04sRUFBdEMsRUFBMkNHLEtBQTNDLEVBQWtEUCxRQUFRVyxNQUFSLENBQWVILE9BQWYsQ0FBbEQ7QUFDQSxRQUFLRCxNQUFNSyxNQUFOLElBQWdCLENBQXJCLEVBQXlCO0FBQ3ZCLFVBQUtMLE1BQU0sQ0FBTixLQUFZLE9BQWpCLEVBQTJCO0FBQ3pCLGFBQUtNLElBQUwsR0FBWWIsUUFBUVcsTUFBUixDQUFlSCxPQUFmLENBQVo7QUFDRCxPQUZELE1BRU8sSUFBS0QsTUFBTSxDQUFOLEtBQVksZUFBakIsRUFBbUM7QUFDeEMsYUFBS08sWUFBTCxHQUFvQmQsUUFBUVcsTUFBUixDQUFlSCxPQUFmLENBQXBCO0FBQ0Q7QUFDRjtBQUNMOzs7Ozs7Ozs7QUFTRztBQXhCMkI7a0JBQVROLFEiLCJmaWxlIjoiRW5kcG9pbnQuanMiLCJzb3VyY2VzQ29udGVudCI6WyJcbmNvbnN0IERlY29kZXIgPSBuZXcgVGV4dERlY29kZXIoXCJVVEYtOFwiKTtcblxuZXhwb3J0IGRlZmF1bHQgY2xhc3MgRW5kcG9pbnQge1xuICBjb25zdHJ1Y3RvcihpZCkge1xuICAgIHRoaXMuaWQgPSBpZDtcbiAgICB0aGlzLnNlcnZpY2VzID0ge307XG4gIH1cblxuICBwcm9jZXNzKHRvcGljLCBtZXNzYWdlKSB7XG4gICAgY29uc29sZS5sb2coIFwiRW5kcG9pbnQgcHJvY2Vzc1wiLCB0aGlzLmlkLCAgdG9waWMsIERlY29kZXIuZGVjb2RlKG1lc3NhZ2UpICk7XG4gICAgaWYgKCB0b3BpYy5sZW5ndGggPT0gMSApIHtcbiAgICAgIGlmICggdG9waWNbMF0gPT0gJyRuYW1lJyApIHtcbiAgICAgICAgdGhpcy5uYW1lID0gRGVjb2Rlci5kZWNvZGUobWVzc2FnZSk7XG4gICAgICB9IGVsc2UgaWYgKCB0b3BpY1swXSA9PSAnJG1hbnVmYWN0dXJlcicgKSB7XG4gICAgICAgIHRoaXMubWFudWZhY3R1cmVyID0gRGVjb2Rlci5kZWNvZGUobWVzc2FnZSk7XG4gICAgICB9XG4gICAgfVxuLypcbiAgICBpZiAoIHRvcGljLmxlbmd0aCA+PSAyICkge1xuICAgICAgdmFyIGVuZHBvaW50ID0gdGhpcy5lbmRwb2ludHNbdG9waWNbMV1dO1xuICAgICAgaWYgKCAhIGVuZHBvaW50ICkge1xuICAgICAgICBlbmRwb2ludCA9IG5ldyBFbmRwb2ludCh0b3BpY1sxXSk7XG4gICAgICB9XG4gICAgICBcbiAgICB9XG4qL1xuICB9XG59XG4iXX0=