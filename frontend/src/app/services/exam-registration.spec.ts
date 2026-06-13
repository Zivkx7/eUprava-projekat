import { TestBed } from '@angular/core/testing';

import { ExamRegistration } from './exam-registration';

describe('ExamRegistration', () => {
  let service: ExamRegistration;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExamRegistration);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
