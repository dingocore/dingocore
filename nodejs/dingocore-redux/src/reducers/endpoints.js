import actionTypes from '../constants/actionTypes';

export default (state=[], action)=>{
    switch ( action.type ) {
        case actionTypes.ADD_ENDPOINT: {
            return state.concat( { connection_id: action.payload.connection_id, id: action.payload.id } );
        }
        case actionTypes.UPDATE_ENDPOINT_NAME: {
            let nextState = state.map( e=>{
                if ( e.id == action.payload.id ) {
                    return Object.assign( {}, e, { name: action.payload.name } );
                } else {
                    return e;
                }
            });

            return nextState;
        }
        case actionTypes.UPDATE_ENDPOINT_MODEL: {
            let nextState = state.map( e=>{
                if ( e.id == action.payload.id ) {
                    return Object.assign( {}, e, { model: action.payload.name } );
                } else {
                    return e;
                }
            });

            return nextState;
        }
        case actionTypes.UPDATE_ENDPOINT_MANUFACTURER: {
            let nextState = state.map( e=>{
                if ( e.id == action.payload.id ) {
                    return Object.assign( {}, e, { manufacturer: action.payload.manufacturer } );
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