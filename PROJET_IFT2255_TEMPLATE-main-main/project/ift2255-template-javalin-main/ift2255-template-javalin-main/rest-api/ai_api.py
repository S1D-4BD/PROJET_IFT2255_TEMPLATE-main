from flask import Flask, request, jsonify
from flask_cors import CORS
from langchain_ollama import OllamaLLM
from langchain_core.prompts import ChatPromptTemplate
import csv
import json

app = Flask(__name__)
CORS(app)

def load_academic_data():
    cours = {}
    with open('../rest-api/data/historique_cours_prog_117510.csv', 'r', encoding='utf-8') as f:
        reader = csv.DictReader(f)
        for row in reader:
            cours[row['sigle']] = {
                'nom': row['nom'],
                'moyenne': row['moyenne'],
                'score': float(row['score']),
                'participants': int(row['participants'])
            }
    return cours

def load_avis_data():
    avis = {}
    with open('../rest-api/data/avis.json', 'r', encoding='utf-8') as f:
        data = json.load(f)
        for review in data:
            course_id = review['courseId'].upper()
            if course_id not in avis:
                avis[course_id] = []
            avis[course_id].append({
                'rating': review.get('rating'),
                'difficulty': review.get('difficulty'),
                'charge': review.get('charge'),
                'comment': review.get('comment', '')
            })
    return avis

def get_avis_stats(avis_data):
    stats = {}
    for course_id, reviews in avis_data.items():
        if len(reviews) > 0:
            stats[course_id] = {
                'count': len(reviews),
                'avg_rating': round(sum(r['rating'] for r in reviews) / len(reviews), 2),
                'avg_difficulty': round(sum(r['difficulty'] for r in reviews) / len(reviews), 2),
                'avg_charge': round(sum(r['charge'] for r in reviews) / len(reviews), 2)
            }
    return stats


COURS_DATA = load_academic_data()
AVIS_DATA = load_avis_data()
AVIS_STATS = get_avis_stats(AVIS_DATA)

def format_cours():
    lines = []
    for sigle, info in COURS_DATA.items():
        lines.append(f"- {sigle}: {info['nom']} | Moyenne: {info['moyenne']} | Score: {info['score']} | Participants: {info['participants']}")
    return "\n".join(lines)

def format_avis():
    lines = []
    for course_id, reviews in AVIS_DATA.items():
        for r in reviews:
            comment = r['comment'] if r['comment'] else "Pas de commentaire"
            lines.append(f"- {course_id}: Note {r['rating']}/5, Difficulté {r['difficulty']}/5, Charge {r['charge']}/5 | Commentaire: {comment}")
    return "\n".join(lines)

def format_stats():
    lines = []
    for course_id, stats in AVIS_STATS.items():
        lines.append(f"- {course_id}: {stats['count']} avis | Note moy: {stats['avg_rating']}/5 | Difficulté moy: {stats['avg_difficulty']}/5 | Charge moy: {stats['avg_charge']}/5")
    return "\n".join(lines)


ALL_COURS = format_cours()
ALL_AVIS = format_avis()
ALL_STATS = format_stats()

template = """
Tu es un conseiller académique pour l'Université de Montréal, programme d'informatique.
Tu dois OBLIGATOIREMENT seulement utiliser les données dessous pour répondre

=== RÉSULTATS ACADÉMIQUES ===
{cours_data}

=== AVIS ÉTUDIANTS (commentaires) ===
{avis_data}

=== STATISTIQUES DES AVIS ===
{stats_data}

=== RÈGLES ===
1) Réponds UNIQUEMENT avec les données fournies ci-dessus
2) Si un cours n'est pas dans les données, dis "Je n'ai pas d'information sur ce cours"
3) Score >= 4, donc c'est un cours facile, Score <= 2.5, donc c'est un cours difficile
4) Sois concis, max 3 phrases

Historique: {context}

Question: {question}

Réponse:
"""

model = OllamaLLM(model="llama3.2:3b")
prompt = ChatPromptTemplate.from_template(template)
chain = prompt | model

conversations = {}

@app.route('/chat', methods=['POST'])
def chat():
    data = request.json
    user_message = data.get('message', '')
    session_id = data.get('session_id', 'default')

    context = conversations.get(session_id, '')

    response = chain.invoke({
        "cours_data": ALL_COURS,
        "avis_data": ALL_AVIS,
        "stats_data": ALL_STATS,
        "context": context,
        "question": user_message
    })

    conversations[session_id] = context + f"\nUser: {user_message}\nBot: {response}"

    return jsonify({"response": response})

if __name__ == '__main__':
    app.run(port=5000)