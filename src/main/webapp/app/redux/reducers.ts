import { IMachine } from '../entities/machine/machine.model';
import { createReducer, on } from '@ngrx/store';
import * as Actions from './actions';

export interface AppState {
  machineList: IMachine[];
}
export const initialState: AppState = {
  machineList: [],
};

export const reducers = createReducer(
  initialState,
  on(Actions.createMachineList, (state, { machineList }) => ({ ...state, machineList }))
);
