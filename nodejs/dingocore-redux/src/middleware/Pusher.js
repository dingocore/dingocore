import actionTypes from '../constants/actionTypes';

export default store => next => action => {
    console.log('dispatching', action, "against", store)
    //let result = next(action)
    //console.log('next state', store.getState())
    //return result
    if ( action.type == actionTypes.PUSH_UPDATE_PROPERTY_VALUE ) {
        console.log( 'push property update', action.payload.value, store.getState().connection.client );
        return null;
    }
    return next(action);
  }