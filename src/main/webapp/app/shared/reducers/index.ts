import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import equipment, {
  EquipmentState
} from 'app/entities/equipment/equipment.reducer';
// prettier-ignore
import equipmentMaintenance, {
  EquipmentMaintenanceState
} from 'app/entities/equipment-maintenance/equipment-maintenance.reducer';
// prettier-ignore
import dam, {
  DamState
} from 'app/entities/dam/dam.reducer';
// prettier-ignore
import sector, {
  SectorState
} from 'app/entities/sector/sector.reducer';
// prettier-ignore
import task, {
  TaskState
} from 'app/entities/task/task.reducer';
// prettier-ignore
import incident, {
  IncidentState
} from 'app/entities/incident/incident.reducer';
// prettier-ignore
import action, {
  ActionState
} from 'app/entities/action/action.reducer';
// prettier-ignore
import sensor, {
  SensorState
} from 'app/entities/sensor/sensor.reducer';
// prettier-ignore
import readings, {
  ReadingsState
} from 'app/entities/readings/readings.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly equipment: EquipmentState;
  readonly equipmentMaintenance: EquipmentMaintenanceState;
  readonly dam: DamState;
  readonly sector: SectorState;
  readonly task: TaskState;
  readonly incident: IncidentState;
  readonly action: ActionState;
  readonly sensor: SensorState;
  readonly readings: ReadingsState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  equipment,
  equipmentMaintenance,
  dam,
  sector,
  task,
  incident,
  action,
  sensor,
  readings,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
