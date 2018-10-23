import React, { Component } from 'react'
import { HuePicker } from 'react-color';

class Hue extends Component {

    constructor(props) {
        super(props);
        this.onChange = this.onChange.bind(this);
    }

    onChange(value) {
        console.log( "hue changed", value);
    }

    render() {
        return (
          <HuePicker
            onChange={this.onChange}
          />
        );
    }
}

export default Hue;