import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './action.reducer';
import { IAction } from 'app/shared/model/action.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IActionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ActionDetail = (props: IActionDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { actionEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Action [<b>{actionEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{actionEntity.title}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{actionEntity.description}</dd>
          <dt>
            <span id="start">Start</span>
          </dt>
          <dd>
            <TextFormat value={actionEntity.start} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="end">End</span>
          </dt>
          <dd>
            <TextFormat value={actionEntity.end} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>Incident</dt>
          <dd>{actionEntity.incident ? actionEntity.incident.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/action" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/action/${actionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ action }: IRootState) => ({
  actionEntity: action.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ActionDetail);
