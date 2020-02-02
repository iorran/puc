import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IIncident } from 'app/shared/model/incident.model';
import { getEntities as getIncidents } from 'app/entities/incident/incident.reducer';
import { getEntity, updateEntity, createEntity, reset } from './action.reducer';
import { IAction } from 'app/shared/model/action.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IActionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ActionUpdate = (props: IActionUpdateProps) => {
  const [incidentId, setIncidentId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { actionEntity, incidents, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/action' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getIncidents();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.start = convertDateTimeToServer(values.start);
    values.end = convertDateTimeToServer(values.end);

    if (errors.length === 0) {
      const entity = {
        ...actionEntity,
        ...values
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="pucApp.action.home.createOrEditLabel">Create or edit a Action</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : actionEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="action-id">ID</Label>
                  <AvInput id="action-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="titleLabel" for="action-title">
                  Title
                </Label>
                <AvField id="action-title" type="text" name="title" />
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="action-description">
                  Description
                </Label>
                <AvField id="action-description" type="text" name="description" />
              </AvGroup>
              <AvGroup>
                <Label id="startLabel" for="action-start">
                  Start
                </Label>
                <AvInput
                  id="action-start"
                  type="datetime-local"
                  className="form-control"
                  name="start"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? null : convertDateTimeFromServer(props.actionEntity.start)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="endLabel" for="action-end">
                  End
                </Label>
                <AvInput
                  id="action-end"
                  type="datetime-local"
                  className="form-control"
                  name="end"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? null : convertDateTimeFromServer(props.actionEntity.end)}
                />
              </AvGroup>
              <AvGroup>
                <Label for="action-incident">Incident</Label>
                <AvInput id="action-incident" type="select" className="form-control" name="incident.id">
                  <option value="" key="0" />
                  {incidents
                    ? incidents.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.title}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/action" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  incidents: storeState.incident.entities,
  actionEntity: storeState.action.entity,
  loading: storeState.action.loading,
  updating: storeState.action.updating,
  updateSuccess: storeState.action.updateSuccess
});

const mapDispatchToProps = {
  getIncidents,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ActionUpdate);
