import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Equipment from './equipment';
import EquipmentMaintenance from './equipment-maintenance';
import Dam from './dam';
import Sector from './sector';
import Task from './task';
import Incident from './incident';
import Action from './action';
import Sensor from './sensor';
import Readings from './readings';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}equipment`} component={Equipment} />
      <ErrorBoundaryRoute path={`${match.url}equipment-maintenance`} component={EquipmentMaintenance} />
      <ErrorBoundaryRoute path={`${match.url}dam`} component={Dam} />
      <ErrorBoundaryRoute path={`${match.url}sector`} component={Sector} />
      <ErrorBoundaryRoute path={`${match.url}task`} component={Task} />
      <ErrorBoundaryRoute path={`${match.url}incident`} component={Incident} />
      <ErrorBoundaryRoute path={`${match.url}action`} component={Action} />
      <ErrorBoundaryRoute path={`${match.url}sensor`} component={Sensor} />
      <ErrorBoundaryRoute path={`${match.url}readings`} component={Readings} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
