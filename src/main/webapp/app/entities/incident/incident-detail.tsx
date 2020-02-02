import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './incident.reducer';
import { IIncident } from 'app/shared/model/incident.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IIncidentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const IncidentDetail = (props: IIncidentDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { incidentEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Incident [<b>{incidentEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{incidentEntity.title}</dd>
          <dt>
            <span id="risk">Risk</span>
          </dt>
          <dd>{incidentEntity.risk}</dd>
          <dt>
            <span id="notes">Notes</span>
          </dt>
          <dd>{incidentEntity.notes}</dd>
          <dt>Task</dt>
          <dd>{incidentEntity.task ? incidentEntity.task.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/incident" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/incident/${incidentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ incident }: IRootState) => ({
  incidentEntity: incident.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(IncidentDetail);
