import { IAction } from 'app/shared/model/action.model';
import { ITask } from 'app/shared/model/task.model';
import { RiskType } from 'app/shared/model/enumerations/risk-type.model';

export interface IIncident {
  id?: number;
  title?: string;
  risk?: RiskType;
  notes?: any;
  actions?: IAction[];
  task?: ITask;
}

export const defaultValue: Readonly<IIncident> = {};
