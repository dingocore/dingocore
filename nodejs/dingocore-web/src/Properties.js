import React from 'react';
import Hue from './properties/Hue';

export default ({properties})=>{
    return (
        <div>
            { 
                properties.map(e=>{
                    if ( e.id == 'Hue' ) {
                        return <Hue property={e}/>
                    }
                })
            }
        </div>

    )

}
