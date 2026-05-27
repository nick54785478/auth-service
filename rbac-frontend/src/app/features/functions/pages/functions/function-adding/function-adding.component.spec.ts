import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FunctionAddingComponent } from './function-adding.component';

describe('FunctionAddingComponent', () => {
  let component: FunctionAddingComponent;
  let fixture: ComponentFixture<FunctionAddingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FunctionAddingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FunctionAddingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
