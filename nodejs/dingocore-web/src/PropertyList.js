import React from 'react';
import Hue from './properties/Hue';
import On from './properties/On';
import Property from './Property';
import { connect } from 'net';

const PropertyList = ({properties})=>{
    return (
        <div>
            <div>Properties</div>
            { 
                properties.map(e=>{
                    console.log( "RENDER PROP", e);
                   return <Property property={e}/>
                })
            }
        </div>
    )
}

export default PropertyList;