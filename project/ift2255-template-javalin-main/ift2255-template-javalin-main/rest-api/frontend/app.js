    const API_URL = 'http://localhost:3000';

    let coursAcademiques = {}; //on doit faire ca first sinn ca lag


    /**
     * cette fct va charge les statistiques académiques des cours à partir d’un fichier CSV
     * et les stocke dans la variable globale `coursAcademiques`
     * @async
     * @returns {Promise<void>}
     */
    async function loadCSV() {
    const response = await fetch('../data/historique_cours_prog_117510.csv');
    const text = await response.text();

    const lignes = text.split('\n');

    //header vrm pas besoin de ca
    for (let i = 1; i < lignes.length; i++) {
        const cols = lignes[i].split(',');

        if (cols[0]) {
            const sigle = cols[0].trim().toUpperCase();
            coursAcademiques[sigle] = {
                nom: cols[1],
                moyenne: cols[2],
                score: parseFloat(cols[3]),
                participants: parseInt(cols[4]),
                trimestres: parseInt(cols[5])
            };
        }
    }
    }


    /**
     * ccette fct va charger la liste des étudiants depuis le service
     * @async
     * @returns {Promise<void>}
     */
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


    /**
     * va aficher la liste des étudiants dans l interface
     * @param {Array<Object>} students --> la Liste des étudiants
     */
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

    /**
     * Genere le code de trimestre
     * @returns {string} Code du trimestre
     */
    function getSemesterCode() {
    const term = document.getElementById("term")?.value || "a";
    const year = document.getElementById("year")?.value || "25";
    return `${term}${year}`;
    }

    /**
     * Trouver un cours / des cours en fct du sigle ou sigle partiel
     * @async
     * @returns {Promise<void>}
     */
    async function searchCourse() {
    const input = document.getElementById("courseId").value.trim().toUpperCase();

    if (!input) {
        showError("course-info", "Veuillez entrer un sigle de cours");
        return;
    }


    if (input.length >= 7) { //si on vs pr cherche le cours au complet
        loadCourse();
    } else {
        loadCourseByKeyword(); //sinn fct hamza
    }
    }

    /**
     * Charge un cours pour un trimestre donné
     * et verifie si on est éligible (etudiant)
     * @async
     * @returns {Promise<void>}
     */
    async function loadCourse() {
    const courseId = document.getElementById("courseId").value.trim();
    if (!courseId) {
        showError("course-info", "Veuillez entrer un sigle de cours");
        return;
    }

    const semester = getSemesterCode();

    try {

        const res = await fetch(
            `${API_URL}/courses/${encodeURIComponent(courseId)}?schedule_semester=${encodeURIComponent(semester)}`
        );

        if (!res.ok) throw new Error("Cours non trouvé");

        const course = await res.json();
        const eligibility = await checkEligibility(courseId); ///RAjoute pour regarder si le cours on peut meme penser le prendre
        course.eligibility = eligibility;
        displayCourse(course);
    } catch (e) {
        showError("course-info", e.message);
    }
    }


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
    let professorText = professors.length > 0 ? professors.join('; ') : "Non assigné";

    let prereqs = "Aucun";
    if (course.prerequisite_courses && course.prerequisite_courses.length > 0) {
        prereqs = course.prerequisite_courses.join(', ');
    }

    let commentsHtml = '';
    if (course.comments && course.comments.length > 0) {
        commentsHtml = '<div class="comments-section">';
        commentsHtml += '<h3>Commentaires des étudiants</h3>';
        course.comments.forEach(c => {
            commentsHtml += `
                <div class="comment">
                    <p><b>${c.author}</b></p>
                    <p>${c.message || ''}</p>
                </div>
            `;
        });
        commentsHtml += '</div>';
    }

    let eligibilityHTML = '';
    if (course.eligibility) {
        if (course.eligibility.eligible) {
            eligibilityHTML = '<p class="eligible"> Vous aves tt les prérequis a ce cours</p>';
        } else {
            let missing = course.eligibility.prerequis.filter(p => !course.eligibility.completedCourses.includes(p));
            eligibilityHTML = `<p class="NON-eligible"> Non éligible, prérequis manquants: ${missing.join(', ')}</p>`;
        }
    }

    ////
    let academicHTML = '';
    const stats = coursAcademiques[course.id.toUpperCase()];
    if (stats) {
        academicHTML = `
            <div class="academic-stats">
                <h4>Resultats moyens obtenus</h4>
                <p><b>Moyenne:</b> ${stats.moyenne}</p>
                <p><b>Score:</b> ${stats.score}</p>
                <p><b>Nbr Participants:</b> ${stats.participants} étudiants</p>
                <p><b>Trimestres:</b> ${stats.trimestres}</p>
            </div>
        `;
    } else {
        academicHTML = `
            <div class="academic-stats">
                <p class="error">Aucune donnée académique disponible pour ce cours</p>
            </div>
        `;
    }

    container.innerHTML = `
        <div class="course-card">
            <h3>${course.name || course.id}</h3>
            <p><b>Sigle</b>: ${course.id}</p>
            <p><b>Crédits</b>: ${course.credits || 3}</p>
            <p><b>Professeur(s)</b>: ${professorText}</p>
            <p><b>Prérequis</b>: ${prereqs}</p>
            ${eligibilityHTML}
            ${academicHTML}
            <p><b>Description</b>: ${course.description || 'Pas de description dispo'}</p>
            ${commentsHtml}
        </div>
    `;
    }


    /**
     * Affiche les info en details dun cours:
     * professeurs, prereqs, eligibility, stats académiques
     * et avis étudiants
     * @param {Object} course cours retourné par planifium
     */

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
    let professorText = professors.length > 0 ? professors.join('; ') : "Non assigné";

    let prereqs = "Aucun";
    if (course.prerequisite_courses && course.prerequisite_courses.length > 0) {
        prereqs = course.prerequisite_courses.join(', ');
    }

    let commentsHtml = '';
    if (course.comments && course.comments.length > 0) {
        commentsHtml = '<div class="comments-section">';
        commentsHtml += '<h3>Commentaires des étudiants</h3>';
        course.comments.forEach(c => {
            commentsHtml += `
                <div class="comment">
                    <p><b>${c.author}</b></p>
                    <p>${c.message || ''}</p>
                </div>
            `;
        });
        commentsHtml += '</div>';
    }

    let eligibilityHTML = '';
    if (course.eligibility) {
        if (course.eligibility.eligible) {
            eligibilityHTML = '<p class="eligible"> Vous aves tt les prérequis a ce cours</p>';
        } else {
            let missing = course.eligibility.prerequis.filter(p => !course.eligibility.completedCourses.includes(p));
            eligibilityHTML = `<p class="NON-eligible"> Non éligible, prérequis manquants: ${missing.join(', ')}</p>`;
        }
    }

    ////
    let academicHTML = '';
    const stats = coursAcademiques[course.id.toUpperCase()];
    if (stats) {
        academicHTML = `
            <div class="academic-stats">
                <h4>Resultats moyens obtenus</h4>
                <p><b>Moyenne:</b> ${stats.moyenne}</p>
                <p><b>Score:</b> ${stats.score}</p>
                <p><b>Nbr Participants:</b> ${stats.participants} étudiants</p>
                <p><b>Trimestres:</b> ${stats.trimestres}</p>
            </div>
        `;
    } else {
        academicHTML = `
            <div class="academic-stats">
                <p class="error">Aucune donnée académique disponible pour ce cours</p>
            </div>
        `;
    }

    container.innerHTML = `
        <div class="course-card">
            <h3>${course.name || course.id}</h3>
            <p><b>Sigle</b>: ${course.id}</p>
            <p><b>Crédits</b>: ${course.credits || 3}</p>
            <p><b>Professeur(s)</b>: ${professorText}</p>
            <p><b>Prérequis</b>: ${prereqs}</p>
            ${eligibilityHTML}
            ${academicHTML}
            <p><b>Description</b>: ${course.description || 'Pas de description dispo'}</p>
            ${commentsHtml}
        </div>
    `;
    }

    /**
     * Vérifie si l’etudiant connecté est eligible à un cours donné
     * @param {string} courseId - Sigle du cours
     * @returns {Promise<Object|null>} Resultat d’eligibilité ou null
     * git
     */
    async function checkEligibility(courseId) {
    console.log("checkEligibility appelé avec:", courseId, "matricule:", matricule);

    if (!matricule || !courseId) {
        console.log("matricule ou courseId manquant");
        return null;
    }

    try {
        const url = `${API_URL}/students/${matricule}/eligibility?courseId=${courseId}`;
        console.log("URL appelée:", url);

        const res = await fetch(url);
        console.log("Réponse status:", res.status);

        if (!res.ok) {
            console.log("Réponse non-ok");
            return null;
        }

        const data = await res.json();
        console.log("Data reçue:", data);
        return data;
    } catch (e) {
        console.log("Erreur:", e);
        return null;
    }
    }


    /**
     * ERROR BOX, afficher une erreur au frontend
     * @param {string} containerId -ID du container ou afficher l'erreur
     * @param {string} message - le msg d'erreur
     */
    function showError(containerId, message) {
    const container = document.getElementById(containerId);
    container.innerHTML = `<div class="error">${message}</div>`;
    }

    /**
     * Affiche un message de succès temporaire
     * @param {string} elementId - L'ID de l'élément de référence
     * @param {string} message - Le message de succès
     */
    function showSuccess(elementId, message) {
    const element = document.getElementById(elementId);
    const successDiv = document.createElement('div');
    successDiv.className = 'success';
    successDiv.textContent = message;
    element.parentNode.insertBefore(successDiv, element.nextSibling);

    setTimeout(() => successDiv.remove(), 3000);
    }

    ///////////////////////AVIS LOGIC//////////

    /**
     * Charge les avis pour un cours spécifique
     */
    async function loadAvis() {
    const courseId = document.getElementById('avis-search-coursebyid').value.trim();

    if (!courseId) {
        showError('avis-list', 'Veuillez entrer un sigle de cours');
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

    /**
     * Prepare stats a partir des avis pour un cours
     */
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

    /**
     * Affiche la list des avis
     * @param {Array} avisList - la list des avis
     */
    function displayAvis(avisList) {
    const container = document.getElementById('avis-list');

    if (avisList.length === 0) {
        container.innerHTML = '<p>Aucun avis pour ce cours</p>';
        return;
    }

    container.innerHTML = '';

    let count = 1;
    for (let avis of avisList) {
        let commentaire = avis.comment || '';

        container.innerHTML += `
            <div class="comment">
                <h4>Review #${count}</h4>
                <p><b>Note:</b> ${avis.rating}/5 | <b>Difficulté:</b> ${avis.difficulty}/5 | <b>Charge:</b> ${avis.charge}/5</p>
                <p>${commentaire}</p>
            </div>
        `;
        count++;
    }
    }

    /**
     * Affiche les stats des avis
     * @param {Object} stats - les stats quon calcule a chq souission d'avis
     * @param {string} courseId - le ID du cours en question
     */
    function displayAvisStats(stats, courseId) {
        const container = document.getElementById('avis-list');

        container.innerHTML = `
            <div class="course-card">
                <h3>Stats pour ${courseId.toUpperCase()}</h3>
                <p><b>Nombre d'avis:</b> ${stats.count}</p>
                <p><b>Note moyenne:</b> ${stats.avg_rating}/5</p>
                <p><b>Difficulté moyenne (avis):</b> ${stats.avg_difficulty}/5</p>
                <p><b>Charge moyenne:</b> ${stats.avg_charge}/5</p>
                <p><b>Score académique:</b> ${stats.score_academique || 'N/A'}</p>
            </div>
        `;
    }

    //form pr les avis
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

        //reseting form
        document.getElementById('avis-courseId').value = '';
        document.getElementById('avis-rating').value = '';
        document.getElementById('avis-difficulty').value = '';
        document.getElementById('avis-charge').value = '';
        document.getElementById('avis-comment').value = '';

        document.getElementById('avis-form-result').innerHTML = '<div class="success">Avis enregistré</div>';
    } catch (error) {
        showError('avis-form-result', error.message);
    }
    });



