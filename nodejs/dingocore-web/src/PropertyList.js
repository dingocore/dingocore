import React from 'react';
import Hue from './properties/Hue';
import On from './properties/On';
import Property from './Property';
import { connect } from 'net';

const PropertyList = ({ properties }) => {
    return (
        <div>
            {
                properties.map(e => {
                    return <Property property={e} />
                })
            }
        </div>
    )
}

export default PropertyList;