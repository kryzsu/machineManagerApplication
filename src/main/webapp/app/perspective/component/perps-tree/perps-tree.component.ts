import { Component } from '@angular/core';
import { TreeDragDropService, TreeNode } from 'primeng/api';

import { machine2Treenode } from 'app/perspective/converter-utils';
import { Store } from '@ngrx/store';
import { selectMachineList } from '../../../redux/selectors';
import { filter, map } from 'rxjs/operators';

@Component({
  selector: 'jhi-perps-tree',
  templateUrl: './perps-tree.component.html',
  providers: [TreeDragDropService],
  styleUrls: ['./perps-tree.component.scss'],
})
export class PerpsTreeComponent {
  data: TreeNode[] = [];
  constructor(pstore: Store) {
    pstore
      .select(selectMachineList)
      .pipe(
        filter(val => val !== undefined), // eslint-disable-line @typescript-eslint/no-unnecessary-condition
        map(appState => appState.machineList.map(machine2Treenode))
      )
      .subscribe((nodes: TreeNode[]) => {
        debugger; // eslint-disable-line no-debugger
        this.data = nodes;
      });
  }

  onDrop(event: any): void {
    event.accept();
  }
  // this.machineListChange.emit(this.machineList);
}
