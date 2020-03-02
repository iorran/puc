import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ISector } from 'app/shared/model/sector.model';
import { getEntities as getSectors } from 'app/entities/sector/sector.reducer';
import { getEntity, updateEntity, createEntity, reset } from './sensor.reducer';
import { ISensor } from 'app/shared/model/sensor.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISensorUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SensorUpdate = (props: ISensorUpdateProps) => {
  const [sectorId, setSectorId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { sensorEntity, sectors, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/sensor' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getSectors();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...sensorEntity,
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
          <h2 id="pucApp.sensor.home.createOrEditLabel">Create or edit a Sensor</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : sensorEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="sensor-id">ID</Label>
                  <AvInput id="sensor-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="tagLabel" for="sensor-tag">
                  Tag
                </Label>
                <AvField id="sensor-tag" type="text" name="tag" />
              </AvGroup>
              <AvGroup>
                <Label id="latitudeLabel" for="sensor-latitude">
                  Latitude
                </Label>
                <AvField id="sensor-latitude" type="string" className="form-control" name="latitude" />
              </AvGroup>
              <AvGroup>
                <Label id="longitudeLabel" for="sensor-longitude">
                  Longitude
                </Label>
                <AvField id="sensor-longitude" type="string" className="form-control" name="longitude" />
              </AvGroup>
              <AvGroup>
                <Label id="maxHeightLabel" for="sensor-maxHeight">
                  Max Height
                </Label>
                <AvField id="sensor-maxHeight" type="string" className="form-control" name="maxHeight" />
              </AvGroup>
              <AvGroup>
                <Label id="maxVolumeLabel" for="sensor-maxVolume">
                  Max Volume
                </Label>
                <AvField id="sensor-maxVolume" type="string" className="form-control" name="maxVolume" />
              </AvGroup>
              <AvGroup>
                <Label id="maxHumidityLabel" for="sensor-maxHumidity">
                  Max Humidity
                </Label>
                <AvField id="sensor-maxHumidity" type="string" className="form-control" name="maxHumidity" />
              </AvGroup>
              <AvGroup>
                <Label id="maxPressureLabel" for="sensor-maxPressure">
                  Max Pressure
                </Label>
                <AvField id="sensor-maxPressure" type="string" className="form-control" name="maxPressure" />
              </AvGroup>
              <AvGroup>
                <Label for="sensor-sector">Sector</Label>
                <AvInput id="sensor-sector" type="select" className="form-control" name="sector.id">
                  <option value="" key="0" />
                  {sectors
                    ? sectors.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/sensor" replace color="info">
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
  sectors: storeState.sector.entities,
  sensorEntity: storeState.sensor.entity,
  loading: storeState.sensor.loading,
  updating: storeState.sensor.updating,
  updateSuccess: storeState.sensor.updateSuccess
});

const mapDispatchToProps = {
  getSectors,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SensorUpdate);
