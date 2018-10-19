
import DingoMQTT from 'dingocore-redux';
import {Component, Children} from "react";


class Connection extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = { connected: false };
    }

    componentWillMount() {
        console.log( DingoMQTT );
        const client = new DingoMQTT(this.props.url, this.props.store);
        client.connect(this.connected.bind(this));
    }

    connected() {
        console.log( "signal connected");
        this.setState( {connected: true} );
    }

    render() {
        console.log( "rendering" );
        if ( this.state.connected ) {
            console.log( "render as connected" );
            return Children.only(this.props.children);
        } else {
            console.log( "render as not connected" );
            return null;
        }
    }

}

export default Connection;