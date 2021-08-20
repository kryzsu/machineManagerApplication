import { createAction, props } from '@ngrx/store';

import { IMachine } from '../entities/machine/machine.model';

export const createMachineList = createAction('[machine list] created', props<{ machineList: IMachine[] }>());
export const editJob = createAction('[machine list] created', props<{ jobId: number }>());
