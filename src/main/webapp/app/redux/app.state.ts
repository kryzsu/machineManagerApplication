import { IMachine } from '../entities/machine/machine.model';

export interface MachineStatistic {
  label: string;
  data: number[];
}

export interface AppState {
  machineList: IMachine[];
  machineNames: string[];
}

export const initialState: AppState = {
  machineList: [],
  machineNames: [],
};
