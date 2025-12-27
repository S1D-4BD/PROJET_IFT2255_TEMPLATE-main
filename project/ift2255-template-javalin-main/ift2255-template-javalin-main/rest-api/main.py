## jai suivi ce tuto :https://www.youtube.com/watch?v=d0o89z134CQ

##run this fir : $ source chatbot/Scripts/activate pour l'environment
##basically on feed tout le temps le contxte de la convo au model pr pas faire de la recorrection et ajustement, et il va repondre a ce tempate de prompt

###########################
from langchain_ollama import OllamaLLM
from langchain_core.prompts import ChatPromptTemplate
import csv
import requests

API_URL = "http://localhost:3000"

def load_academic_data():
    cours = {}
    with open('data/historique_cours_prog_117510.csv', 'r', encoding='utf-8') as f:
        reader = csv.DictReader(f)
        for row in reader:
            cours[row['sigle']] = {
                'nom': row['nom'],
                'moyenne': row['moyenne'],
                'score': float(row['score']),
                'participants': int(row['participants']),
                'trimestres': int(row['trimestres'])
            }
    return cours

COURS_DATA = load_academic_data()


def get_cours_summary():
    ## ca c'Est 100% ift3700 qui ma fait regurgiter tt ca, omg NHLproject + dev 6 flashbacks

    faciles = sorted(COURS_DATA.items(), key=lambda x: x[1]['score'], reverse=True)[:5]
    difficiles = sorted(COURS_DATA.items(), key=lambda x: x[1]['score'])[:5]

    summary = "COURS FACILES (meilleures moyennes):\n"
    for sigle, data in faciles:
        summary += f"- {sigle}: {data['nom']} (moyenne: {data['moyenne']}, score: {data['score']})\n"

    summary += "\nCOURS DIFFICILES (moyennes basses):\n"
    for sigle, data in difficiles:
        summary += f"- {sigle}: {data['nom']} (moyenne: {data['moyenne']}, score: {data['score']})\n"

    return summary

template = """
Tu es un conseiller académique travaillant pour l'université de montréal, pour des étudiants.
Tu aides les étudiants à choisir leurs cours pour leur faciliter au max la vie.

Voici des données sur les cours:
{cours_data}

Historique de la conversation: {context}

Question de l'étudiant: {question}

Réponds en français, sois utile et concis, tourne pas autour du pot:
"""

model = OllamaLLM(model="llama3.2:3b")
prompt = ChatPromptTemplate.from_template(template)
chain = prompt | model

def get_cours_info(sigle):
    """Cherche un cours spécifique"""
    sigle = sigle.upper()
    if sigle in COURS_DATA:
        data = COURS_DATA[sigle]
        return f"{sigle} - {data['nom']}: moyenne {data['moyenne']}, score {data['score']}, {data['participants']} participants"
    return None

def handle_conversation():
    context = ""
    cours_summary = get_cours_summary()

    print("Bienvenue! Je suis ton conseiller académique.")
    print("Pose-moi des questions sur les cours. Tape 'exit' pour quitter.\n")

    while True:
        user_input = input("Toi: ")
        if user_input.lower() == "exit":
            break

        # Enrichir le contexte si l'utilisateur mentionne un sigle de cours
        extra_info = ""
        for sigle in COURS_DATA.keys():
            if sigle.lower() in user_input.lower():
                info = get_cours_info(sigle)
                if info:
                    extra_info += f"\n[INFO: {info}]"

        result = chain.invoke({
            "cours_data": cours_summary + extra_info,
            "context": context,
            "question": user_input
        })

        print("Bot:", result)
        context += f"\nÉtudiant: {user_input}\nConseiller: {result}"

if __name__ == "__main__":
    handle_conversation()