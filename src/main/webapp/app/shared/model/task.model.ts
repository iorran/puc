import { Moment } from 'moment';
import { IIncident } from 'app/shared/model/incident.model';
import { ISector } from 'app/shared/model/sector.model';

export interface ITask {
  id?: number;
  title?: string;
  description?: string;
  start?: Moment;
  end?: Moment;
  incidents?: IIncident[];
  sector?: ISector;
}

export const defaultValue: Readonly<ITask> = {};
