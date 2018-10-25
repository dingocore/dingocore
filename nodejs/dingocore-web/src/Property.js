
import React, { Component } from 'react';
import Hue from './properties/Hue';
import On from './properties/On';
import { connect } from 'react-redux';

const Property = ({ property }) => {
    return (
        <div className="Property">
            {(property.id === 'Hue') ? <Hue property={property} /> : null}
            {(property.id === 'On') ? <On property={property} /> : null}
        </div>
    )
}

export default Property;