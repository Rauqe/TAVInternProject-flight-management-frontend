export async function login(username, password) {
  // Gerçek API ile değiştirilebilir
  if (username === 'admin' && password === 'admin') {
    return 'mock-jwt-token-admin';
  }
  if (username === 'operator' && password === 'operator') {
    return 'mock-jwt-token-operator';
  }
  if (username === 'viewer' && password === 'viewer') {
    return 'mock-jwt-token-viewer';
  }
  throw new Error('Kullanıcı adı veya parola hatalı');
}

export function getUserRole() {
  const token = localStorage.getItem('jwt');
  if (token === 'mock-jwt-token-admin') return 'admin';
  if (token === 'mock-jwt-token-operator') return 'operator';
  if (token === 'mock-jwt-token-viewer') return 'viewer';
  return null;
} 