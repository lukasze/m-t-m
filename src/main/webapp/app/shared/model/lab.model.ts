import { IStudent } from 'app/shared/model/student.model';

export interface ILab {
  id?: number;
  title?: string | null;
  descritpion?: string | null;
  students?: IStudent[] | null;
}

export const defaultValue: Readonly<ILab> = {};
