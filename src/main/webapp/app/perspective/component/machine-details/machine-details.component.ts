import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { getRelatedProduct, IIdWithPriority, IJob, Job } from '../../../entities/job/job.model';
import { IMachine, Machine } from '../../../entities/machine/machine.model';
import { selectMachineList } from '../../../redux/selectors';
import { filter, map } from 'rxjs/operators';
import { machine2Treenode } from '../../converter-utils';
import { TreeNode } from 'primeng/api';
import { Store } from '@ngrx/store';
import { FilterInterval } from '../interval-filter/filter-interval';

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

  @Output() savePriorities = new EventEmitter<IIdWithPriority[]>();

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
  }

  machineChanged(): void {
    if (this.selectedMachine?.jobs != null) {
      this.jobs = this.selectedMachine.jobs.map(job => ({ ...job }));
    }
  }

  ngOnInit(): void {
    this.jobs = [];
    this.machines = [];
  }

  getJobProduct(job: IJob): string {
    return getRelatedProduct(job);
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
}