////////////////////////////////////////////////////////////////
//horaire

var couleurs = ['#FFB3BA', '#BAFFC9', '#BAE1FF', '#FFFFBA', '#FFDFBa', '#E0BBE4'];

function getCouleur(courseId) {
    var index = panier.indexOf(courseId);
    return couleurs[index % couleurs.length]; // CYCLIQUE, INCASE ON PERMET D'AJOUTER  + DE COURS , maix 6 c normalement le max
}

var panier = [];
var grille = {
    'Lundi': {},
    'Mardi': {},
    'Mercredi': {},
    'Jeudi': {},
    'Vendredi': {}
};

async function addToHoraire() {
    var courseId = document.getElementById('horaire-courseId').value.toUpperCase();

    if (!courseId) {
        alert('Entre un cours');
        return;
    }
    if (panier.length >= 6) {
        alert('Panier plein (max 6)');
        return;
    }
    if (panier.includes(courseId)) {
        alert('Cours deja dans le panier');
        return;
    }

    panier.push(courseId);
    document.getElementById('horaire-courseId').value = '';
    await refreshHoraire(); // deja ce truc faut le load en demarrage de pag
}

function removeFromHoraire(courseId) {
    panier = panier.filter(c => c !== courseId);
    refreshHoraire();
}

function clearHoraire() {
    panier = [];
    refreshHoraire();
}

