import { createReducer, on } from '@ngrx/store';

import { AppState, initialState } from './app.state';
import * as Actions from './actions';
import { IMachine } from '../entities/machine/machine.model';

export const reducers = createReducer(
  initialState,
  on(Actions.createMachineList, (state, { machineList }) => ({
    ...state,
    machineList,
    machineNames: machineList.map(machine => machine.name ?? ''),
  })),
  on(Actions.deleteMachine, (state: AppState, { machineId }) => {
    const machineList = state.machineList.filter(machine => machine.id !== machineId);
    return {
      ...state,
      machineList,
      machineNames: machineList.map(machine => machine.name ?? ''),
    };
  }),
  on(Actions.deleteJob, (state: AppState, { jobId }) => {
    const machineList: IMachine[] = state.machineList.map(machine => {
      if (machine.jobs?.find(job => job.id !== jobId) === undefined) {
        return machine;
      }
      return { ...machine, jobs: machine.jobs.filter(job => job.id !== jobId) };
    });

    return {
      ...state,
      machineList,
    };
  })
);
