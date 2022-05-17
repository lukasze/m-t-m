import { ILab } from 'app/shared/model/lab.model';

export interface IStudent {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  labs?: ILab[] | null;
}

export const defaultValue: Readonly<IStudent> = {};
