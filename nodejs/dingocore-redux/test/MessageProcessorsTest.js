
import { DingoProcessor } from '../dist/MessageProcessors';

import configureStore from 'redux-mock-store' //ES6 modules
import { add_connection } from '../src/actions/connections';
import { add_endpoint } from '../dist/actions/endpoints';
import { add_service } from '../src/actions/services';
import { add_property, update_property_value } from '../dist/actions/properties';
const mockStore = configureStore([])


var assert = require('assert');

describe('MessageProcessors', function() {
  describe('processing messages', function() {
    it('deep state should be created from single messages', function() {
      const store = mockStore({});
      const processor = new DingoProcessor(store);
      processor.process( "dingo/conn1/ep1/svc1/on", Buffer.from("true", 'UTF-8') );
      const actions = store.getActions();
      //assert( actions.length == 2 );
      //console.log( actions );
      assert.deepStrictEqual( actions, [
          add_connection('conn1'),
          add_endpoint('conn1', 'ep1'),
          add_service('conn1', 'ep1', 'svc1' ),
          add_property('conn1', 'ep1', 'svc1', 'on'),
          update_property_value('conn1', 'ep1', 'svc1', 'on', 'true' ),
      ]);
    });
  });
});