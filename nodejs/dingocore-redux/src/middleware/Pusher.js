import actionTypes from '../constants/actionTypes';

export default store => next => action => {
    //console.log('dispatching', action, "against", store)
    //let result = next(action)
    //console.log('next state', store.getState())
    //return result
    if ( action.type == actionTypes.PUSH_UPDATE_PROPERTY_VALUE ) {
        console.log( 'push property update', action.payload, store.getState().connection.client );
        const client = store.getState().connection.client;
        client.pushPropertyValue(action.payload.property, action.payload.value);
        return null;
    }
    return next(action);
  }