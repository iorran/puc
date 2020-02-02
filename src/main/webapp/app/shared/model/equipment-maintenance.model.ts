import { Moment } from 'moment';
import { IEquipment } from 'app/shared/model/equipment.model';

export interface IEquipmentMaintenance {
  id?: number;
  date?: Moment;
  notes?: string;
  equipment?: IEquipment;
}

export const defaultValue: Readonly<IEquipmentMaintenance> = {};
