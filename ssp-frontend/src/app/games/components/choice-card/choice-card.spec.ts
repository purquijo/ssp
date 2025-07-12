import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChoiceCard } from './choice-card';

describe('ChoiceCard', () => {
  let component: ChoiceCard;
  let fixture: ComponentFixture<ChoiceCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChoiceCard],
    }).compileComponents();

    fixture = TestBed.createComponent(ChoiceCard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
