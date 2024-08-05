import { ComponentType } from '@angular/cdk/portal';
import {
  ApplicationRef,
  ComponentFactoryResolver,
  EmbeddedViewRef,
  Injectable,
  Injector,
  ViewContainerRef,
} from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { ConfirmDialogComponent } from '../../shared/components/confirm-dialog/confirm-dialog.component';

export interface ConfirmDialogData {
  title: string;
  message: string;
}


@Injectable({
  providedIn: 'root',
})
export class DialogService {
  private static instance: DialogService | null = null;
  constructor(
    private injector: Injector,
    private appRef: ApplicationRef,
    private componentFactoryResolver: ComponentFactoryResolver
  ) {
    DialogService.instance = this;
  }

  public static getInstance() {
    return DialogService.instance;
  }

  open<T>(title: string, message: string): Observable<boolean> {
    const dialogSubject = new Subject<boolean>();

    const componentFactory =
      this.componentFactoryResolver.resolveComponentFactory(
        ConfirmDialogComponent
      );
    const componentRef = componentFactory.create(this.injector);

    componentRef.instance.title.set(title);
    componentRef.instance.message.set(message);

    this.appRef.attachView(componentRef.hostView);
    const domElem = (componentRef.hostView as EmbeddedViewRef<any>)
      .rootNodes[0] as HTMLElement;
    document.body.appendChild(domElem);

    componentRef.instance.close = (result: boolean) => {
      dialogSubject.next(result);
      dialogSubject.complete();

      this.appRef.detachView(componentRef.hostView);
      componentRef.destroy();
    };

    return dialogSubject.asObservable();
  }
}

const defaultConfirmData = {
  title: 'Confirmation',
  message: 'Are you sure you want to perform this action?',
};

export function needConfirmation(
  confirmData: ConfirmDialogData = defaultConfirmData
) {
  return function (
    target: Object,
    propertyKey: string,
    descriptor: PropertyDescriptor
  ) {
    const originalMethod = descriptor.value;

    descriptor.value = async function (...args: any) {
      DialogService.getInstance()
        ?.open(confirmData.title, confirmData.message)
        .subscribe((validation) => {
          if (validation) {
            originalMethod.apply(this, args);
          }
        });
    };

    return descriptor;
  };
}
