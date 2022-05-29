import { IJob } from '../entities/job/job.model';
import {IMachine, IMachineDay} from '../entities/machine/machine.model';

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
