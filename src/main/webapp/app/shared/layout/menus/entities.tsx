import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Modules" id="entity-menu">
    <MenuItem icon="th-list" to="">
      UC1 - Controle de Ativos
      <MenuItem icon="asterisk" to="/equipment">
        Equipment
      </MenuItem>
      <MenuItem icon="asterisk" to="/equipment-maintenance">
        Equipment Maintenance
      </MenuItem>

      <MenuItem icon="asterisk" to="/dam">
        Dam
      </MenuItem>
      <MenuItem icon="asterisk" to="/sector">
        Sector
      </MenuItem>
      <MenuItem icon="asterisk" to="/sensor">
        Sensor
      </MenuItem>
    </MenuItem>

    <MenuItem icon="th-list" to="">
      UC2 - Controle de Processos Minerários
      <MenuItem icon="asterisk" to="/task">
        Task
      </MenuItem>
      <MenuItem icon="asterisk" to="/incident">
        Incident
      </MenuItem>
      <MenuItem icon="asterisk" to="/action">
        Action
      </MenuItem>
    </MenuItem>

    <MenuItem icon="th-list" to="">
      UC3 - Monitoramento de Barragens
      <MenuItem icon="asterisk" to="/readings">
        Readings
      </MenuItem>
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
