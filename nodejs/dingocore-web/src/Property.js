
import React, { Component } from 'react';
import Hue from './properties/Hue';
import On from './properties/On';
import { connect } from 'react-redux';

const Property = ({ property }) => {
    if (property.id === 'Hue') {
        return (
          <Hue property={property}/>
        )
        return null;
    } else if (property.id == 'On') {
        return (
            <On property={property} />
        )
    } else {
        return null;
    }
}

export default Property;