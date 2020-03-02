import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './sensor.reducer';
import { ISensor } from 'app/shared/model/sensor.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISensorDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SensorDetail = (props: ISensorDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { sensorEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Sensor [<b>{sensorEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="tag">Tag</span>
          </dt>
          <dd>{sensorEntity.tag}</dd>
          <dt>
            <span id="latitude">Latitude</span>
          </dt>
          <dd>{sensorEntity.latitude}</dd>
          <dt>
            <span id="longitude">Longitude</span>
          </dt>
          <dd>{sensorEntity.longitude}</dd>
          <dt>
            <span id="maxHeight">Max Height</span>
          </dt>
          <dd>{sensorEntity.maxHeight}</dd>
          <dt>
            <span id="maxVolume">Max Volume</span>
          </dt>
          <dd>{sensorEntity.maxVolume}</dd>
          <dt>
            <span id="maxHumidity">Max Humidity</span>
          </dt>
          <dd>{sensorEntity.maxHumidity}</dd>
          <dt>
            <span id="maxPressure">Max Pressure</span>
          </dt>
          <dd>{sensorEntity.maxPressure}</dd>
          <dt>Sector</dt>
          <dd>{sensorEntity.sector ? sensorEntity.sector.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/sensor" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sensor/${sensorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ sensor }: IRootState) => ({
  sensorEntity: sensor.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SensorDetail);
