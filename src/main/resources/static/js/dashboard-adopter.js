const user = JSON.parse(localStorage.getItem('user'));

window.showSection = function(id) {
    document.querySelectorAll('.dash-section').forEach(s => s.style.display = 'none');
    const target = document.getElementById(`${id}-section`);
    if (target) {
        target.style.display = 'block';
        if (id === 'pets') loadPets();
        if (id === 'appointments') loadMyAppointments();
        if (id === 'queries') loadQueries();
    }
};

window.openBooking = function(id, name) {
    document.getElementById('booking-pet-id').value = id;
    document.getElementById('booking-title').innerText = `Visit for ${name}`;
    document.getElementById('booking-modal').style.display = 'flex';
};

window.closeModal = function(id) {
    document.getElementById(id).style.display = 'none';
};

async function loadPets() {
    const grid = document.getElementById('pet-grid');
    try {
        const response = await fetch('/pets/all');
        const pets = await response.json();
        const available = pets.filter(p => p.availabilityStatus === 'AVAILABLE');
        grid.innerHTML = available.map(pet => `
            <div class="pet-card animate-in">
                <img src="${pet.imageUrl || 'img/pet.png'}" alt="${pet.name}">
                <div class="pet-info">
                    <span class="pet-tag">${pet.species}</span>
                    <h3>${pet.name}</h3>
                    <p style="color:var(--text-muted); font-size:0.9rem; margin-bottom:1rem;">Age: ${pet.age} • ${pet.healthStatus || 'Healthy'}</p>
                    <div style="display:flex; justify-content:space-between; align-items:center;">
                        <span style="font-size:0.8rem">${pet.vaccinationStatus ? '✅ Vaccinated' : '❌ No Vacc'}</span>
                        <button class="btn btn-primary" onclick="openBooking(${pet.petId}, '${pet.name}')" style="padding:0.5rem 1rem; font-size:0.85rem">Adopt ➜</button>
                    </div>
                </div>
            </div>
        `).join('');
    } catch (e) { 
        console.error("Failed to load pets", e);
        grid.innerHTML = '<p>Oops! Failed to load companions.</p>'; 
    }
}

async function loadMyAppointments() {
    const list = document.getElementById('appointment-list');
    try {
        const response = await fetch('/appointments/all');
        const all = await response.json();
        const mine = all.filter(a => a.user && a.user.userId === user.userId);
        list.innerHTML = mine.length ? mine.map(a => `
            <div class="glass-panel animate-in" style="margin-bottom:1rem; display:flex; justify-content:space-between; align-items:center;">
                <div>
                    <span class="status-tag" style="background:${getStatusColor(a.status)}; color:white">${a.status}</span>
                    <h3 style="margin-top:0.5rem">Meeting with ${a.pet ? a.pet.name : 'Unknown Pet'}</h3>
                    <p style="color:var(--text-muted)">Date: ${a.date} | Time: ${a.time}</p>
                </div>
                <button class="btn" onclick="cancelApp(${a.appointmentId})" style="background:#ff7675; color:white; padding:0.5rem 1rem; font-size:0.8rem">Cancel</button>
            </div>
        `).join('') : '<p>No appointments booked yet.</p>';
    } catch (e) {
        console.error("Failed to load appointments", e);
        list.innerHTML = '<p>Error loading appointments.</p>';
    }
}

function getStatusColor(status) {
    if (status === 'APPROVED') return '#55efc4';
    if (status === 'REJECTED') return '#ff7675';
    return '#fdcb6e'; // PENDING
}

window.cancelApp = async function(id) {
    if(confirm('Cancel appointment?')) {
        await fetch(`/appointments/${id}`, { method: 'DELETE' });
        loadMyAppointments();
    }
}

async function loadQueries() {
    const list = document.getElementById('query-list');
    try {
        const response = await fetch('/queries/all');
        const queries = await response.json();
        const mine = queries.filter(q => q.user && q.user.userId === user.userId);
        list.innerHTML = mine.length ? mine.map(q => `
            <div class="glass-panel animate-in" style="margin-bottom:1.5rem">
                <p style="font-weight:600; font-size:1.1rem; color:var(--accent-orange)">Question:</p>
                <p style="margin-bottom:1rem">${q.text}</p>
                <div style="padding-top:1rem; border-top:1px solid #eee">
                    <p style="font-weight:600; color:var(--text-muted)">Response:</p>
                    <p>${q.answer || '<span style="color:var(--text-muted); font-style:italic">Pending staff response...</span>'}</p>
                </div>
            </div>
        `).join('') : '<p>No questions asked yet.</p>';
    } catch (e) {
        console.error("Failed to load queries", e);
        list.innerHTML = '<p>Error loading questions.</p>';
    }
}

// FIX: Improved submit for Ask Query
document.getElementById('ask-query-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const textArea = document.getElementById('query-text');
    const text = textArea.value.trim();
    if(!text) return;

    try {
        const response = await fetch('/queries/ask', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ 
                userId: user.userId, 
                text: text 
            })
        });

        if (response.ok) {
            alert('Your question has been sent to our staff!');
            textArea.value = '';
            closeModal('query-modal');
            loadQueries();
        } else {
            const err = await response.text();
            alert('Failed to send question: ' + err);
        }
    } catch (err) {
        console.error("Submission error", err);
        alert('Connection error. Please try again.');
    }
});

document.getElementById('booking-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
        petId: document.getElementById('booking-pet-id').value,
        userId: user.userId,
        date: document.getElementById('book-date').value,
        time: document.getElementById('book-time').value,
        status: 'PENDING',
        appointmentType: 'VISIT'
    };
    await fetch('/appointments/book', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(payload)
    });
    alert('Visit Requested!');
    closeModal('booking-modal');
    loadMyAppointments();
});

// Initialize
if (user) {
    loadPets();
}
