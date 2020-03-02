import { IReadings } from 'app/shared/model/readings.model';
import { ISector } from 'app/shared/model/sector.model';

export interface ISensor {
  id?: number;
  tag?: string;
  latitude?: number;
  longitude?: number;
  maxHeight?: number;
  maxVolume?: number;
  maxHumidity?: number;
  maxPressure?: number;
  readings?: IReadings[];
  sector?: ISector;
}

export const defaultValue: Readonly<ISensor> = {};
