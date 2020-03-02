import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './readings.reducer';
import { IReadings } from 'app/shared/model/readings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IReadingsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ReadingsDetail = (props: IReadingsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { readingsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Readings [<b>{readingsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="readBy">Read By</span>
          </dt>
          <dd>{readingsEntity.readBy}</dd>
          <dt>
            <span id="height">Height</span>
          </dt>
          <dd>{readingsEntity.height}</dd>
          <dt>
            <span id="volume">Volume</span>
          </dt>
          <dd>{readingsEntity.volume}</dd>
          <dt>
            <span id="humidity">Humidity</span>
          </dt>
          <dd>{readingsEntity.humidity}</dd>
          <dt>
            <span id="pressure">Pressure</span>
          </dt>
          <dd>{readingsEntity.pressure}</dd>
          <dt>Sensor</dt>
          <dd>{readingsEntity.sensor ? readingsEntity.sensor.tag : ''}</dd>
        </dl>
        <Button tag={Link} to="/readings" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/readings/${readingsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ readings }: IRootState) => ({
  readingsEntity: readings.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ReadingsDetail);
