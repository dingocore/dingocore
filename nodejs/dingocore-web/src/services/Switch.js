import React, {Component} from 'react';
import { Button } from '@patternfly/react-core';
import Slider, { Range } from 'rc-slider';

//import { HuePicker, SaturationPicker } from 'react-color';
import { HuePicker } from 'react-color';
import { Saturation } from 'react-color/lib/components/common';

import 'rc-slider/assets/index.css';

class Switch extends Component {
    constructor(props) {
        super(props);
        this.onClick = this.onClick.bind(this);
    }

    onClick() {
        console.log( this.props );
        console.log( "switch clicked" + this.props.service.name );
    }

    render() {
      return (
            <div>
              <Button onClick={this.onClick}>{this.props.service.name}</Button>
            </div>
      )
    }
}

export default Switch;