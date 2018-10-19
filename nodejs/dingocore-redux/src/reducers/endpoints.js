import actionTypes from '../constants/actionTypes';

export default (state=[], action)=>{
    switch ( action.type ) {
        case actionTypes.ADD_ENDPOINT: {
            return state.concat( { id: action.payload.id } );
        }
        case actionTypes.UPDATE_ENDPOINT_NAME: {
            let updated = false;
            let nextState = state.map( e=>{
                if ( e.id == action.payload.id ) {
                    updated = true;
                    return Object.assign( {}, e, { name: action.payload.name } );
                } else {
                    return e;
                }
            });

            if ( ! updated ) {
                nextState.push( {
                    id: action.payload.id,
                    name: action.payload.name,
                });
            }

            return nextState;
        }
        case actionTypes.UPDATE_ENDPOINT_MODEL: {
            let updated = false;
            let nextState = state.map( e=>{
                if ( e.id == action.payload.id ) {
                    updated = true;
                    return Object.assign( {}, e, { model: action.payload.name } );
                } else {
                    return e;
                }
            });

            if ( ! updated ) {
                nextState.push( {
                    id: action.payload.id,
                    model: action.payload.name,
                });
            }

            return nextState;
        }
        case actionTypes.UPDATE_ENDPOINT_MANUFACTURER: {
            let updated = false;
            let nextState = state.map( e=>{
                if ( e.id == action.payload.id ) {
                    updated = true;
                    return Object.assign( {}, e, { manufacturer: action.payload.manufacturer } );
                } else {
                    return e;
                }
            });

            if ( ! updated ) {
                nextState.push( {
                    id: action.payload.id,
                    manufacturer: action.payload.manufacturer,
                });
            }

            return nextState;
        }
        default: {
            return state;
        }
    }

}