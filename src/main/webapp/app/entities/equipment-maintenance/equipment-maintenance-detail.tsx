import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './equipment-maintenance.reducer';
import { IEquipmentMaintenance } from 'app/shared/model/equipment-maintenance.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEquipmentMaintenanceDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const EquipmentMaintenanceDetail = (props: IEquipmentMaintenanceDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { equipmentMaintenanceEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          EquipmentMaintenance [<b>{equipmentMaintenanceEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="date">Date</span>
          </dt>
          <dd>
            <TextFormat value={equipmentMaintenanceEntity.date} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="notes">Notes</span>
          </dt>
          <dd>{equipmentMaintenanceEntity.notes}</dd>
          <dt>Equipment</dt>
          <dd>{equipmentMaintenanceEntity.equipment ? equipmentMaintenanceEntity.equipment.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/equipment-maintenance" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/equipment-maintenance/${equipmentMaintenanceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ equipmentMaintenance }: IRootState) => ({
  equipmentMaintenanceEntity: equipmentMaintenance.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EquipmentMaintenanceDetail);
