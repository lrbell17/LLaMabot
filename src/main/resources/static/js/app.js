const apiBase = '';
let jwtToken = null;
let userId = null;

// Helpers
function showError(el, msg) {
  el.textContent = msg;
}
function hide(...els) {
  els.forEach(e => e.classList.add('hidden'));
}
function show(...els) {
  els.forEach(e => e.classList.remove('hidden'));
}

// User registration handler
document.getElementById('register-form').onsubmit = async e => {
  e.preventDefault();
  const form = e.target;
  const data = {
    username: form.username.value,
    email: form.email.value,
    password: form.password.value
  };
  try {
    const res = await fetch(apiBase + '/api/auth/register', {
      method: 'POST',
      headers: { 'Content-Type':'application/json' },
      body: JSON.stringify(data)
    });
    if (!res.ok) {
      const err = await res.json();
      throw new Error(err.error || 'Registration failed');
    }
    alert('Registration successful—please log in.');
    form.reset();
  } catch (err) {
    showError(document.getElementById('register-error'), err.message);
  }
};

// Login handler
document.getElementById('login-form').onsubmit = async e => {
  e.preventDefault();
  const form = e.target;
  const data = {
    username: form.username.value,
    password: form.password.value
  };
  try {
    const res = await fetch(apiBase + '/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type':'application/json' },
      body: JSON.stringify(data)
    });
    if (!res.ok) {
      const err = await res.json();
      throw new Error(err.error || 'Login failed');
    }
    const body = await res.json();
    jwtToken = body.token;
    userId = body.userId;
    form.reset();
    hide(document.getElementById('register-section'),
         document.getElementById('login-section'));
    show(document.getElementById('sessions-section'));
    loadSessions();
  } catch (err) {
    showError(document.getElementById('login-error'), err.message);
  }
};

// Add new session handler
document.getElementById('new-session-form').onsubmit = async e => {
    const newSessionMutation = `
      mutation($userId: String!) {
        startChatSession(userId: $userId) {
          sessionId
          userId
          startedAt
          updatedAt
        }
      }
    `;
  e.preventDefault();
  const desc = e.target.description.value;
  try {
    const res = await fetch(`${apiBase}/graphql`, {
      method: 'POST',
      headers: { 'Content-Type':'application/json', 'Authorization': `Bearer ${jwtToken}` },
      body: JSON.stringify({ query: newSessionMutation, variables: { userId, description: desc } })
    });
    const payload = await res.json();
    if (payload.errors) {
      throw new Error(payload.errors[0].message);
    }
    e.target.reset();
    loadSessions();
  } catch (err) {
    showError(document.getElementById('new-session-error'), err.message);
  }
};

// Load sessions
async function loadSessions() {
  const query = `
      query($userId: String!, $page: Int!, $size: Int!) {
        getChatSessions(userId: $userId, page: $page, size: $size) {
          sessions { sessionId userId startedAt updatedAt }
          page size totalPages totalElements
        }
      }
    `;
  const res = await fetch(apiBase + '/graphql', {
    method: 'POST',
    headers: {
      'Content-Type':'application/json',
      'Authorization': 'Bearer ' + jwtToken
    },
     body: JSON.stringify({
        query,
        variables: { userId, page: 0, size: 10 }
     })
  });
  const payload = await res.json();
  if (payload.errors) {
    alert('Error loading sessions: ' + payload.errors[0].message);
    return;
  }
  const list = document.getElementById('sessions');
  list.innerHTML = '';
  for (const s of payload.data.getChatSessions.sessions) {
    const li = document.createElement('li');
    li.textContent = `${s.sessionId} (started ${new Date(s.startedAt).toLocaleString()}, updated ${new Date(s.updatedAt).toLocaleString()})`;
    list.appendChild(li);
  }
}

document.getElementById('refresh-sessions').onclick = loadSessions;
