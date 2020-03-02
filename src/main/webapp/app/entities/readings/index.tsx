import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Readings from './readings';
import ReadingsDetail from './readings-detail';
import ReadingsUpdate from './readings-update';
import ReadingsDeleteDialog from './readings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ReadingsDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ReadingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ReadingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ReadingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={Readings} />
    </Switch>
  </>
);

export default Routes;
