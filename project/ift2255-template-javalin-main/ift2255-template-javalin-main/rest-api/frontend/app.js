
const API_URL = 'http://localhost:3000';

async function loadUsers() {
    try {
        const response = await fetch(`${API_URL}/students`);

        if (!response.ok) {
            throw new Error('Erreur lors du chargement des utilisateurs');
        }

        const students = await response.json();
        displayUsers(students);
    } catch (error) {
        showError('users-list', error.message);
    }
}

function displayUsers(students) {
    const container = document.getElementById('users-list');

    if (students.length === 0) {
        container.innerHTML = '<p>Aucun utilisateur trouvé.</p>';
        return;
    }

    container.innerHTML = students.map(student => `
        <div class="user-card">
            <h3>${student.name}</h3>
            <p>Programme: ${student.program}</p>
            <p>Matricule: ${student.matricule}</p>
            <p>Cours complétés: ${student.completedCourses.join(', ')}</p>
        </div>
    `).join('');
}

async function loadCourse() {
    const courseId = document.getElementById('courseId').value.trim();

    if (!courseId) {
        showError('course-info', 'Veuillez entrer un ID de cours');
        return;
    }

    try {
        const response = await fetch(`${API_URL}/courses/${courseId}?include_schedule=true&schedule_semester=a25`);

        if (!response.ok) {
            throw new Error('Cours non trouve');
        }

        const course = await response.json();
        displayCourse(course);
    } catch (error) {
        showError('course-info', error.message);
    }
}


function displayCourse(course) {
    const container = document.getElementById('course-info');

    container.innerHTML = `
        <div class="course-card">
            <h3>${course.name || course.id}</h3>
            <p>cours offer: ${course.id}</p>
            <p>Description: ${course.description || 'Pas de description'}</p>
            <p>prerequis: ???</p>
        </div>
    `;
}

document.getElementById('createUserForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const name = document.getElementById('userName').value;
    const matricule = document.getElementById('userMatricule').value;

    try {
        const response = await fetch(`${API_URL}/students`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name, matricule })
        });

        if (!response.ok) {
            throw new Error('Erreur lors de la creation');
        }

        const newUser = await response.json();
        showSuccess('createUserForm', `Utilisateur ${newUser.name} créé avec succès!`);

        document.getElementById('createUserForm').reset();
        loadUsers();
    } catch (error) {
        showError('createUserForm', error.message);
    }
});


function showError(containerId, message) {
    const container = document.getElementById(containerId);
    container.innerHTML = `<div class="error"> ${message}</div>`;
}

function showSuccess(elementId, message) {
    const element = document.getElementById(elementId);
    const successDiv = document.createElement('div');
    successDiv.className = 'success';
    successDiv.textContent = ` ${message}`;
    element.parentNode.insertBefore(successDiv, element.nextSibling);

    setTimeout(() => successDiv.remove(), 3000);
}


window.addEventListener('load', () => {
    loadUsers();
});