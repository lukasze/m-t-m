import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Lab from './lab';
import LabDetail from './lab-detail';
import LabUpdate from './lab-update';
import LabDeleteDialog from './lab-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LabUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LabUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LabDetail} />
      <ErrorBoundaryRoute path={match.url} component={Lab} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LabDeleteDialog} />
  </>
);

export default Routes;
