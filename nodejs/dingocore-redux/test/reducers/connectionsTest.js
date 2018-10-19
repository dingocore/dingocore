var assert = require('assert');

import reducer from '../../dist/reducers/connections'
import { update_connection_state, add_connection } from '../../dist/actions/connections';

describe('connection reducer', function() {
  describe('default state', function() {
    it('should produce default state', function() {
        const state = reducer(undefined, { type: 'init' } );
        assert.deepEqual( state, [] );
    });
    it('should add a connection', function() {
        const state = reducer(undefined, add_connection("conn1"));
        assert.deepEqual( state, [ { id: 'conn1' } ]);
    })
    it('should update connection state', function() {
        const state1 = reducer(undefined, add_connection("conn1"));
        assert.deepEqual( state1, [ { id: 'conn1' } ]);
        const state2 = reducer(state1, update_connection_state("conn1", "ready"));
        assert.deepEqual( state2, [ { id: 'conn1', state: 'ready' } ]);
        assert.notStrictEqual(state1, state2);
    })
    it('should add a connection state if state is the first item seen', function() {
        const state = reducer(undefined, update_connection_state("conn1", "init"));
        assert.deepEqual( state, [ { id: 'conn1', state: 'init' }] );
    })
  });
});