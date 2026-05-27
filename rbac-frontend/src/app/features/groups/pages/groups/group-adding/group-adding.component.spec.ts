import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupAddingComponent } from './group-adding.component';

describe('GroupAddingComponent', () => {
  let component: GroupAddingComponent;
  let fixture: ComponentFixture<GroupAddingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GroupAddingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GroupAddingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
