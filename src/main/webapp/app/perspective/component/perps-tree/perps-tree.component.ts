import { Component } from '@angular/core';
import { MenuItem, TreeDragDropService, TreeNode } from 'primeng/api';

import { machine2Treenode } from 'app/perspective/converter-utils';
import { Store } from '@ngrx/store';
import { selectMachineList } from '../../../redux/selectors';
import { filter, map } from 'rxjs/operators';
import * as Actions from '../../../redux/actions';

@Component({
  selector: 'jhi-perps-tree',
  templateUrl: './perps-tree.component.html',
  providers: [TreeDragDropService],
  styleUrls: ['./perps-tree.component.scss'],
})
export class PerpsTreeComponent {
  data: TreeNode[] = [];
  menuitems: MenuItem[] = [];
  selectedItem: TreeNode | null = null;
  constructor(private store: Store) {
    store
      .select(selectMachineList)
      .pipe(
        filter(val => val !== undefined), // eslint-disable-line @typescript-eslint/no-unnecessary-condition
        map(appState => appState.machineList.map(machine2Treenode))
      )
      .subscribe((nodes: TreeNode[]) => {
        this.data = nodes;
      });
    this.menuitems = [
      { label: 'Edit', icon: 'pi pi-search', command: () => this.onEdit() },
      { label: 'New', icon: 'pi pi-times', command: () => this.onNew() },
    ];
  }
  onDrop(event: any): void {
    event.accept();
  }

  onEdit(): void {
    const isJobNodeSelected = this.selectedItem?.parent !== null; // eslint-disable-line @typescript-eslint/no-unnecessary-condition
    if (isJobNodeSelected) {
      const keyParts = this.selectedItem?.key?.split(' ');
      if (keyParts === undefined) {
        return;
      }
      const jobId = Number(keyParts[1]);
      this.store.dispatch(Actions.editJob({ jobId }));
    }
  }

  onNew(): void {
    const isJobNodeSelected = this.selectedItem?.parent !== null; // eslint-disable-line @typescript-eslint/no-unnecessary-condition
    let key;
    if (isJobNodeSelected) {
      key = this.selectedItem?.parent?.key ?? '';
    } else {
      key = this.selectedItem?.key ?? '';
    }
    const keyParts = key.split(' ');
    const machineId = Number(keyParts[1]);
    this.store.dispatch(Actions.newJob({ machineId }));
  }

  // this.machineListChange.emit(this.machineList);
}
