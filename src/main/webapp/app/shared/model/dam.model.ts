import { ISector } from 'app/shared/model/sector.model';

export interface IDam {
  id?: number;
  name?: string;
  adress?: string;
  sectors?: ISector[];
}

export const defaultValue: Readonly<IDam> = {};
