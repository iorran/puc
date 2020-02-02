import { ITask } from 'app/shared/model/task.model';
import { IDam } from 'app/shared/model/dam.model';

export interface ISector {
  id?: number;
  name?: string;
  tasks?: ITask[];
  dam?: IDam;
}

export const defaultValue: Readonly<ISector> = {};
