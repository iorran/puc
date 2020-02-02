import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import EquipmentMaintenance from './equipment-maintenance';
import EquipmentMaintenanceDetail from './equipment-maintenance-detail';
import EquipmentMaintenanceUpdate from './equipment-maintenance-update';
import EquipmentMaintenanceDeleteDialog from './equipment-maintenance-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={EquipmentMaintenanceDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EquipmentMaintenanceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EquipmentMaintenanceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EquipmentMaintenanceDetail} />
      <ErrorBoundaryRoute path={match.url} component={EquipmentMaintenance} />
    </Switch>
  </>
);

export default Routes;
