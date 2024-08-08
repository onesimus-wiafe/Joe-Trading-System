import { Component, computed, effect, input, output } from '@angular/core';
import {
  FormGroup,
  FormControl,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import {
  Portfolio,
  PortfolioCreate,
  PortfolioCreateSchema,
} from '../../models/portfolio.model';
import * as v from 'valibot';

@Component({
  selector: 'app-portfolio-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './portfolio-form.component.html',
  styleUrl: './portfolio-form.component.css',
})
export class PortfolioFormComponent {
  portfolioForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
    ]),
    description: new FormControl(''),
  });

  defaultPortfolio = input<Portfolio | null>(null);
  isEdit = computed<boolean>(() => !!this.defaultPortfolio());

  submit = output<PortfolioCreate & { id?: number }>();

  constructor() {
    effect(() => {
      this.portfolioForm = new FormGroup({
        name: new FormControl(
          this.defaultPortfolio()?.name || '',
          Validators.required
        ),
        description: new FormControl(
          this.defaultPortfolio()?.description || ''
        ),
      });
    });
  }

  handleSubmit() {
    if (this.portfolioForm.invalid) {
      this.portfolioForm.markAllAsTouched();
      return;
    }

    const result = v.safeParse(PortfolioCreateSchema, this.portfolioForm.value);
    if (result.success) {
      console.log("emitted", { ...result.output, id: this.defaultPortfolio()?.id });
      this.submit.emit({ ...result.output, id: this.defaultPortfolio()?.id });
    } else {
      console.error(result.issues);
    }
  }
}
