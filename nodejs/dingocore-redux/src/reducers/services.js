import actionTypes from '../constants/actionTypes';

export default (state=[], action)=>{
    switch ( action.type ) {
        case actionTypes.ADD_SERVICE: {
            console.log( "adding connection", action );
            return state.concat( { 
                connection_id: action.payload.connection_id,
                endpoint_id: action.payload.endpoint_id,
                id: action.payload.id 
            } );
        }
        case actionTypes.UPDATE_SERVICE_NAME: {
            let nextState = state.map( e=>{
                if ( e.id == action.payload.id ) {
                    return Object.assign( {}, e, { name: action.payload.name } );
                } else {
                    return e;
                }
            });
            return nextState;
        }
        case actionTypes.UPDATE_SERVICE_TYPE: {
            let nextState = state.map( e=>{
                if ( e.id == action.payload.id ) {
                    return Object.assign( {}, e, { type: action.payload.type } );
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