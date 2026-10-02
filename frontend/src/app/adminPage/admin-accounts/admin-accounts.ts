import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { User } from '../../models/user';
import { AdminService } from '../../services/admin-service';
import { Message } from '../../models/message';
import { AdminNavBar } from '../admin-nav-bar/admin-nav-bar';

@Component({
  selector: 'app-admin-accounts',
  standalone: true,
  imports: [CommonModule, FormsModule, AdminNavBar],
  templateUrl: './admin-accounts.html',
  styleUrl: './admin-accounts.css'
})
export class AdminAccounts implements OnInit {
  private adminService = inject(AdminService);

  users: User[] = [];
  editingUser: User | null = null;
  message: Message | null = null;

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.adminService.viewAllAccounts().subscribe({
      next: (data) => this.users = data,
      error: (err) => console.error('Failed to fetch users', err)
    });
  }

  startEdit(user: User) {
    this.editingUser = { ...user };
  }

  cancelEdit() {
    this.editingUser = null;
  }

  saveEdit() {
    if (!this.editingUser) return;

    this.adminService.updateUser(this.editingUser).subscribe({
      next: (res) => {
        this.message = res;
        if (res.success) {
          this.editingUser = null;
          this.loadUsers();
        }
      },
      error: (err) => console.error('Failed to update user', err)
    });
  }

  deleteUser(userId: number) {
    if (!confirm('Are you sure you want to delete this account? This action cannot be undone.')) return;

    this.adminService.deleteUser(userId).subscribe({
      next: (res) => {
        this.message = res;
        if (res.success) this.loadUsers();
      },
      error: (err) => console.error('Failed to delete user', err)
    });
  }
}