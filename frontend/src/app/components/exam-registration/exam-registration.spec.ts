import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExamRegistration } from './exam-registration';

describe('ExamRegistration', () => {
  let component: ExamRegistration;
  let fixture: ComponentFixture<ExamRegistration>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExamRegistration]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExamRegistration);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
