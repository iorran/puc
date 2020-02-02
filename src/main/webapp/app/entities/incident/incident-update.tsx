import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ITask } from 'app/shared/model/task.model';
import { getEntities as getTasks } from 'app/entities/task/task.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './incident.reducer';
import { IIncident } from 'app/shared/model/incident.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IIncidentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const IncidentUpdate = (props: IIncidentUpdateProps) => {
  const [taskId, setTaskId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { incidentEntity, tasks, loading, updating } = props;

  const { notes } = incidentEntity;

  const handleClose = () => {
    props.history.push('/incident' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getTasks();
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...incidentEntity,
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
          <h2 id="pucApp.incident.home.createOrEditLabel">Create or edit a Incident</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : incidentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="incident-id">ID</Label>
                  <AvInput id="incident-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="titleLabel" for="incident-title">
                  Title
                </Label>
                <AvField id="incident-title" type="text" name="title" />
              </AvGroup>
              <AvGroup>
                <Label id="riskLabel" for="incident-risk">
                  Risk
                </Label>
                <AvInput
                  id="incident-risk"
                  type="select"
                  className="form-control"
                  name="risk"
                  value={(!isNew && incidentEntity.risk) || 'HIGH'}
                >
                  <option value="HIGH">HIGH</option>
                  <option value="MEDIUM">MEDIUM</option>
                  <option value="LOWER">LOWER</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="notesLabel" for="incident-notes">
                  Notes
                </Label>
                <AvInput id="incident-notes" type="textarea" name="notes" />
              </AvGroup>
              <AvGroup>
                <Label for="incident-task">Task</Label>
                <AvInput id="incident-task" type="select" className="form-control" name="task.id">
                  <option value="" key="0" />
                  {tasks
                    ? tasks.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.title}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/incident" replace color="info">
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
  tasks: storeState.task.entities,
  incidentEntity: storeState.incident.entity,
  loading: storeState.incident.loading,
  updating: storeState.incident.updating,
  updateSuccess: storeState.incident.updateSuccess
});

const mapDispatchToProps = {
  getTasks,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(IncidentUpdate);
