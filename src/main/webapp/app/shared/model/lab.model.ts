import { IStudent } from 'app/shared/model/student.model';

export interface ILab {
  id?: number;
  jobTitle?: string | null;
  minSalary?: number | null;
  maxSalary?: number | null;
  students?: IStudent[] | null;
}

export const defaultValue: Readonly<ILab> = {};
