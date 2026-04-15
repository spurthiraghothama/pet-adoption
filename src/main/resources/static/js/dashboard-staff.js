const staffUser = JSON.parse(localStorage.getItem('user'));

window.showSection = function(id) {
    document.querySelectorAll('.dash-section').forEach(s => s.style.display = 'none');
    document.getElementById(`${id}-section`).style.display = 'block';
    if (id === 'manage-pets') loadStaffPets();
    if (id === 'manage-apps') loadStaffApps();
    if (id === 'queries') loadStaffQueries();
    if (id === 'tasks') { loadStaffTasks(); loadVolunteers(); }
};

async function loadStaffPets() {
    const grid = document.getElementById('staff-pet-grid');
    const response = await fetch('/pets/all');
    const pets = await response.json();
    grid.innerHTML = pets.map(pet => `
        <div class="pet-card animate-in">
            <img src="${pet.imageUrl || 'img/pet.png'}" alt="${pet.name}">
            <div class="pet-info">
                <span class="pet-tag">${pet.species}</span>
                <h3>${pet.name}</h3>
                <p style="color:var(--text-muted); font-size:0.85rem">Age: ${pet.age} | ${pet.healthStatus || 'Healthy'}</p>
                <p style="font-size:0.8rem; margin-top:0.5rem">Vacc: ${pet.vaccinationStatus ? '✅' : '❌'}</p>
                <div style="display:flex; gap:0.5rem; margin-top:1.5rem">
                    <button class="btn btn-primary" style="flex:1; font-size:0.75rem; padding:0.5rem;" onclick="openVetCheck(${pet.petId})">Vet Check</button>
                    <button class="btn btn-outline" style="flex:1; font-size:0.75rem; padding:0.5rem;" onclick="toggleVacc(${pet.petId}, ${!pet.vaccinationStatus})">Update Vacc</button>
                </div>
            </div>
        </div>
    `).join('');
}

function openVetCheck(id) {
    document.getElementById('vet-pet-id').value = id;
    document.getElementById('vet-check-modal').style.display = 'flex';
}

document.getElementById('vet-check-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
        petId: document.getElementById('vet-pet-id').value,
        userId: staffUser.userId,
        date: document.getElementById('vet-date').value,
        time: document.getElementById('vet-time').value,
        status: 'PENDING',
        appointmentType: 'VET_CHECK'
    };
    await fetch('/appointments/book', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(payload)
    });
    alert('Vet Check Appointment Created!');
    document.getElementById('vet-check-modal').style.display = 'none';
    loadStaffApps();
});

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
        healthStatus: document.getElementById('pet-health').value,
        imageUrl: document.getElementById('pet-img').value || 'img/pet.png',
        vaccinationStatus: document.getElementById('pet-vacc').value === 'true',
        availabilityStatus: 'AVAILABLE'
    };
    await fetch('/pets/add', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(payload)
    });
    document.getElementById('pet-modal').style.display = 'none';
    loadStaffPets();
});

async function loadStaffApps() {
    const list = document.getElementById('staff-app-list');
    const response = await fetch('/appointments/all');
    const apps = await response.json();
    list.innerHTML = apps.map(a => `
        <div class="glass-panel animate-in" style="margin-bottom:1rem; display:flex; justify-content:space-between; align-items:center;">
            <div>
                <span class="status-tag" style="background:${a.appointmentType==='VET_CHECK'?'#818cf8':'#dfa164'}; color:white">${a.appointmentType || 'VISIT'}</span>
                <h3 style="margin-top:0.5rem">${a.pet ? a.pet.name : 'Unknown Pet'} with ${a.user ? a.user.name : 'Unknown User'}</h3>
                <p style="color:var(--text-muted)">${a.date} | ${a.time}</p>
                <p>Status: <strong>${a.status}</strong></p>
            </div>
            ${a.status === 'PENDING' ? `
                <div style="display:flex; gap:0.5rem">
                    <button class="btn btn-primary" onclick="updateAppStatus(${a.appointmentId}, 'APPROVED')">Approve</button>
                    <button class="btn btn-outline" style="border-color:#ff7675; color:#ff7675" onclick="updateAppStatus(${a.appointmentId}, 'REJECTED')">Reject</button>
                </div>
            ` : ''}
        </div>
    `).join('');
}

