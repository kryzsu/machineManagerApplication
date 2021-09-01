import { createAction, props } from '@ngrx/store';

import { IMachine } from '../entities/machine/machine.model';

export const createMachineList = createAction('[machine list] created', props<{ machineList: IMachine[] }>());
export const deleteMachine = createAction('[machine] delete', props<{ machineId: number }>());

export const deleteJob = createAction('[job] delete', props<{ jobId: number }>());
export const editJob = createAction('[job] edit', props<{ jobId: number }>());
export const editJobEnd = createAction('[job] edit end', props<{ jobId: number }>());

export const newJob = createAction('[job] new', props<{ machineId: number }>());
export const newJobEnd = createAction('[job] new end');
