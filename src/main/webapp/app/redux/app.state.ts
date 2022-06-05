import { IJob } from '../entities/job/job.model';
import {IMachine} from '../entities/machine/machine.model';
import {IMachineDay} from "../entities/machineday";

export interface MachineStatistic {
  label: string;
  data: number[];
}

export interface AppState {
  machineList: IMachine[];
  machineNames: string[];
  selectedJob: IJob | undefined;
  machineDays : IMachineDay[];
}

export const initialState: AppState = {
  machineList: [],
  machineNames: [],
  selectedJob: undefined,
  machineDays: []
};
