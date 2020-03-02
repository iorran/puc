import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ISensor } from 'app/shared/model/sensor.model';
import { getEntities as getSensors } from 'app/entities/sensor/sensor.reducer';
import { getEntity, updateEntity, createEntity, reset } from './readings.reducer';
import { IReadings } from 'app/shared/model/readings.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IReadingsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ReadingsUpdate = (props: IReadingsUpdateProps) => {
  const [sensorId, setSensorId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { readingsEntity, sensors, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/readings' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getSensors();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...readingsEntity,
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
          <h2 id="pucApp.readings.home.createOrEditLabel">Create or edit a Readings</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : readingsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="readings-id">ID</Label>
                  <AvInput id="readings-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="readByLabel" for="readings-readBy">
                  Read By
                </Label>
                <AvField id="readings-readBy" type="text" name="readBy" />
              </AvGroup>
              <AvGroup>
                <Label id="heightLabel" for="readings-height">
                  Height
                </Label>
                <AvField id="readings-height" type="string" className="form-control" name="height" />
              </AvGroup>
              <AvGroup>
                <Label id="volumeLabel" for="readings-volume">
                  Volume
                </Label>
                <AvField id="readings-volume" type="string" className="form-control" name="volume" />
              </AvGroup>
              <AvGroup>
                <Label id="humidityLabel" for="readings-humidity">
                  Humidity
                </Label>
                <AvField id="readings-humidity" type="string" className="form-control" name="humidity" />
              </AvGroup>
              <AvGroup>
                <Label id="pressureLabel" for="readings-pressure">
                  Pressure
                </Label>
                <AvField id="readings-pressure" type="string" className="form-control" name="pressure" />
              </AvGroup>
              <AvGroup>
                <Label for="readings-sensor">Sensor</Label>
                <AvInput id="readings-sensor" type="select" className="form-control" name="sensor.id">
                  <option value="" key="0" />
                  {sensors
                    ? sensors.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.tag}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/readings" replace color="info">
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
  sensors: storeState.sensor.entities,
  readingsEntity: storeState.readings.entity,
  loading: storeState.readings.loading,
  updating: storeState.readings.updating,
  updateSuccess: storeState.readings.updateSuccess
});

const mapDispatchToProps = {
  getSensors,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ReadingsUpdate);
