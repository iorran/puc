import { ITask } from 'app/shared/model/task.model';
import { ISensor } from 'app/shared/model/sensor.model';
import { IDam } from 'app/shared/model/dam.model';

export interface ISector {
  id?: number;
  name?: string;
  tasks?: ITask[];
  sensors?: ISensor[];
  dam?: IDam;
}

export const defaultValue: Readonly<ISector> = {};
