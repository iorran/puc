import { ISensor } from 'app/shared/model/sensor.model';

export interface IReadings {
  id?: number;
  readBy?: string;
  height?: number;
  volume?: number;
  humidity?: number;
  pressure?: number;
  sensor?: ISensor;
}

export const defaultValue: Readonly<IReadings> = {};
