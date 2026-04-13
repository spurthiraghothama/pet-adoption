async function loadStaffPets() {
    const grid = document.getElementById('staff-pet-grid');
    const response = await fetch('/pets/all');
    const pets = await response.json();
    grid.innerHTML = pets.map(pet => `
        <div class="glass-panel pet-card">
            <div class="pet-info">
                <h3>${pet.name}</h3>
                <p>${pet.species} | Age: ${pet.age}</p>
                <p>Vaccination: ${pet.vaccinationStatus ? '✅' : '❌'}</p>
                <div style="display:flex; gap:0.5rem; margin-top:1rem">
                    <button class="btn btn-primary" onclick="toggleVacc(${pet.petId}, ${!pet.vaccinationStatus})">Toggle Vacc</button>
                </div>
            </div>
        </div>
    `).join('');
}

async function toggleVacc(id, status) {
    await fetch(`/pets/${id}/vaccination?status=${status}`, { method: 'PATCH' });
    loadStaffPets();
}

document.getElementById('add-pet-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
        name: document.getElementById('pet-name').value,
        species: document.getElementById('pet-species').value,
        age: document.getElementById('pet-age').value,
        vaccinationStatus: document.getElementById('pet-vacc').value === 'true',
        availabilityStatus: 'AVAILABLE'
    };
    await fetch('/pets/add', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(payload)
    });
    closeModal('pet-modal');
    loadStaffPets();
});

async function loadStaffApps() {
    const list = document.getElementById('staff-app-list');
    const response = await fetch('/appointments/all');
    const apps = await response.json();
    list.innerHTML = apps.map(a => `
        <div class="glass-panel">
            <h3>${a.pet.name} visit with ${a.user.name}</h3>
            <p>On: ${a.date} at ${a.time}</p>
            <p>ID: ${a.appointmentId} | Contact: ${a.user.phone || 'N/A'}</p>
        </div>
    `).join('');
}

async function loadStaffQueries() {
    const list = document.getElementById('staff-query-list');
    const response = await fetch('/queries/all');
    const queries = await response.json();
    const pending = queries.filter(q => !q.answer);
    list.innerHTML = pending.length ? pending.map(q => `
        <div class="glass-panel" style="margin-bottom:1rem">
            <p><strong>From:</strong> ${q.user.name}</p>
            <p><strong>Quest:</strong> ${q.text}</p>
            <button class="btn btn-primary" onclick="openRespModal(${q.questionId})">Respond</button>
        </div>
    `).join('') : '<p>No pending queries! Good job.</p>';
}

function openRespModal(id) {
    document.getElementById('resp-query-id').value = id;
    openModal('response-modal');
}

document.getElementById('response-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('resp-query-id').value;
    const ans = document.getElementById('resp-text').value;
    await fetch(`/queries/respond?questionId=${id}&answer=${ans}`, { strategy: 'POST', method: 'POST' });
    closeModal('response-modal');
    loadStaffQueries();
});

loadStaffPets();
function openModal(id) { document.getElementById(id).style.display = 'flex'; }
function closeModal(id) { document.getElementById(id).style.display = 'none'; }
