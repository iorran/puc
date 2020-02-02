import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Dam from './dam';
import DamDetail from './dam-detail';
import DamUpdate from './dam-update';
import DamDeleteDialog from './dam-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DamDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DamUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DamUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DamDetail} />
      <ErrorBoundaryRoute path={match.url} component={Dam} />
    </Switch>
  </>
);

export default Routes;
