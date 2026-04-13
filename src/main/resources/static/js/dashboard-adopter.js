const user = JSON.parse(localStorage.getItem('user'));
if (!user || user.userType !== 'ADOPTER') {
    window.location.href = 'login.html';
}

// Global functions for the buttons
window.logout = function() {
    localStorage.removeItem('user');
    window.location.href = 'login.html';
};

window.showSection = function(id) {
    document.querySelectorAll('.dash-section').forEach(s => s.style.display = 'none');
    document.getElementById(`${id}-section`).style.display = 'block';
    if (id === 'pets') loadPets();
    if (id === 'appointments') loadMyAppointments();
    if (id === 'queries') loadQueries();
};

window.openBooking = function(id, name) {
    document.getElementById('booking-pet-id').value = id;
    document.getElementById('booking-title').innerText = `Meet ${name}`;
    document.getElementById('booking-modal').style.display = 'flex';
};

window.closeModal = function(id) {
    document.getElementById(id).style.display = 'none';
};

async function loadPets() {
    const grid = document.getElementById('pet-grid');
    try {
        const response = await fetch('/pets');
        const pets = await response.json();
        grid.innerHTML = pets.map(pet => `
            <div class="glass-panel pet-card">
                <div class="pet-image">🐾</div>
                <div class="pet-info">
                    <span class="pet-tag">${pet.species}</span>
                    <h3>${pet.name}</h3>
                    <p>${pet.age} years • ${pet.vaccinationStatus ? 'Vaccinated' : 'Not Vaccinated'}</p>
                    <button class="btn btn-primary" onclick="openBooking(${pet.petId}, '${pet.name}')" style="margin-top:1rem; width:100%">Adopt Me</button>
                </div>
            </div>
        `).join('');
    } catch (e) { grid.innerHTML = '<p>Error loading pets.</p>'; }
}

async function loadMyAppointments() {
    const list = document.getElementById('appointment-list');
    const response = await fetch('/appointments/all');
    const all = await response.json();
    const mine = all.filter(a => a.user.userId === user.userId);
    list.innerHTML = mine.length ? mine.map(a => `
        <div class="glass-panel">
            <h3>Meeting with ${a.pet.name}</h3>
            <p>Date: ${a.date} | Time: ${a.time}</p>
            <p>Status: <span style="color:var(--accent)">${a.status}</span></p>
            <button class="btn" onclick="cancelApp(${a.appointmentId})" style="background:#ef4444; margin-top:1rem">Cancel</button>
        </div>
    `).join('') : '<p>No appointments yet.</p>';
}

window.cancelApp = async function(id) {
    if(confirm('Cancel appointment?')) {
        await fetch(`/appointments/${id}`, { method: 'DELETE' });
        loadMyAppointments();
    }
}

async function loadQueries() {
    const list = document.getElementById('query-list');
    const response = await fetch('/queries/all');
    const queries = await response.json();
    const mine = queries.filter(q => q.user.userId === user.userId);
    list.innerHTML = mine.length ? mine.map(q => `
        <div class="glass-panel" style="margin-bottom:1rem">
            <p><strong>Q:</strong> ${q.text}</p>
            <p><strong>A:</strong> ${q.answer || '<em>Waiting for response...</em>'}</p>
        </div>
    `).join('') : '<p>No questions asked yet.</p>';
}

document.getElementById('booking-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
        petId: document.getElementById('booking-pet-id').value,
        userId: user.userId,
        date: document.getElementById('book-date').value,
        time: document.getElementById('book-time').value
    };
    const response = await fetch('/appointments/book', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(payload)
    });
    if (response.ok) {
        alert('Booked!');
        closeModal('booking-modal');
    }
});

document.getElementById('ask-query-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const text = document.getElementById('query-text').value;
    if(!text) return;
    const response = await fetch('/queries/ask', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({ userId: user.userId, text: text })
    });
    if (response.ok) {
        document.getElementById('query-text').value = '';
        loadQueries();
    }
});

// Initialization
document.getElementById('user-display').innerText = `Hello, ${user.name}`;
loadPets();
