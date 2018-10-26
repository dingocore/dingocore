import { compose, createStore, combineReducers, applyMiddleware } from 'redux'

import connectionsReducer from 'dingocore-redux/dist/reducers/connections';
import endpointsReducer from 'dingocore-redux/dist/reducers/endpoints';
import servicesReducer from 'dingocore-redux/dist/reducers/services';
import propertiesReducer from 'dingocore-redux/dist/reducers/properties';
import Pusher from 'dingocore-redux/dist/middleware/Pusher';


const rootReducer = combineReducers({
    automation: combineReducers({
      connections: connectionsReducer,
      endpoints: endpointsReducer,
      services: servicesReducer,
      properties: propertiesReducer,
    }),
    connection: (state = {}, action) => {
      if (action.type == 'CONNECTED') {
        console.log("--------------> ", action, action.payload.client);
        const newState = Object.assign({}, state, { client: action.payload.client });
        console.log("new state", newState);
        return newState;
      }
      return state;
    }
  })
  
  
  const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
  
  
  const store = createStore(
    rootReducer,
    composeEnhancers(
      applyMiddleware(Pusher)
    )
  )

  export default store;