import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TreeDragDropService, TreeNode } from 'primeng/api';

import { machine2Treenode } from 'app/perspective/converter-utils';
import { IMachine } from 'app/entities/machine/machine.model';
import { Store } from '@ngrx/store';
import { selectMachineList } from '../../../redux/selectors';
import { map } from 'rxjs/operators';
import { AppState } from '../../../redux/app.state';

@Component({
  selector: 'jhi-perps-tree',
  templateUrl: './perps-tree.component.html',
  providers: [TreeDragDropService],
  styleUrls: ['./perps-tree.component.scss'],
})
export class PerpsTreeComponent {
  data: TreeNode[] = [];

  constructor(private readonly store: Store) {
    this.store
      .select(selectMachineList)
      .pipe(map(appState => appState.machineList.map(machine2Treenode)))
      .subscribe((nodes: TreeNode[]) => {
        this.data = nodes;
      });
  }

  onDrop(event: any): void {
    event.accept();
  }
  // this.machineListChange.emit(this.machineList);
}
