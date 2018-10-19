import actionTypes from '../constants/actionTypes';

export default (state=[], action)=>{
    switch ( action.type ) {
        case actionTypes.ADD_PROPERTY: {
            return state.concat( { 
                connection_id: action.payload.connection_id,
                endpoint_id: action.payload.endpoint_id,
                service_id: action.payload.service_id,
                id: action.payload.id 
            } );
        }
        case actionTypes.UPDATE_PROPERTY_NAME: {
            let nextState = state.map( e=>{
                if ( e.id == action.payload.id ) {
                    return Object.assign( {}, e, { name: action.payload.name } );
                } else {
                    return e;
                }
            });
            return nextState;
        }
        case actionTypes.UPDATE_PROPERTY_VALUE: {
            let nextState = state.map( e=>{
                if ( e.id == action.payload.id ) {
                    return Object.assign( {}, e, { value: action.payload.value } );
                } else {
                    return e;
                }
            });
            return nextState;
        }
        default: {
            return state;
        }
    }

}