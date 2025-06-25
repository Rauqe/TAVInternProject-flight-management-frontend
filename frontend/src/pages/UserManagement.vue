<template>
  <div class="user-management-container">
    <header class="page-header">
      <h1>User Management</h1>
      <button @click="openCreateModal" class="add-user-btn">
        <span class="icon">+</span> Add New User
      </button>
    </header>

    <div v-if="loading" class="loading-spinner">Loading...</div>
    <div v-if="error" class="error-message">{{ error }}</div>

    <div v-if="!loading && users.length" class="user-table-container">
      <table class="user-table">
        <thead>
          <tr>
            <th>Username</th>
            <th>Role</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <!-- <td>{{ user.id }}</td> -->
            <td>{{ user.username }}</td>
            <td>
              <span :class="['role-badge', user.role.toLowerCase()]">{{ user.role }}</span>
            </td>
            <td class="actions">
              <button class="action-btn edit-btn" title="Edit user" @click="openEditModal(user)">✏️</button>
              <button class="action-btn delete-btn" title="Delete user" @click="handleDeleteUser(user.id)">🗑️</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    
    <div v-if="!loading && !users.length && !error" class="no-users-message">
      No users found.
    </div>

    <div v-if="isModalOpen" class="drawer-backdrop" @click.self="closeCreateModal">
      <div class="drawer">
        <h2>Create New User</h2>
        <form @submit.prevent="handleCreateUser">
          <div class="input-group">
            <label for="new-username">Username</label>
            <input id="new-username" type="text" v-model="newUser.username" required />
          </div>
          <div class="input-group">
            <label for="new-password">Password</label>
            <input id="new-password" type="password" v-model="newUser.password" required />
          </div>
          <div class="input-group">
            <label for="new-role">Role</label>
            <select id="new-role" v-model="newUser.role" required>
              <option value="USER">User</option>
              <option value="ADMIN">Admin</option>
            </select>
          </div>
          <div v-if="modalError" class="error-message">{{ modalError }}</div>
          <div class="modal-actions">
            <button type="button" @click="closeCreateModal" class="btn-secondary">Cancel</button>
            <button type="submit" class="btn-primary" :disabled="isCreating">
              {{ isCreating ? 'Creating...' : 'Create User' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Edit User Modal -->
    <div v-if="isEditModalOpen" class="modal-overlay" @click.self="closeEditModal">
      <div class="modal-content">
        <h2>Edit User</h2>
        <form @submit.prevent="handleUpdateUser">
          <div class="input-group">
            <label for="edit-username">Username</label>
            <input id="edit-username" type="text" v-model="editUserData.username" required />
          </div>
          <div class="input-group">
            <label for="edit-password">Password (leave blank to keep current)</label>
            <input id="edit-password" type="password" v-model="editUserData.password" />
          </div>
          <div class="input-group">
            <label for="edit-role">Role</label>
            <select id="edit-role" v-model="editUserData.role" required>
              <option value="USER">User</option>
              <option value="ADMIN">Admin</option>
            </select>
          </div>
          <div v-if="modalError" class="error-message">{{ modalError }}</div>
          <div class="modal-actions">
            <button type="button" @click="closeEditModal" class="btn-secondary">Cancel</button>
            <button type="submit" class="btn-primary" :disabled="isUpdating">
              {{ isUpdating ? 'Updating...' : 'Update User' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getUsers, createUser, deleteUser, updateUser } from '../services/userService';

const users = ref([]);
const loading = ref(true);
const error = ref('');
const modalError = ref('');

const isModalOpen = ref(false);
const isEditModalOpen = ref(false);
const isCreating = ref(false);
const isUpdating = ref(false);

const newUser = ref({
  username: '',
  password: '',
  role: 'USER',
});
const editUserData = ref({ id: '', username: '', password: '', role: 'USER' });

const fetchUsers = async () => {
  try {
    loading.value = true;
    error.value = '';
    users.value = await getUsers();
  } catch (e) {
    error.value = 'Failed to load users. You may not have permission to view this page.';
  } finally {
    loading.value = false;
  }
};

onMounted(fetchUsers);

const openCreateModal = () => {
  isModalOpen.value = true;
  modalError.value = '';
  newUser.value = { username: '', password: '', role: 'USER' };
};

const closeCreateModal = () => {
  isModalOpen.value = false;
};

const handleCreateUser = async () => {
  isCreating.value = true;
  modalError.value = '';
  try {
    await createUser(newUser.value);
    closeCreateModal();
    await fetchUsers(); // Refresh the user list
  } catch (e) {
    modalError.value = 'Failed to create user. The username might already exist.';
  } finally {
    isCreating.value = false;
  }
};

const openEditModal = (user) => {
  isEditModalOpen.value = true;
  editUserData.value = { ...user, password: '' };
  modalError.value = '';
};

const closeEditModal = () => {
  isEditModalOpen.value = false;
};

const handleUpdateUser = async () => {
  isUpdating.value = true;
  modalError.value = '';
  try {
    await updateUser(editUserData.value.id, editUserData.value);
    closeEditModal();
    await fetchUsers();
  } catch (e) {
    modalError.value = 'Failed to update user.';
  } finally {
    isUpdating.value = false;
  }
};

const handleDeleteUser = async (userId) => {
  if (!confirm('Are you sure you want to delete this user?')) return;
  try {
    await deleteUser(userId);
    await fetchUsers();
  } catch (e) {
    error.value = 'Failed to delete user.';
  }
};
</script>

<style scoped>
.user-management-container {
  padding: 2rem;
  font-family: sans-serif;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}
h1 {
  font-size: 2rem;
  font-weight: bold;
}
.add-user-btn {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 0.8rem 1.2rem;
  border-radius: 8px;
  cursor: pointer;
  font-size: 1rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
.loading-spinner, .error-message, .no-users-message {
  text-align: center;
  padding: 2rem;
  font-size: 1.2rem;
}
.error-message {
  color: #dc3545;
  background-color: #f8d7da;
  border: 1px solid #f5c6cb;
  border-radius: 8px;
}
.user-table-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  overflow: hidden;
}
.user-table {
  width: 100%;
  border-collapse: collapse;
}
.user-table th, .user-table td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid #e9ecef;
}
.user-table th {
  background-color: #f8f9fa;
  font-weight: 600;
}
.role-badge {
  padding: 0.3em 0.6em;
  border-radius: 10px;
  font-weight: bold;
  font-size: 0.8rem;
  text-transform: uppercase;
}
.role-badge.admin {
  background-color: #f8d7da;
  color: #721c24;
}
.role-badge.user {
  background-color: #d1ecf1;
  color: #0c5460;
}
.actions {
  display: flex;
  gap: 0.5rem;
}
.action-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.2rem;
}
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  width: 100%;
  max-width: 500px;
}
.modal-content h2 {
  font-size: 1.5rem;
  margin-bottom: 1.5rem;
}
.input-group {
  margin-bottom: 1rem;
}
.input-group label {
  display: block;
  margin-bottom: 0.5rem;
}
.input-group input, .input-group select {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}
.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 2rem;
}
.btn-primary {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 0.8rem 1.2rem;
  border-radius: 4px;
  cursor: pointer;
}
.btn-primary:disabled {
  background-color: #aaa;
  cursor: not-allowed;
}
.btn-secondary {
  background-color: #6c757d;
  color: white;
  border: none;
  padding: 0.8rem 1.2rem;
  border-radius: 4px;
  cursor: pointer;
}
.drawer-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0,0,0,0.2);
  z-index: 1000;
  display: flex;
  justify-content: flex-end;
}
.drawer {
  background: #fff;
  width: 400px;
  max-width: 90vw;
  height: 100vh;
  box-shadow: -2px 0 16px rgba(0,0,0,0.12);
  padding: 2rem 2rem 2rem 2rem;
  display: flex;
  flex-direction: column;
  animation: slideInDrawer 0.2s;
}
@keyframes slideInDrawer {
  from { transform: translateX(100%); }
  to { transform: translateX(0); }
}
</style> 