import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserStadistics } from './user-stadistics';

describe('UserStadistics', () => {
  let component: UserStadistics;
  let fixture: ComponentFixture<UserStadistics>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserStadistics],
    }).compileComponents();

    fixture = TestBed.createComponent(UserStadistics);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
