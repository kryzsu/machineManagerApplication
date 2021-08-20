import { createFeatureSelector } from '@ngrx/store';

import { AppState } from './app.state';

export const selectMachineList = createFeatureSelector<AppState>('machineList');
