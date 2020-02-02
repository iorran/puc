import { IEquipmentMaintenance } from 'app/shared/model/equipment-maintenance.model';

export interface IEquipment {
  id?: number;
  name?: string;
  maintenances?: IEquipmentMaintenance[];
}

export const defaultValue: Readonly<IEquipment> = {};
