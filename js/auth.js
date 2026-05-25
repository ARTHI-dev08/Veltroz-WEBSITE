const API_BASE = 'http://localhost:8081/api';

async function apiAuthFetch(path, options = {}) {
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  };
  if (options.token !== false) {
    const token = getAuthToken();
    if (token) {
      headers.Authorization = `Bearer ${token}`;
    }
  }

  const response = await fetch(`${API_BASE}${path}`, {
    headers,
    ...options,
  });

  if (!response.ok) {
    const errorText = await response.text();
    let message = errorText || `API error ${response.status}`;
    try {
      const parsed = JSON.parse(errorText);
      if (parsed?.message) {
        message = parsed.message;
      } else if (parsed?.error) {
        message = parsed.error;
      }
    } catch (err) {
      // ignore parse errors and use raw text
    }
    throw new Error(message);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

function getAuthToken() {
  return localStorage.getItem('authToken');
}

function getAuthUser() {
  try {
    const raw = localStorage.getItem('authUser');
    return raw ? JSON.parse(raw) : null;
  } catch (err) {
    console.warn('Invalid authUser data stored', err);
    localStorage.removeItem('authUser');
    return null;
  }
}

function saveAuthToken(token) {
  localStorage.setItem('authToken', token);
}

function saveAuthUser(user) {
  if (user && user.username && user.email) {
    localStorage.setItem('authUser', JSON.stringify({ username: user.username, email: user.email }));
  }
}

function clearAuth() {
  localStorage.removeItem('authToken');
  localStorage.removeItem('authUser');
}

function signupUser() {
  const username = document.getElementById('signupUsername').value.trim();
  const email = document.getElementById('signupEmail').value.trim();
  const password = document.getElementById('signupPassword').value;

  if (!username || !email || !password) {
    alert('Please fill all signup fields.');
    return;
  }

  apiAuthFetch('/auth/register', {
    method: 'POST',
    body: JSON.stringify({ username, email, password }),
    token: false,
  })
    .then(response => {
      saveAuthToken(response.token);
      saveAuthUser(response);
      alert('Signup successful. Welcome, ' + response.username + '!');
      window.location.href = 'index.html';
    })
    .catch(error => {
      alert('Signup failed: ' + error.message);
      console.error(error);
    });
}

function loginUser() {
  const email = document.getElementById('loginEmail').value.trim();
  const password = document.getElementById('password').value;

  if (!email || !password) {
    alert('Please enter both email and password.');
    return;
  }

  apiAuthFetch('/auth/login', {
    method: 'POST',
    body: JSON.stringify({ email, password }),
    token: false,
  })
    .then(response => {
      saveAuthToken(response.token);
      saveAuthUser(response);
      alert('Login successful. Welcome back, ' + response.username + '!');
      window.location.href = 'index.html';
    })
    .catch(error => {
      alert('Login failed: ' + error.message);
      console.error(error);
    });
}

function logoutUser() {
  clearAuth();
  window.location.href = 'login.html';
}

function redirectIfLoggedIn() {
  const token = getAuthToken();
  const path = window.location.pathname.toLowerCase();
  const authPages = ['/login.html', '/signup.html'];
  if (token && authPages.some(authPage => path.endsWith(authPage))) {
    window.location.href = 'index.html';
  }
}

function initializeAuthUI() {
  const token = getAuthToken();
  const authUser = getAuthUser();

  const loginBtn = document.querySelector('.login-btn');
  const signupBtn = document.querySelector('.signup-btn');
  const profileMenu = document.getElementById('profileMenu');
  const profileButton = document.getElementById('profileBtn');
  const profileInitial = document.getElementById('profileInitial');
  const profileName = document.getElementById('profileName');
  const profileEmail = document.getElementById('profileEmail');

  if (token && profileMenu) {
    if (loginBtn) loginBtn.style.display = 'none';
    if (signupBtn) signupBtn.style.display = 'none';
    profileMenu.style.display = 'flex';

    const displayName = authUser?.username || 'Member';
    const displayEmail = authUser?.email || '';
    if (profileInitial) profileInitial.textContent = displayName.charAt(0).toUpperCase();
    if (profileName) profileName.textContent = displayName;
    if (profileEmail) profileEmail.textContent = displayEmail;
  } else {
    if (profileMenu) profileMenu.style.display = 'none';
    if (loginBtn) loginBtn.style.display = 'inline-flex';
    if (signupBtn) signupBtn.style.display = 'inline-flex';
  }
}

function toggleProfileMenu() {
  const dropdown = document.getElementById('profileDropdown');
  if (!dropdown) return;
  dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
}

window.addEventListener('DOMContentLoaded', () => {
  redirectIfLoggedIn();
  initializeAuthUI();
});
