import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { getRelatedProduct, IIdWithPriority, IJob } from '../../../entities/job/job.model';
import {IMachine, IMachineDay} from '../../../entities/machine/machine.model';
import { selectMachineList } from '../../../redux/selectors';
import { filter, map } from 'rxjs/operators';
import { Store } from '@ngrx/store';

@Component({
  selector: 'jhi-machine-details',
  templateUrl: './machine-details.component.html',
  styleUrls: ['./machine-details.component.scss'],
})
export class MachineDetailsComponent implements OnInit {
  jobs: IJob[];
  machines: IMachine[];
  selectedMachine?: IMachine;
  changed = false;
  machineDays : IMachineDay[] = [];
  weekName: string[] = ['', 'Hetfo', 'Kedd', 'Szerda', 'Csutortok', 'Pentek', 'Szombat', 'Vasarnap'];

  @Output() savePriorities = new EventEmitter<IIdWithPriority[]>();
  @Output() getMachineDays = new EventEmitter();
  @Output() startNextJob = new EventEmitter<number>();
  @Output() stopRunningJob = new EventEmitter<number>();


  constructor(private store: Store) {
    this.jobs = [];
    this.machines = [];
    this.selectedMachine = undefined;
    store
      .select(selectMachineList)
      .pipe(
        filter(val => val !== undefined), // eslint-disable-line @typescript-eslint/no-unnecessary-condition
        map(appState => appState.machineList)
        // map(machine => ({...machine}))
      )
      .subscribe((machineList: IMachine[]) => {
        this.machines = machineList;
        if (machineList.length > 0) {
          this.selectedMachine = machineList[0];
          this.machineChanged();
        }
      });

    store
      .select(selectMachineList)
      .pipe(
        filter(val => val !== undefined), // eslint-disable-line @typescript-eslint/no-unnecessary-condition
        map(appState => appState.machineDays)
      )
      .subscribe((machineDays: IMachineDay[]) => {
        this.machineDays = machineDays;
      });
  }

  machineChanged(): void {
    if (this.selectedMachine?.jobs != null) {
      this.jobs = this.selectedMachine.jobs.map(job => ({ ...job }));
    }

    if (this.selectedMachine?.id != null) {
      this.getMachineDays.emit(this.selectedMachine.id);
    }
  }

  ngOnInit(): void {
    this.jobs = [];
    this.machines = [];
    this.machineDays = [];
  }

  getJobProduct(job: IJob): string {
    return getRelatedProduct(job);
  }

  getSeverity(machineDay: IMachineDay): string {
    return machineDay.occupied ? "danger" : "success";
  }

  reorder(): void {
    let priority = this.jobs.length;
    this.jobs.forEach((job: IJob) => (job.priority = priority--));
    this.changed = true;
  }

  onSave(): void {
    console.warn(this.selectedMachine);
    this.savePriorities.emit(
      this.jobs.map((job: IJob) => ({
        id: job.id,
        priority: job.priority,
      }))
    );
  }

  onStartNextJob(): void {
    if (this.selectedMachine == null) {
      return;
    }

    this.startNextJob.emit(this.selectedMachine.id);
  }

  onStopRunningJob(): void {
    if (this.selectedMachine == null) {
      return;
    }

    this.stopRunningJob.emit(this.selectedMachine.id);
  }

  getProductName(selectedMachine?: IMachine): string {
    if (selectedMachine?.runningJob?.products == null) {
      return '';
    }

    return selectedMachine.runningJob.products[0]?.name ?? '';
  }
}
