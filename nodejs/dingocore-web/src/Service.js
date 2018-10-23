
import React, {Component} from 'react';
import Switch from './services/Switch';
import Properties from './Properties';
import { connect } from 'react-redux'; 

class Service extends Component {
    constructor(props) {
        super(props);
    }

    service() {
        if ( this.props.service.type == 'switch' ) {
            return Switch;
        }
        return null;
    }

    render() {
        const SpecificService = this.service();
        console.log( "using", SpecificService);

        if ( SpecificService == null ) {
            return null;
        }

        return ( 
            <div>
                <SpecificService service={this.props.service}/>
                <Properties properties={this.props.properties}/>
            </div>
        )
        //if ( this.props.service.type === 'switch' ) {
            //return (
                //<Switch service={this.props.service} properties={this.props.properties}></Switch>
            //)
        //} else {
            //return null;
        //}
    }
}

export default connect(
    (state, props)=>{
        console.log( "mapping for ", props, state);
        return {
            properties: state.automation.properties.filter(e=>e.service_id==props.service.id)
        }
    }
)(Service);

//export default Service;
