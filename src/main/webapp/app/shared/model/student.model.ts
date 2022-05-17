import { ILab } from 'app/shared/model/lab.model';

export interface IStudent {
  id?: number;
  title?: string | null;
  description?: string | null;
  labs?: ILab[] | null;
}

export const defaultValue: Readonly<IStudent> = {};
