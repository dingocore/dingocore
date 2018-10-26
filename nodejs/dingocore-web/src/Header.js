import React from 'react';
import { PageHeader } from '@patternfly/react-core';


const Header = (
    <PageHeader
        logo="Logo"
        showNavToggle
        onNavToggle={this.onNavToggle}
        toolbar="Toolbar" avatar=" | Avatar" />
);

export default Header;