import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IEquipment } from 'app/shared/model/equipment.model';
import { getEntities as getEquipment } from 'app/entities/equipment/equipment.reducer';
import { getEntity, updateEntity, createEntity, reset } from './equipment-maintenance.reducer';
import { IEquipmentMaintenance } from 'app/shared/model/equipment-maintenance.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IEquipmentMaintenanceUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const EquipmentMaintenanceUpdate = (props: IEquipmentMaintenanceUpdateProps) => {
  const [equipmentId, setEquipmentId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { equipmentMaintenanceEntity, equipment, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/equipment-maintenance' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getEquipment();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.date = convertDateTimeToServer(values.date);

    if (errors.length === 0) {
      const entity = {
        ...equipmentMaintenanceEntity,
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
          <h2 id="pucApp.equipmentMaintenance.home.createOrEditLabel">Create or edit a EquipmentMaintenance</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : equipmentMaintenanceEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="equipment-maintenance-id">ID</Label>
                  <AvInput id="equipment-maintenance-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="dateLabel" for="equipment-maintenance-date">
                  Date
                </Label>
                <AvInput
                  id="equipment-maintenance-date"
                  type="datetime-local"
                  className="form-control"
                  name="date"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? null : convertDateTimeFromServer(props.equipmentMaintenanceEntity.date)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="notesLabel" for="equipment-maintenance-notes">
                  Notes
                </Label>
                <AvField id="equipment-maintenance-notes" type="text" name="notes" />
              </AvGroup>
              <AvGroup>
                <Label for="equipment-maintenance-equipment">Equipment</Label>
                <AvInput id="equipment-maintenance-equipment" type="select" className="form-control" name="equipment.id">
                  <option value="" key="0" />
                  {equipment
                    ? equipment.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/equipment-maintenance" replace color="info">
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
  equipment: storeState.equipment.entities,
  equipmentMaintenanceEntity: storeState.equipmentMaintenance.entity,
  loading: storeState.equipmentMaintenance.loading,
  updating: storeState.equipmentMaintenance.updating,
  updateSuccess: storeState.equipmentMaintenance.updateSuccess
});

const mapDispatchToProps = {
  getEquipment,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EquipmentMaintenanceUpdate);
