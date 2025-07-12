import { inject, Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';
import { ErrorResponse } from '../models/error.model';

@Injectable({
  providedIn: 'root',
})
export class ToastService {
  private messageService = inject(MessageService);

  showError(error: ErrorResponse): void {
    this.messageService.add({
      severity: 'error',
      summary: error.error,
      detail: error.message,
      life: 3000,
    });
  }
}
