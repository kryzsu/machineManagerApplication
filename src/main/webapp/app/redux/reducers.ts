import { createReducer, on } from '@ngrx/store';

import { initialState } from './app.state';
import * as Actions from './actions';

export const reducers = createReducer(
  initialState,
  on(Actions.createMachineList, (state, { machineList }) => ({
    ...state,
    machineList,
    machineNames: machineList.map(machine => machine.name ?? ''),
  }))
);
