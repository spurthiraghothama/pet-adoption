const staffUser = JSON.parse(localStorage.getItem('user'));

window.showSection = function(id) {
    document.querySelectorAll('.dash-section').forEach(s => s.style.display = 'none');
    document.getElementById(`${id}-section`).style.display = 'block';

    // Toggle active button style
    document.querySelectorAll('.dash-nav button').forEach(btn => {
        if (btn.getAttribute('onclick').includes(`'${id}'`)) {
            btn.classList.remove('btn-outline');
            btn.classList.add('btn-primary');
        } else {
            btn.classList.remove('btn-primary');
            btn.classList.add('btn-outline');
        }
    });

    if (id === 'manage-pets') loadStaffPets();
    if (id === 'manage-apps') loadStaffApps();
    if (id === 'queries') loadStaffQueries();
    if (id === 'tasks') loadStaffTasks();
    if (id === 'volunteers') loadVolunteerStatus();
    if (id === 'pending-approvals') loadPendingApprovals();
    if (id === 'adopted-pets') loadAdoptedPets();
};

async function loadStaffPets() {
    const grid = document.getElementById('staff-pet-grid');
    const response = await fetch('/pets/all');
    const pets = await response.json();
    // Show: staff-added pets (no registeredByType) that are not ADOPTED,
    // plus breeder/volunteer pets that are AVAILABLE (approved) and not ADOPTED
    const current = pets.filter(p => {
        if (p.availabilityStatus === 'ADOPTED') return false;
        if (!p.registeredByType) return true; // added directly by staff
        return p.availabilityStatus === 'AVAILABLE'; // breeder/volunteer only if approved
    });
    grid.innerHTML = current.map(pet => `
        <div class="pet-card animate-in">
            <img src="${pet.imageUrl || 'img/pet.png'}" alt="${pet.name}">
            <div class="pet-info">
                <span class="pet-tag">${pet.species}</span>
                <h3>${pet.name}</h3>
                <p style="color:var(--text-muted); font-size:0.85rem">Age: ${pet.age} | ${pet.healthStatus || 'Healthy'}</p>
                <p style="font-size:0.8rem; margin-top:0.5rem">Vacc: ${pet.vaccinationStatus ? '✅' : '❌'}</p>
                <p style="font-size:0.75rem; color:var(--text-muted); margin-top:0.3rem">${pet.registeredByType ? '📋 ' + pet.registeredByType : '🏠 Shelter'}</p>
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
    const vetList = document.getElementById('staff-vet-list');
    const visitorList = document.getElementById('staff-visitor-list');
    
    // Safety check in case we switched sections too fast
    if (!vetList || !visitorList) return;

    try {
        const response = await fetch('/appointments/all');
        const apps = await response.json();

        const vetApps = apps.filter(a => a.appointmentType && a.appointmentType.toUpperCase() === 'VET_CHECK');
        const visitorApps = apps.filter(a => !a.appointmentType || a.appointmentType.toUpperCase() !== 'VET_CHECK');

        function renderApp(a) {
            return `
                <div class="glass-panel animate-in"
                     style="margin-bottom:1rem; display:flex; justify-content:space-between; align-items:center;">

                    <div>
                        <span class="status-tag"
                              style="background:${(a.appointmentType || 'VISIT').toUpperCase() === 'VET_CHECK' ? '#818cf8' : '#dfa164'}; color:white">
                            ${a.appointmentType || 'VISIT'}
                        </span>

                        <h3 style="margin-top:0.5rem">
                            ${a.pet ? a.pet.name : 'Unknown Pet'} with ${a.user ? a.user.name : 'Unknown User'}
                        </h3>

                        <p style="color:var(--text-muted)">${a.date} | ${a.time}</p>
                        <p>Status: <strong>${a.status}</strong></p>

                        ${a.status === 'APPROVED' ? `
                            <div style="display:flex; gap:0.5rem; margin-top:0.5rem">
                                <button class="btn btn-primary"
                                        onclick="markVisit(${a.appointmentId}, 'complete')">
                                    Complete
                                </button>

                                <button class="btn btn-outline"
                                        onclick="markVisit(${a.appointmentId}, 'expire')">
                                    Expire
                                </button>
                            </div>
                        ` : ''}
                    </div>

                    ${a.status === 'PENDING' ? `
                        <div style="display:flex; gap:0.5rem">
                            <button class="btn btn-primary"
                                    onclick="updateAppStatus(${a.appointmentId}, 'APPROVED')">
                                Approve
                            </button>

                            <button class="btn btn-outline"
                                    onclick="updateAppStatus(${a.appointmentId}, 'REJECTED')">
                                Reject
                            </button>
                        </div>
                    ` : ''}
                </div>
            `;
        }

        vetList.innerHTML = vetApps.length ? vetApps.map(renderApp).join('') : '<p style="color:var(--text-muted); font-style:italic">No pending vet evaluations.</p>';
        visitorList.innerHTML = visitorApps.length ? visitorApps.map(renderApp).join('') : '<p style="color:var(--text-muted); font-style:italic">No visiting appointments scheduled.</p>';
    } catch (err) {
        console.error("Failed to load appointments:", err);
    }
}

window.toggleCategory = function(listId, chevronId) {
    const list = document.getElementById(listId);
    const chevron = document.getElementById(chevronId);
    if (list.style.display === 'none') {
        list.style.display = 'block';
        chevron.style.transform = 'rotate(0deg)';
    } else {
        list.style.display = 'none';
        chevron.style.transform = 'rotate(-90deg)';
    }
};


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
            <p style="color:var(--text-muted)">
                ${t.volunteerName ? `<strong>${t.volunteerName}</strong>` : '<em>Not yet accepted</em>'}
            </p>
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

async function loadVolunteerStatus() {
    const response = await fetch('/users/volunteers/status');
    const volunteers = await response.json();
    const list = document.getElementById('volunteer-status-list');
    
    list.innerHTML = volunteers.length ? volunteers.map(v => `
        <div class="glass-panel animate-in" style="margin-bottom:1rem; display:flex; justify-content:space-between; align-items:center;">
            <div>
                <h3>${v.name}</h3>
                <p style="color:var(--text-muted); font-size:0.9rem">${v.email}</p>
                <p style="color:var(--text-muted); font-size:0.85rem">Phone: ${v.phone || 'N/A'}</p>
            </div>
            <div style="text-align: right;">
                <span class="status-tag" style="background:${v.availabilityStatus ? '#55efc4' : '#ff7675'}; color:${v.availabilityStatus ? 'black' : 'white'}; font-size:0.9rem; padding:0.5rem 1.2rem">
                    ${v.availabilityStatus ? '✅ Available' : '⏸️ Unavailable'}
                </span>
            </div>
        </div>
    `).join('') : '<p>No volunteers registered.</p>';
}

async function markVisit(id, action) {
    await fetch(`/appointments/${id}/${action}`, {
        method: 'PATCH'
    });
    loadStaffApps();
}

document.getElementById('add-task-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
        description: document.getElementById('task-desc').value,
        specifications: document.getElementById('task-specs').value || "",
        volunteerId: null  // Always null since it's publish to all
    };
    await fetch('/tasks/create', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(payload)
    });
    document.getElementById('task-modal').style.display = 'none';
    loadStaffTasks();
});


async function loadPendingApprovals() {
    const list = document.getElementById('pending-approvals-list');
    const res = await fetch('/pets/pending-review');
    const pets = await res.json();
    list.innerHTML = pets.length ? pets.map(p => `
        <div class="glass-panel animate-in" style="margin-bottom:1rem; display:flex; justify-content:space-between; align-items:center;">
            <div style="display:flex; gap:1.5rem; align-items:center;">
                <img src="${p.imageUrl || 'img/pet.png'}" style="width:70px; height:70px; border-radius:12px; object-fit:cover;">
                <div>
                    <span class="status-tag" style="background:#f1c40f; color:black; font-size:0.75rem;">
                        ${p.registeredByType || 'EXTERNAL'}
                    </span>
                    <h3 style="margin-top:0.4rem">${p.name}</h3>
                    <p style="color:var(--text-muted); font-size:0.85rem">${p.species} | Age: ${p.age} | ${p.healthStatus || 'Healthy'}</p>
                    <p style="font-size:0.8rem; color:var(--text-muted)">Status: <strong>${p.availabilityStatus}</strong></p>
                </div>
            </div>
            <div style="display:flex; gap:0.5rem;">
                <button class="btn btn-primary" onclick="approvePet(${p.petId})">✅ Approve</button>
                <button class="btn btn-outline" onclick="rejectPet(${p.petId})">❌ Reject</button>
            </div>
        </div>
    `).join('') : '<p style="color:var(--text-muted)">No pets pending review.</p>';
}

async function approvePet(id) {
    await fetch(`/pets/${id}/approve`, { method: 'PATCH' });
    loadPendingApprovals();
}

async function rejectPet(id) {
    await fetch(`/pets/${id}/reject`, { method: 'PATCH' });
    loadPendingApprovals();
}


async function loadAdoptedPets() {
    const grid = document.getElementById('adopted-pet-grid');
    const response = await fetch('/pets/all');
    const pets = await response.json();
    const adopted = pets.filter(p => p.availabilityStatus === 'ADOPTED');
    grid.innerHTML = adopted.length ? adopted.map(pet => `
        <div class="pet-card animate-in">
            <img src="${pet.imageUrl || 'img/pet.png'}" alt="${pet.name}">
            <div class="pet-info">
                <span class="pet-tag">${pet.species}</span>
                <h3>${pet.name}</h3>
                <p style="color:var(--text-muted); font-size:0.85rem">Age: ${pet.age} | ${pet.healthStatus || 'Healthy'}</p>
                <span class="status-tag" style="background:#a29bfe; color:black; margin-top:0.5rem; display:inline-block;">ADOPTED</span>
            </div>
        </div>
    `).join('') : '<p style="color:var(--text-muted)">No adopted pets yet.</p>';
}

// Init
showSection('manage-pets');