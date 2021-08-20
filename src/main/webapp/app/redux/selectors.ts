import { AppState } from './reducers';
import { createFeatureSelector } from '@ngrx/store';

export const selectMachineList = createFeatureSelector<AppState>('machineList');
