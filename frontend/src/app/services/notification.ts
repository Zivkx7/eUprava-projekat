import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  message = '';
  type = '';
  visible = false;

  success(msg: string) {
    this.message = msg;
    this.type = 'success';
    this.visible = true;
    setTimeout(() => this.visible = false, 3000);
  }

  error(msg: string) {
    this.message = msg;
    this.type = 'error';
    this.visible = true;
    setTimeout(() => this.visible = false, 3000);
  }
}