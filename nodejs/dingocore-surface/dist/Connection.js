"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _Endpoint = require("./Endpoint.js");

var _Endpoint2 = _interopRequireDefault(_Endpoint);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

const Decoder = new TextDecoder("UTF-8");

class Connection {
  constructor(id) {
    this.id = id;
    this.endpoints = {};
  }

  process(topic, message) {
    console.log("connection process", this.id, topic, Decoder.decode(message));
    if (topic.length == 1) {
      if (topic[0] == '$state') {
        this.state = Decoder.decode(message);
      }
    }
    if (topic.length >= 2) {
      if (!topic[0].startsWith('$')) {
        var endpoint = this.endpoints[topic[0]];
        if (!endpoint) {
          console.log("CREATE ENDPOINT", topic[0]);
          endpoint = new _Endpoint2.default(topic[0]);
          this.endpoints[topic[0]] = endpoint;
        }
        endpoint.process(topic.slice(1), message);
      }
    }
  }
}
exports.default = Connection;
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uL3NyYy9Db25uZWN0aW9uLmpzIl0sIm5hbWVzIjpbIkRlY29kZXIiLCJUZXh0RGVjb2RlciIsIkNvbm5lY3Rpb24iLCJjb25zdHJ1Y3RvciIsImlkIiwiZW5kcG9pbnRzIiwicHJvY2VzcyIsInRvcGljIiwibWVzc2FnZSIsImNvbnNvbGUiLCJsb2ciLCJkZWNvZGUiLCJsZW5ndGgiLCJzdGF0ZSIsInN0YXJ0c1dpdGgiLCJlbmRwb2ludCIsIkVuZHBvaW50Iiwic2xpY2UiXSwibWFwcGluZ3MiOiI7Ozs7OztBQUNBOzs7Ozs7QUFDQSxNQUFNQSxVQUFVLElBQUlDLFdBQUosQ0FBZ0IsT0FBaEIsQ0FBaEI7O0FBRWUsTUFBTUMsVUFBTixDQUFpQjtBQUM5QkMsY0FBWUMsRUFBWixFQUFnQjtBQUNkLFNBQUtBLEVBQUwsR0FBVUEsRUFBVjtBQUNBLFNBQUtDLFNBQUwsR0FBaUIsRUFBakI7QUFDRDs7QUFFREMsVUFBUUMsS0FBUixFQUFlQyxPQUFmLEVBQXdCO0FBQ3RCQyxZQUFRQyxHQUFSLENBQWEsb0JBQWIsRUFBbUMsS0FBS04sRUFBeEMsRUFBNkNHLEtBQTdDLEVBQW9EUCxRQUFRVyxNQUFSLENBQWVILE9BQWYsQ0FBcEQ7QUFDQSxRQUFLRCxNQUFNSyxNQUFOLElBQWdCLENBQXJCLEVBQXlCO0FBQ3ZCLFVBQUtMLE1BQU0sQ0FBTixLQUFZLFFBQWpCLEVBQTRCO0FBQzFCLGFBQUtNLEtBQUwsR0FBYWIsUUFBUVcsTUFBUixDQUFlSCxPQUFmLENBQWI7QUFDRDtBQUNGO0FBQ0QsUUFBS0QsTUFBTUssTUFBTixJQUFnQixDQUFyQixFQUF5QjtBQUN2QixVQUFLLENBQUVMLE1BQU0sQ0FBTixFQUFTTyxVQUFULENBQW9CLEdBQXBCLENBQVAsRUFBa0M7QUFDaEMsWUFBSUMsV0FBVyxLQUFLVixTQUFMLENBQWVFLE1BQU0sQ0FBTixDQUFmLENBQWY7QUFDQSxZQUFLLENBQUVRLFFBQVAsRUFBa0I7QUFDaEJOLGtCQUFRQyxHQUFSLENBQWEsaUJBQWIsRUFBZ0NILE1BQU0sQ0FBTixDQUFoQztBQUNBUSxxQkFBVyxJQUFJQyxrQkFBSixDQUFhVCxNQUFNLENBQU4sQ0FBYixDQUFYO0FBQ0EsZUFBS0YsU0FBTCxDQUFlRSxNQUFNLENBQU4sQ0FBZixJQUEyQlEsUUFBM0I7QUFDRDtBQUNEQSxpQkFBU1QsT0FBVCxDQUFpQkMsTUFBTVUsS0FBTixDQUFZLENBQVosQ0FBakIsRUFBaUNULE9BQWpDO0FBQ0Q7QUFDRjtBQUNGO0FBeEI2QjtrQkFBWE4sVSIsImZpbGUiOiJDb25uZWN0aW9uLmpzIiwic291cmNlc0NvbnRlbnQiOlsiXG5pbXBvcnQgRW5kcG9pbnQgZnJvbSAnLi9FbmRwb2ludC5qcyc7XG5jb25zdCBEZWNvZGVyID0gbmV3IFRleHREZWNvZGVyKFwiVVRGLThcIik7XG5cbmV4cG9ydCBkZWZhdWx0IGNsYXNzIENvbm5lY3Rpb24ge1xuICBjb25zdHJ1Y3RvcihpZCkge1xuICAgIHRoaXMuaWQgPSBpZDtcbiAgICB0aGlzLmVuZHBvaW50cyA9IHt9O1xuICB9XG5cbiAgcHJvY2Vzcyh0b3BpYywgbWVzc2FnZSkge1xuICAgIGNvbnNvbGUubG9nKCBcImNvbm5lY3Rpb24gcHJvY2Vzc1wiLCB0aGlzLmlkLCAgdG9waWMsIERlY29kZXIuZGVjb2RlKG1lc3NhZ2UpICk7XG4gICAgaWYgKCB0b3BpYy5sZW5ndGggPT0gMSApIHtcbiAgICAgIGlmICggdG9waWNbMF0gPT0gJyRzdGF0ZScgKSB7XG4gICAgICAgIHRoaXMuc3RhdGUgPSBEZWNvZGVyLmRlY29kZShtZXNzYWdlKTtcbiAgICAgIH1cbiAgICB9IFxuICAgIGlmICggdG9waWMubGVuZ3RoID49IDIgKSB7XG4gICAgICBpZiAoICEgdG9waWNbMF0uc3RhcnRzV2l0aCgnJCcpICkge1xuICAgICAgICB2YXIgZW5kcG9pbnQgPSB0aGlzLmVuZHBvaW50c1t0b3BpY1swXV07XG4gICAgICAgIGlmICggISBlbmRwb2ludCApIHtcbiAgICAgICAgICBjb25zb2xlLmxvZyggXCJDUkVBVEUgRU5EUE9JTlRcIiwgdG9waWNbMF0pO1xuICAgICAgICAgIGVuZHBvaW50ID0gbmV3IEVuZHBvaW50KHRvcGljWzBdKTtcbiAgICAgICAgICB0aGlzLmVuZHBvaW50c1t0b3BpY1swXV0gPSBlbmRwb2ludDtcbiAgICAgICAgfVxuICAgICAgICBlbmRwb2ludC5wcm9jZXNzKHRvcGljLnNsaWNlKDEpLCBtZXNzYWdlKTtcbiAgICAgIH1cbiAgICB9XG4gIH1cbn1cbiJdfQ==