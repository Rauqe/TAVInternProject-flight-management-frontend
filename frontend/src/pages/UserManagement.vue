<template>
  <div class="user-management-container">
    <header class="page-header">
      <h1>User Management</h1>
      <el-button type="primary" @click="openCreateDrawer">
        Add New User
      </el-button>
    </header>

    <div v-if="loading" class="loading-spinner">Loading...</div>
    <div v-if="error" class="error-message">{{ error }}</div>

    <div v-if="!loading && users.length" class="user-table-container">
      <el-table :data="users" style="width: 100%">
        <el-table-column prop="username" label="Username" />
        <el-table-column prop="role" label="Role">
          <template #default="scope">
            <el-tag :type="scope.row.role === 'ADMIN' ? 'danger' : 'info'">{{ scope.row.role }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="160">
          <template #default="scope">
            <el-button size="small" @click="openEditDrawer(scope.row)">Edit</el-button>
            <el-button size="small" type="danger" @click="handleDeleteUser(scope.row.id)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <div v-if="!loading && !users.length && !error" class="no-users-message">
      No users found.
    </div>

    <!-- Add User Drawer -->
    <el-drawer
      v-model="isCreateDrawerOpen"
      title="Add New User"
      direction="rtl"
      :size="drawerSize"
      :show-close="false"
    >
      <el-form
        ref="createFormRef"
        :model="newUser"
        :rules="userRules"
        label-width="120px"
        @submit.prevent="handleCreateUser"
      >
        <el-form-item label="Username" prop="username">
          <el-input v-model="newUser.username" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Password" prop="password">
          <el-input v-model="newUser.password" type="password" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Role" prop="role">
          <el-select v-model="newUser.role" placeholder="Select Role">
            <el-option label="User" value="USER" />
            <el-option label="Admin" value="ADMIN" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button @click="closeCreateDrawer">Cancel</el-button>
          <el-button type="primary" @click="handleCreateUser" :loading="isCreating">Create</el-button>
        </el-form-item>
        <div v-if="modalError" class="error-message">{{ modalError }}</div>
      </el-form>
    </el-drawer>

    <!-- Edit User Drawer -->
    <el-drawer
      v-model="isEditDrawerOpen"
      title="Edit User"
      direction="rtl"
      :size="drawerSize"
      :show-close="false"
    >
      <el-form
        ref="editFormRef"
        :model="editUserData"
        :rules="userRulesEdit"
        label-width="120px"
        @submit.prevent="handleUpdateUser"
      >
        <el-form-item label="Username" prop="username">
          <el-input v-model="editUserData.username" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Password" prop="password">
          <el-input v-model="editUserData.password" type="password" autocomplete="off" placeholder="Leave blank to keep current" />
        </el-form-item>
        <el-form-item label="Role" prop="role">
          <el-select v-model="editUserData.role" placeholder="Select Role">
            <el-option label="User" value="USER" />
            <el-option label="Admin" value="ADMIN" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button @click="closeEditDrawer">Cancel</el-button>
          <el-button type="primary" @click="handleUpdateUser" :loading="isUpdating">Update</el-button>
        </el-form-item>
        <div v-if="modalError" class="error-message">{{ modalError }}</div>
      </el-form>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { getUsers, createUser, deleteUser, updateUser } from '../services/userService';

const users = ref([]);
const loading = ref(true);
const error = ref('');
const modalError = ref('');

const isCreateDrawerOpen = ref(false);
const isEditDrawerOpen = ref(false);
const isCreating = ref(false);
const isUpdating = ref(false);

const createFormRef = ref();
const editFormRef = ref();

const drawerSize = computed(() => window.innerWidth < 768 ? '100%' : '40%');

const newUser = ref({
  username: '',
  password: '',
  role: 'USER',
});
const editUserData = ref({ id: '', username: '', password: '', role: 'USER' });

const userRules = {
  username: [
    { required: true, message: 'Username is required', trigger: 'blur' },
    { min: 3, message: 'Username must be at least 3 characters', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Password is required', trigger: 'blur' },
    { min: 4, message: 'Password must be at least 4 characters', trigger: 'blur' }
  ],
  role: [
    { required: true, message: 'Role is required', trigger: 'change' }
  ]
};
const userRulesEdit = {
  username: userRules.username,
  password: [
    { min: 0, message: 'Password must be at least 4 characters', trigger: 'blur' }
  ],
  role: userRules.role
};

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

function openCreateDrawer() {
  isCreateDrawerOpen.value = true;
  modalError.value = '';
  newUser.value = { username: '', password: '', role: 'USER' };
}
function closeCreateDrawer() {
  isCreateDrawerOpen.value = false;
}
async function handleCreateUser() {
  if (!createFormRef.value) return;
  try {
    await createFormRef.value.validate();
    isCreating.value = true;
    await createUser(newUser.value);
    closeCreateDrawer();
    await fetchUsers();
    ElMessage.success('User created successfully!');
  } catch (e) {
    modalError.value = 'Failed to create user. The username might already exist.';
  } finally {
    isCreating.value = false;
  }
}

function openEditDrawer(user) {
  isEditDrawerOpen.value = true;
  editUserData.value = { ...user, password: '' };
  modalError.value = '';
}
function closeEditDrawer() {
  isEditDrawerOpen.value = false;
}
async function handleUpdateUser() {
  if (!editFormRef.value) return;
  try {
    await editFormRef.value.validate();
    isUpdating.value = true;
    await updateUser(editUserData.value.id, editUserData.value);
    closeEditDrawer();
    await fetchUsers();
    ElMessage.success('User updated successfully!');
  } catch (e) {
    modalError.value = 'Failed to update user.';
  } finally {
    isUpdating.value = false;
  }
}

async function handleDeleteUser(userId) {
  try {
    await deleteUser(userId);
    await fetchUsers();
    ElMessage.success('User deleted successfully!');
  } catch (e) {
    error.value = 'Failed to delete user.';
  }
}
</script>

<style scoped>
.user-management-container {
  padding: 30px;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}
.no-users-message {
  margin-top: 2rem;
  color: #888;
  text-align: center;
}
.error-message {
  color: #f56c6c;
  margin: 1rem 0;
}
.loading-spinner {
  text-align: center;
  margin: 2rem 0;
}
</style> 