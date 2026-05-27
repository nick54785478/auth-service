import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoleAddingComponent } from './role-adding.component';

describe('RoleAddingComponent', () => {
  let component: RoleAddingComponent;
  let fixture: ComponentFixture<RoleAddingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoleAddingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoleAddingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
