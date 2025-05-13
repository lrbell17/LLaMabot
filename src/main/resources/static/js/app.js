const apiBase = '';
let jwtToken = null;
let userId = null;

// Pagination state
let sessionsCurrentPage = 0;
const sessionsPageSize = 10;
let messagesCurrentPage = 0;
const messagesPageSize = 10;
let selectedSessionId = null;

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

// Registration handler
const registerSection = document.getElementById('register-section');
const loginSection = document.getElementById('login-section');
const sessionsSection = document.getElementById('sessions-section');
const messagesSection = document.getElementById('messages-section');

// Register
document.getElementById('register-form').onsubmit = async e => {
  e.preventDefault();
  const form = e.target;
  const data = { username: form.username.value, email: form.email.value, password: form.password.value };
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

// Login
document.getElementById('login-form').onsubmit = async e => {
  e.preventDefault();
  const form = e.target;
  const data = { username: form.username.value, password: form.password.value };
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
    hide(registerSection, loginSection);
    show(sessionsSection);
    loadSessions(0);
  } catch (err) {
    showError(document.getElementById('login-error'), err.message);
  }
};

// New session
document.getElementById('new-session-form').onsubmit = async e => {
  e.preventDefault();
  const msg = e.target.message.value;
  const mutation = `
    mutation($userId: String!, $message: String!) {
      startChatSession(userId: $userId, message: $message) {
        sessionId userId startedAt updatedAt
      }
    }
  `;
  try {
    const res = await fetch(apiBase + '/graphql', {
      method: 'POST',
      headers: { 'Content-Type':'application/json', 'Authorization': `Bearer ${jwtToken}` },
      body: JSON.stringify({ query: mutation, variables: { userId, message: msg } })
    });
    const payload = await res.json();
    if (payload.errors) throw new Error(payload.errors[0].message);
    e.target.reset();
    loadSessions(0);
  } catch (err) {
    showError(document.getElementById('new-session-error'), err.message);
  }
};

// Load sessions
async function loadSessions(page = 0) {
  if (typeof page !== 'number') page = 0;
  sessionsCurrentPage = page;
  hide(messagesSection);
  const query = `
    query($userId: String!, $page: Int!, $size: Int!) {
      getChatSessions(userId: $userId, page: $page, size: $size) {
        sessions { sessionId description startedAt }
        page size totalPages totalElements
      }
    }
  `;
  const res = await fetch(apiBase + '/graphql', {
    method: 'POST',
    headers: { 'Content-Type':'application/json', 'Authorization': 'Bearer '+jwtToken },
    body: JSON.stringify({ query, variables: { userId, page, size: sessionsPageSize } })
  });
  const { data, errors } = await res.json();
  if (errors) return alert('Error loading sessions: ' + errors[0].message);

  const { sessions, page: p, totalPages } = data.getChatSessions;
  const list = document.getElementById('sessions');
  list.innerHTML = '';
  sessions.forEach(s => {
    const li = document.createElement('li');
    li.textContent = s.description;
    li.dataset.sessionId = s.sessionId;
    li.addEventListener('click', () => {
      selectedSessionId = s.sessionId;
      messagesCurrentPage = 0;
      show(messagesSection);
      loadMessages(s.sessionId, 0);
    });
    list.appendChild(li);
  });

  document.getElementById('prev-sessions').disabled = (p === 0);
  document.getElementById('next-sessions').disabled = (p+1 >= totalPages);
  document.getElementById('sessions-page-info').textContent = `Page ${p+1} of ${totalPages}`;
}

// Sessions pagination handlers
document.getElementById('prev-sessions').onclick = () => {
  if (sessionsCurrentPage > 0) loadSessions(sessionsCurrentPage - 1);
};
document.getElementById('next-sessions').onclick = () => {
  loadSessions(sessionsCurrentPage + 1);
};
document.getElementById('refresh-sessions').onclick = () => {
  loadSessions(sessionsCurrentPage);
};

// Load messages
async function loadMessages(sessionId, page = 0) {
  if (typeof page !== 'number') page = 0;
  messagesCurrentPage = page;
  const query = `
    query($sessionId: String!, $userId: String!, $page: Int!, $size: Int!) {
      chatHistory(sessionId: $sessionId, userId: $userId, page: $page, size: $size) {
        messages { timestamp content sender }
        page size totalPages totalElements
      }
    }
  `;
  const res = await fetch(apiBase + '/graphql', {
    method: 'POST',
    headers: { 'Content-Type':'application/json', 'Authorization': 'Bearer '+jwtToken },
    body: JSON.stringify({ query, variables: { sessionId, userId, page, size: messagesPageSize } })
  });
  const { data, errors } = await res.json();
  if (errors) return alert('Error loading messages: ' + errors[0].message);

  const { messages, page: p, totalPages } = data.chatHistory;
  const list = document.getElementById('messages');
  list.innerHTML = '';
  messages.forEach(m => {
    const li = document.createElement('li');
    li.innerHTML = `<strong>[${new Date(m.timestamp).toLocaleTimeString()}]</strong> ${m.sender||'?'}: ${m.content}`;
    list.appendChild(li);
  });

  document.getElementById('prev-messages').disabled = (p === 0);
  document.getElementById('next-messages').disabled = (p+1 >= totalPages);
  document.getElementById('messages-page-info').textContent = `Page ${p+1} of ${totalPages}`;
}

// Messages pagination handlers
document.getElementById('prev-messages').onclick = () => {
  if (messagesCurrentPage > 0 && selectedSessionId) {
    loadMessages(selectedSessionId, messagesCurrentPage - 1);
  }
};
document.getElementById('next-messages').onclick = () => {
  if (selectedSessionId) {
    loadMessages(selectedSessionId, messagesCurrentPage + 1);
  }
};