async function updateAppStatus(id, status) {
    await fetch(`/appointments/${id}/status?status=${status}`, { method: 'PATCH' });
    loadStaffApps();
}

async function loadStaffQueries() {
    const list = document.getElementById('staff-query-list');
    const response = await fetch('/queries/all');
    const queries = await response.json();
    const pending = queries.filter(q => !q.answer);
    list.innerHTML = pending.length ? pending.map(q => `
        <div class="glass-panel animate-in" style="margin-bottom:1rem">
            <p style="font-weight:600; color:var(--accent-orange)">Query from ${q.user ? q.user.name : 'Guest'}:</p>
            <p>${q.text}</p>
            <button class="btn btn-primary" style="margin-top:1rem; font-size:0.8rem" onclick="openRespModal(${q.questionId})">Answer Inquirer</button>
        </div>
    `).join('') : '<p>No pending inquiries.</p>';
}

function openRespModal(id) {
    document.getElementById('resp-query-id').value = id;
    document.getElementById('response-modal').style.display = 'flex';
}

document.getElementById('response-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('resp-query-id').value;
    const ans = document.getElementById('resp-text').value;
    await fetch(`/queries/respond?questionId=${id}&answer=${ans}`, { method: 'POST' });
    document.getElementById('response-modal').style.display = 'none';
    loadStaffQueries();
});

async function loadStaffTasks() {
    const response = await fetch('/tasks');
    const tasks = await response.json();
    const list = document.getElementById('staff-task-list');
    list.innerHTML = tasks.length ? tasks.map(t => `
        <div class="glass-panel animate-in" style="margin-bottom:1rem">
            <span class="status-tag" style="background:${t.status==='COMPLETED'?'#55efc4':'var(--primary)'}; color:black">${t.status}</span>
            <h3 style="margin-top:0.5rem">${t.description}</h3>
            <p style="color:var(--text-muted)">Assigned to: <strong>${t.volunteer ? t.volunteer.name : 'Unassigned'}</strong></p>
            <p style="color:var(--text-muted); font-size:0.85rem">Specs: ${t.specifications || 'Standard procedure'}</p>

            <div style="margin-top:1rem; display:flex; gap:0.5rem">
                ${t.status === 'DRAFTED' ? `
                    <button class="btn btn-primary" onclick="publishTask(${t.taskId})">Publish</button>
                ` : ''}

                ${t.status === 'MONITORED' ? `
                    <button class="btn btn-primary" onclick="reviewTask(${t.taskId}, true)">Approve</button>
                    <button class="btn btn-outline" onclick="reviewTask(${t.taskId}, false)">Reject</button>
                ` : ''}
            </div>
        </div>
    `).join('') : '<p>No tasks created yet.</p>';
}

async function publishTask(taskId) {
    await fetch(`/tasks/${taskId}/publish`, { method: 'PATCH' });
    loadStaffTasks();
}

async function reviewTask(taskId, approved) {
    await fetch(`/tasks/${taskId}/review?approved=${approved}`, { method: 'PATCH' });
    loadStaffTasks();
}

async function loadVolunteers() {
    const res = await fetch('/users/volunteers');
    const vols = await res.json();
    const select = document.getElementById('task-vol');
    select.innerHTML = '<option value="">-- Unassigned --</option>' + 
        vols.map(v => `<option value="${v.userId}">${v.name}</option>`).join('');
}

document.getElementById('add-task-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
        description: document.getElementById('task-desc').value,
        specifications: document.getElementById('task-specs').value,
        volunteerId: document.getElementById('task-vol').value || null
    };
    await fetch('/tasks/create', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(payload)
    });
    document.getElementById('task-modal').style.display = 'none';
    loadStaffTasks();
});

// Init
showSection('manage-pets');