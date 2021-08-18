import { Component, EventEmitter, Input, Output } from '@angular/core';
import { IMachine } from 'app/shared/model/machine.model';
import { TreeNode } from 'primeng/api';

@Component({
  selector: 'jhi-perps-tree',
  templateUrl: './perps-tree.component.html',
  styleUrls: ['./perps-tree.component.scss'],
})
export class PerpsTreeComponent {
  @Input()
  set machineList(machineList: IMachine[]) {
    this._name = (name && name.trim()) || '<no name set>';
  }
  machineList: IMachine[] = [];

  @Output() machineListChange = new EventEmitter<IMachine[]>();

  data: TreeNode[] = [];

  onDrop(event: any): void {
    event.accept();
  }
  // this.machineListChange.emit(this.machineList);
}
