import actionTypes from '../constants/actionTypes';

export default (state=[], action)=>{
    switch ( action.type ) {
        case actionTypes.ADD_CONNECTION: {
            return state.concat( { id: action.payload.id } );
        }
        case actionTypes.UPDATE_CONNECTION_STATE: {
            let updated = false;
            let nextState = state.map( e=>{
                if ( e.id == action.payload.id ) {
                    updated = true;
                    return Object.assign( {}, e, { state: action.payload.state } );
                } else {
                    return e;
                }
            });

            if ( ! updated ) {
                nextState.push( {
                    id: action.payload.id,
                    state: action.payload.state,
                });
            }

            return nextState;
        }
        default: {
            return state;
        }
    }

}