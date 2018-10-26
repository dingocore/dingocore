
import React from 'react';
import { PageSidebar } from '@patternfly/react-core';
import Navigation from './Navigation';

//import './Sidebar.css';

const Sidebar = (
    <PageSidebar className="Sidebar" nav={Navigation} isNavOpen={true} />
     
);

export default Sidebar;