async function refreshHoraire() {
    document.getElementById('horaire-count').textContent = panier.length;

    var panierHtml = '';
    for (var i = 0; i < panier.length; i++) {
        panierHtml += '<span>' + panier[i] + ' <button onclick="removeFromHoraire(\'' + panier[i] + '\')">X</button></span> ';
    }
    document.getElementById('horaire-panier').innerHTML = panierHtml || '<p>Panier vide</p>';

    grille = {
        'Lundi': {},
        'Mardi': {},
        'Mercredi': {},
        'Jeudi': {},
        'Vendredi': {}
    };

    for (var i = 0; i < panier.length; i++) {
        await loadCourseSchedule(panier[i]);
    }

    afficherGrille();
}

    async function loadCourseSchedule(courseId) {
        try {

            var term = document.getElementById('horaire-term').value;
            var year = document.getElementById('horaire-year').value;
            var semester = term + year;

            var response = await fetch('http://localhost:3000/courses/' + courseId + '/full?semester=' + semester);
            //var response = await fetch('http://localhost:3000/courses/' + courseId + '/full');
            var data = await response.json();

            var joursMap = {
                'Lu': 'Lundi',
                'Ma': 'Mardi',
                'Me': 'Mercredi',
                'Je': 'Jeudi',
                'Ve': 'Vendredi'
            };

            if (!data.schedules || data.schedules.length === 0) return;

            var schedule = data.schedules[0];
            var section = schedule.sections[0];

            for (var v = 0; v < section.volets.length; v++) {
                var volet = section.volets[v];

                if (volet.name !== 'TH' && volet.name !== 'TP') continue;

                for (var a = 0; a < volet.activities.length; a++) {
                    var activity = volet.activities[a];
                    var debut = activity.start_time;
                    var fin = activity.end_time;
                    //permet de remplir tt les cases occupant la duree d un cours

                    var cases = getCasesEntreHeures(debut, fin);

                    for (var d = 0; d < activity.days.length; d++) {
                        var jourAbr = activity.days[d];
                        var jour = joursMap[jourAbr];

                        if (jour && grille[jour]) {
                            for (var c = 0; c < cases.length; c++) {
                                if (grille[jour][cases[c]]) {
                                    if (!grille[jour][cases[c]].includes(courseId)) { /// SI YA DEJA UN COURS, -> ALORS ON A LE CONLFIT
                                        grille[jour][cases[c]] += ' / ' + courseId; //montrer les 2 cors en conflit
                                    }
                                } else {
                                    grille[jour][cases[c]] = courseId;
                                }
                            }
                        }
                    }
                }
            }
        } catch (e) {
            console.log('Erreur pour ' + courseId, e);
        }
    }

    function getCasesEntreHeures(debut, fin) {
        var heures = ['08:00', '08:30','09:00', '09:30','10:00', '10:30','11:00', '11:30','12:00', '12:30','13:00', '13:30','14:00', '14:30','15:00', '15:30','16:00', '16:30','17:00', '17:30','18:00', '18:30','19:00', '19:30','20:00', '20:30']; //on peut enlever ou ajouter incase, jme sie au centre etudiant

        var result = [];
        var started = false;

        for (var i = 0; i < heures.length; i++) {
            var h = heures[i];

            if (h === debut) {
                started = true;
            }
            if (started) {
                result.push(h);
            }

            if (started && h >= fin) {
                break;
            }
        }

        return result;
    }

function afficherGrille() {
    var heures = ['08:00', '08:30','09:00', '09:30','10:00', '10:30','11:00', '11:30','12:00', '12:30','13:00', '13:30','14:00', '14:30','15:00', '15:30','16:00', '16:30','17:00', '17:30','18:00', '18:30','19:00', '19:30','20:00', '20:30'];
    var jours = ['Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi'];

    var html = '';
    for (var i = 0; i < heures.length; i++) {
        html += '<tr><td>' + heures[i] + '</td>';
        for (var j = 0; j < jours.length; j++) {
            var cours = grille[jours[j]][heures[i]] || '';
            if (cours) {
                html += '<td style="background-color:' + getCouleur(cours) + '">' + cours + '</td>';
            } else {
                html += '<td></td>';
            }
        }
        html += '</tr>';
    }

    document.getElementById('horaire-body').innerHTML = html;
}


    window.addEventListener('load', () => {
    //loadUsers();
    loadCSV();
    refreshHoraire();
    });