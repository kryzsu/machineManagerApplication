import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TreeDragDropService, TreeNode } from 'primeng/api';

import { machine2Treenode } from 'app/perspective/converter-utils';
import { IMachine } from 'app/entities/machine/machine.model';

@Component({
  selector: 'jhi-perps-tree',
  templateUrl: './perps-tree.component.html',
  providers: [TreeDragDropService],
  styleUrls: ['./perps-tree.component.scss'],
})
export class PerpsTreeComponent {
  @Input()
  set machineList(machineList: IMachine[]) {
    this.data = machineList.map(machine2Treenode);
  }

  @Output() machineListChange = new EventEmitter<IMachine[]>();

  data: TreeNode[] = [];

  onDrop(event: any): void {
    event.accept();
  }
  // this.machineListChange.emit(this.machineList);
}
