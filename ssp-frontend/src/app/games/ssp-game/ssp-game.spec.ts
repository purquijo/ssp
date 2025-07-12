import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BotGame } from './ssp-game';

describe('BotGame', () => {
  let component: BotGame;
  let fixture: ComponentFixture<BotGame>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BotGame],
    }).compileComponents();

    fixture = TestBed.createComponent(BotGame);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
