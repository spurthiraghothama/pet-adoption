const apiBase = ''; // Current host
let currentUser = null;

// Modal Logic
function openModal(id) {
    document.getElementById(id).style.display = 'flex';
}

function closeModal(id) {
    document.getElementById(id).style.display = 'none';
}

// Initial Data Loading
async function loadPets() {
    const grid = document.getElementById('pet-grid');
    try {
        const response = await fetch(`${apiBase}/pets`);
        const pets = await response.json();
        
        if (pets.length === 0) {
            grid.innerHTML = '<div class="hero" style="grid-column: 1/-1;"><p>No pets available at the moment. Check back later!</p></div>';
            return;
        }

        grid.innerHTML = pets.map(pet => `
            <div class="glass-panel pet-card">
                <div class="pet-image">${getPetEmoji(pet.species)}</div>
                <div class="pet-info">
                    <span class="pet-tag">${pet.species}</span>
                    <h3>${pet.name}</h3>
                    <p style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1rem;">
                        ${pet.age} years old • ${pet.vaccinationStatus ? 'Vaccinated' : 'Not Vaccinated'}
                    </p>
                    <button class="btn btn-primary" onclick="initiateBooking(${pet.petId}, '${pet.name}')" ${!currentUser ? 'disabled title="Please register first"' : ''}>
                        Meet ${pet.name}
                    </button>
                </div>
            </div>
        `).join('');
    } catch (error) {
        console.error('Error loading pets:', error);
        grid.innerHTML = '<p>Failed to load pets. Is the server running?</p>';
    }
}

function getPetEmoji(species) {
    const s = species.toLowerCase();
    if (s.includes('cat')) return '🐱';
    if (s.includes('dog')) return '🐶';
    if (s.includes('bird')) return '🦜';
    return '🐾';
}

// Registration
document.getElementById('register-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
        name: document.getElementById('reg-name').value,
        email: document.getElementById('reg-email').value,
        phone: document.getElementById('reg-phone').value,
        userType: 'ADOPTER'
    };

    try {
        const response = await fetch(`${apiBase}/users/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        currentUser = await response.json();
        
        document.getElementById('user-nav').innerHTML = `
            <div style="display: flex; align-items: center; gap: 1rem;">
                <span>Welcome, <strong>${currentUser.name}</strong></span>
                <div style="width: 40px; height: 40px; border-radius: 50%; background: var(--accent); display: flex; align-items: center; justify-content: center; font-weight: bold;">
                    ${currentUser.name[0]}
                </div>
            </div>
        `;
        
        document.getElementById('user-status').innerText = 'Ready to Adopt!';
        closeModal('register-modal');
        loadPets(); // Refresh buttons
    } catch (error) {
        alert('Registration failed. Check console.');
    }
});

// Booking
function initiateBooking(id, name) {
    if (!currentUser) return;
    document.getElementById('booking-pet-id').value = id;
    document.getElementById('booking-title').innerText = `Meet ${name}`;
    openModal('booking-modal');
}

document.getElementById('booking-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
        petId: document.getElementById('booking-pet-id').value,
        userId: currentUser.userId,
        date: document.getElementById('book-date').value,
        time: document.getElementById('book-time').value
    };

    try {
        const response = await fetch(`${apiBase}/appointments/book`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        
        if (response.ok) {
            alert('Appointment booked successfully! Our staff will contact you shortly.');
            closeModal('booking-modal');
        }
    } catch (error) {
        alert('Booking failed.');
    }
});

// Initialize
loadPets();
