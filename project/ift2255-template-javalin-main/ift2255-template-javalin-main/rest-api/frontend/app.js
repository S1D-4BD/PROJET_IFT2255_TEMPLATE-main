
const API_URL = 'http://localhost:3000';
/////
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
            <h3><b>${student.name}</b></h3>
            <p><b>Programme:</b> ${student.program}</p>
            <p><b>Matricule:</b> ${student.matricule}</p>
            <p><b>Cours complétés:</b> ${student.completedCourses.join(', ')}</p>
        </div>
    `).join('');
}

/**async function loadCourse() {
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
        console.log(course)
        displayCourse(course);
    } catch (error) {
        showError('course-info', error.message);
    }
}*/
async function loadCourseExact() {
    const courseId = document.getElementById('courseId').value.trim().toUpperCase();

    if (!courseId) {
        showError('course-info', 'Veuillez entrer un sigle de cours');
        return;
    }

    try {
        const response = await fetch(`${API_URL}/courses/${courseId}`);
        if (!response.ok) throw new Error('Cours non trouvé');

        const course = await response.json();
        displayCourse(course);

    } catch (error) {
        showError('course-info', error.message);
    }
}

async function loadCourseByKeyword() {
    const query = document.getElementById('courseId').value.trim().toUpperCase();

    if (!query) {
        showError('course-info', 'Veuillez entrer un sigle ou un mot-clé');
        return;
    }

    try {
        // On récupère tous les cours et on filtre côté front
        const response = await fetch(`${API_URL}/courses`);
        if (!response.ok) throw new Error('Impossible de charger les cours');

        const allCourses = await response.json();

        const filteredCourses = allCourses.filter(c =>
            c.id.toUpperCase().includes(query) ||
            (c.name && c.name.toUpperCase().includes(query))
        );

        if (filteredCourses.length === 0) {
            showError('course-info', 'Aucun cours trouvé');
            return;
        }

        displayCourses(filteredCourses);

    } catch (error) {
        showError('course-info', error.message);
    }
}


function displayCourses(courses) {
    const container = document.getElementById('course-info');
    container.innerHTML = "";

    courses.forEach(course => {
        container.innerHTML += `
            <div class="course-card">
                <h3>${course.id} - ${course.name}</h3>
                <p><b>Crédits:</b> ${course.credits}</p>
                <p>${course.description || ""}</p>
            </div>
        `;
    });
}


function displayCourse(course) {
    const container = document.getElementById('course-info');

    let professors = [];
    if (course.schedules?.[0]?.sections) {
        course.schedules[0].sections.forEach(section => {

            if (section.teachers) {
                section.teachers.forEach(teacher => {

                    if (!professors.includes(teacher)) {
                        professors.push(teacher);
                    }
                });
            }
        });
    }
    let professorText = professors.length > 0 ? professors.join('; ') : "Non assigné";  //mdr easy shit, juste cnocatener par ; each professor, sinn juste write nn assigned

    let prereqs = "Aucun";
    if (course.prerequisite_courses && course.prerequisite_courses.length > 0) {
        prereqs = course.prerequisite_courses.join(', ');
    }

    let commentsHtml = '';
    /*if (course.comments && course.comments.length > 0) {
        commentsHtml = '<div class="comments-section">';
        commentsHtml += '<h3>Commentaires des étudiants</h3>';
        course.comments.forEach(c => {
            commentsHtml += `
                <div class="comment">
                    ${c.author}: ${c.message}
                </div>
            `;
        });
        commentsHtml += '</div>';
    } */
   if (course.comments && course.comments.length > 0) {
        commentsHtml = '<div class="comments-section">';
        commentsHtml += '<h3>Commentaires des étudiants</h3>';
        course.comments.forEach(c => {

            /* VERSION AVEC SENTIMENT
            let sentimentEmoji = '';
            if (c.sentiment === 'positif') sentimentEmoji = '++';
            else if (c.sentiment === 'negatif') sentimentEmoji = '--';
            else sentimentEmoji = '+/-';

            commentsHtml += `
                <div class="comment">
                    <p><b>${c.author}</b> ${sentimentEmoji}</p>
                    <p>${c.message || ''}</p>
                </div>
            `;
            */

            // VERSION SANS LES SENTIMENTS
            commentsHtml += `
                <div class="comment">
                    <p><b>${c.author}</b></p>
                    <p>${c.message || ''}</p>
                </div>
            `;
        });
        commentsHtml += '</div>';
    }

    container.innerHTML = `
        <div class="course-card">
            <h3>${course.name || course.id}</h3>
            <p><b>Sigle</b>: ${course.id}</p>
            <p><b>Crédits</b>: ${course.credits || 3}</p>
            <p><b>Professeur(s)</b>: ${professorText}</p>
            <p><b>Prérequis</b>: ${prereqs}</p>
            <p><b>Description</b>: ${course.description || 'Pas de description'}</p>
            ${commentsHtml}
        </div>
    `;
}


/*
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
});*/


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



///////////////////////AVIS LOGIC//////////33



async function loadAvis() {
    const courseId = document.getElementById('avis-search-coursebyid').value.trim();

    if (!courseId) {
        showError('avis-list', 'Veuillez entrer un sigle de cour');
        return;
    }

    try {
        const response = await fetch(`${API_URL}/avis/${courseId}`);
        const avis = await response.json();
        displayAvis(avis);
    } catch (error) {
        showError('avis-list', error.message);
    }
}

async function loadAvisStats() {
    const courseId = document.getElementById('avis-search-coursebyid').value.trim();

    if (!courseId) {
        showError('avis-list', 'Veuillez entrer un sigle de cours');
        return;
    }

    try {
        const response = await fetch(`${API_URL}/avis/${courseId}/stats`);
        const stats = await response.json();
        displayAvisStats(stats, courseId);
    } catch (error) {
        showError('avis-list', error.message);
    }
}

function displayAvis(avisList) {
    const container = document.getElementById('avis-list');

    if (avisList.length === 0) {
        container.innerHTML = '<p>aucun avis pour ce cours</p>';
        return;
    }

    container.innerHTML = '';

    let count = 1;
    for (let avis of avisList) {
        let commentaire = avis.comment || '';


        container.innerHTML += `
            <div class="comment">
                <h4>review# ${count}</h4>
                <p><b>Note:</b> ${avis.rating}/5 | <b>Difficulté:</b> ${avis.difficulty}/5 | <b>Charge:</b> ${avis.charge}/5</p>
                <p>--${commentaire}</p>
            </div>
        `;
        count = count+1
    }
}

function displayAvisStats(stats, courseId) {
    const container = document.getElementById('avis-list');

    container.innerHTML = `
        <div class="course-card">
            <h3>Stats pour ${courseId.toUpperCase()}</h3>
            <p><b>Nombre d'avis:</b> ${stats.count}</p>
            <p><b>Note moyenne:</b> ${stats.avg_rating}/5</p>
            <p><b>Difficulté moyenne:</b> ${stats.avg_difficulty}/5</p>
            <p><b>Charge moyenne:</b> ${stats.avg_charge}/5</p>
        </div>
    `;
}

document.getElementById('createAvisForm').addEventListener('submit', async (e) => {


    e.preventDefault();

    const avis = {
        courseId: document.getElementById('avis-courseId').value.trim().toUpperCase(),
        rating: parseInt(document.getElementById('avis-rating').value),
        difficulty: parseInt(document.getElementById('avis-difficulty').value),
        charge: parseInt(document.getElementById('avis-charge').value),
        comment: document.getElementById('avis-comment').value
    };

    try {
        const response = await fetch(`${API_URL}/avis`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(avis)
        });

        if (!response.ok) {
            throw new Error('Erreur lors de la création');
        }


        document.getElementById('avis-courseId').value = '';
        document.getElementById('avis-rating').value = '';
        document.getElementById('avis-difficulty').value = '';
        document.getElementById('avis-charge').value = '';
        document.getElementById('avis-comment').value = '';
        //shit was annoying a chq fois reset me sort de la page en effacant uri tbrnk
        document.getElementById('avis-form-result').innerHTML = '<div class="success">Avis enregistre</div>';
        //document.getElementById('createAvisForm').reset();
    } catch (error) {
        showError('avis-form-result', error.message);
    }
});


window.addEventListener('load', () => {
    //loadUsers();
});