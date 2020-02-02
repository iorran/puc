import { Moment } from 'moment';
import { IIncident } from 'app/shared/model/incident.model';

export interface IAction {
  id?: number;
  title?: string;
  description?: string;
  start?: Moment;
  end?: Moment;
  incident?: IIncident;
}

export const defaultValue: Readonly<IAction> = {};
