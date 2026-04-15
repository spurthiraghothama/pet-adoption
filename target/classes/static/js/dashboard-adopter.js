const user = JSON.parse(localStorage.getItem('user'));

window.showSection = function(id) {
    document.querySelectorAll('.dash-section').forEach(s => s.style.display = 'none');
    const target = document.getElementById(`${id}-section`);
    if (target) {
        target.style.display = 'block';
        if (id === 'pets') loadPets();
        if (id === 'appointments') loadMyAppointments();
        if (id === 'queries') loadQueries();
        if (id === 'adoptions') loadMyAdoptions();
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

        const mine = all
            .filter(a => a.user && a.user.userId === user.userId)
            .map(a => ({
                ...a,
                status: (a.status || "").trim().toUpperCase()
            }));

        list.innerHTML = mine.length ? mine.map(a => `

            <div class="glass-panel animate-in"
                style="margin-bottom:1rem; display:flex; justify-content:space-between; align-items:center;">

                <div>
                    <span class="status-tag"
                        style="background:${getStatusColor(a.status)}; color:white">
                        ${a.status}
                    </span>

                    <h3 style="margin-top:0.5rem">
                        Meeting with ${a.pet ? a.pet.name : 'Unknown Pet'}
                    </h3>

                    <p style="color:var(--text-muted)">
                        Date: ${a.date} | Time: ${a.time}
                    </p>

                    ${a.status === 'EXPIRED' ? `
                        <p style="color:#ff7675; font-weight:600; margin-top:0.5rem">
                            This visit has expired.
                        </p>
                    ` : ''}

                </div>

                <div style="display:flex; flex-direction:column; gap:0.5rem; align-items:flex-end">

                    ${a.status === 'PENDING' ? `
                        <button class="btn"
                            onclick="cancelApp(${a.appointmentId})"
                            style="background:#ff7675; color:white; padding:0.5rem 1rem; font-size:0.8rem">
                            Cancel
                        </button>
                    ` : ''}

                    ${a.status === 'COMPLETED' && a.pet && a.pet.availabilityStatus !== 'ADOPTED' ? `
                        <button class="btn btn-primary"
                            onclick="adoptPet(${a.pet.petId})">
                            Adopt
                        </button>

                        <button class="btn btn-outline"
                            onclick="notAdopt()">
                            Not Interested
                        </button>
                    ` : a.status === 'COMPLETED' ? `
                        <span style="color:#55efc4; font-size:0.85rem; font-weight:600;">✅ Adopted</span>
                    ` : ''}

                    ${a.status === 'EXPIRED' ? `
                        <span style="color:var(--text-muted); font-size:0.8rem">
                            No actions available
                        </span>
                    ` : ''}

                </div>
            </div>

        `).join('') : '<p>No appointments booked yet.</p>';

    } catch (e) {
        console.error("Failed to load appointments", e);
        list.innerHTML = '<p>Error loading appointments.</p>';
    }
}


function getStatusColor(status) {
    status = (status || "").toUpperCase();

    if (status === 'COMPLETED') return '#55efc4';
    if (status === 'EXPIRED') return '#ff7675';
    if (status === 'PENDING') return '#fdcb6e';

    return '#ccc';
}

function notAdopt() {
    showSection('pets');
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

async function adoptPet(petId) {
    try {
        const response = await fetch('/adoption/create', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                petId,
                adopterId: user.userId
            })
        });

        if (!response.ok) {
            const msg = await response.text();
            alert("Adoption failed: " + msg);
            return;
        }

        alert("🎉 Congratulations! Adoption successful!");
        showSection('adoptions');

    } catch (err) {
        console.error(err);
        alert("Server error during adoption");
    }
}

async function loadMyAdoptions() {
    const list = document.getElementById('adoption-list');
    list.innerHTML = '<p>Loading...</p>';
    try {
        const response = await fetch(`/adoption/my/${user.userId}`);
        const adoptions = await response.json();

        if (!adoptions.length) {
            list.innerHTML = '<p>You have not adopted any pets yet.</p>';
            return;
        }

        list.innerHTML = `
            <table style="width:100%; border-collapse:collapse; background:white; border-radius:12px; overflow:hidden; box-shadow:0 2px 12px rgba(0,0,0,0.06);">
                <thead>
                    <tr style="background:var(--primary, #f0c040); text-align:left;">
                        <th style="padding:1rem 1.2rem;">Pet</th>
                        <th style="padding:1rem 1.2rem;">Species</th>
                        <th style="padding:1rem 1.2rem;">Adoption Date</th>
                        <th style="padding:1rem 1.2rem;">Status</th>
                    </tr>
                </thead>
                <tbody>
                    ${adoptions.map((a, i) => `
                        <tr style="border-top:1px solid #f0f0f0; background:${i % 2 === 0 ? 'white' : '#fafafa'}">
                            <td style="padding:1rem 1.2rem; font-weight:600;">${a.pet ? a.pet.name : '—'}</td>
                            <td style="padding:1rem 1.2rem; color:var(--text-muted, #888);">${a.pet ? a.pet.species : '—'}</td>
                            <td style="padding:1rem 1.2rem;">${a.adoptionDate ? new Date(a.adoptionDate).toLocaleDateString() : '—'}</td>
                            <td style="padding:1rem 1.2rem;">
                                <span style="background:#55efc4; color:#1a6650; padding:0.3rem 0.8rem; border-radius:20px; font-size:0.8rem; font-weight:700;">
                                    ${a.status || 'ACTIVE'}
                                </span>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        `;
    } catch (e) {
        console.error("Failed to load adoptions", e);
        list.innerHTML = '<p>Error loading adoptions.</p>';
    }
}



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