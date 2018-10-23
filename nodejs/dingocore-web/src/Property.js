
import React, {Component} from 'react';
import Hue from './properties/Hue';

class Property extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        if ( this.props.property.id === 'Hue' ) {
            return (
                <Hue property={this.props.property}></Hue>
            )
        } else {
            return null;
        }
    }
}

export default Property;
