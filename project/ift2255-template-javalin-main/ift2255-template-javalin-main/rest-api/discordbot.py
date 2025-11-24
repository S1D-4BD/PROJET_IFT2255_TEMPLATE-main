

####JAI PRIS DE ICI LE BOT: https://github.com/Rapptz/discord.py/blob/v2.6.4/examples/basic_bot.py# This example requires the 'members' and 'message_content' privileged intents to function.
## et le tuto : https://www.youtube.com/watch?v=YD_N6Ffoojw# Bot Discord pour scanner les sigles de cours UdeM
import discord
from discord.ext import commands
import re ##pr regex omg for once jlutilises sans me crever les yeux
import requests


import random
import os
from dotenv import load_dotenv


load_dotenv()

description = """Bot pour scrape les commentaires"""

intents = discord.Intents.default()
intents.members = True
intents.message_content = True

bot = commands.Bot(command_prefix='?', description=description, intents=intents)


API_URL = "http://localhost:3000" ###url du backend


def select_rand_name():
  lstnomsbiz = ["Udémien", "Académicien", "Squatteur", "AlenaJunior", "MarkFeeling"]
  i = random.randint(0,4)
  return lstnomsbiz[i]

def has_sigle(texte):
    ###on verifie par regex (thanks ift3700 ffs) les cours disponibles
    ### btw les fins de cours c est de mémoire,genre 1575, ift3700 mat1978 phy1972
    ##as always jv ignorer la maj/min
    if re.search(r'\b[A-Z]{3}[-\s]?[1236]\d{2}[0582]\b', texte, re.IGNORECASE): #mais mdr, jai oublié ya des cours qui finissent en 8 -> mat1978
        return True
    if re.search(r'\b[1236]\d{2}[0582]\b', texte):
        return True
    return False

def extraire_sigles(texte):

    sigles = re.findall(r'\b[A-Z]{3}[-\s]?[1236]\d{2}[0582]\b', texte, re.IGNORECASE)
    return [s.upper().replace(' ', '').replace('-', '') for s in sigles]
@bot.event
async def on_ready():
    assert bot.user is not None
    print(f'Logged in as {bot.user} (ID: {bot.user.id})')

    print('--------------------------------')

    print("lecture de l'historique de la convo")
    total = 0
    for guild in bot.guilds:
        print(f"Serveur: {guild.name}")
        for channel in guild.text_channels:
            print(f"Channel: {channel.name}") ## debug again, le canal c commentaires
            try:
                count = 0
                async for message in channel.history(limit=500): ## also pr eviter le potential latency
                    if has_sigle(message.content):
                        count += 1
                        total += 1
                        print(f"Trouve: {message.content[:50]}")  ## jai limit a 50 chars sinn c juste trop long

                        sigles = extraire_sigles(message.content)
                        for sigle in sigles:
                            data = {
                                "courseId": sigle,
                                "author": select_rand_name(),
                                "message": message.content
                            }
                            requests.post(f"{API_URL}/comments", json=data) ## pr chaque commentaire deja la jv les post pour que qd jv les get via get course/:id je le trouve direct

                print(f"Messages dans le channel: {count}")
            except Exception as e:
                print(f"Erreur: {e}")

    print(f"Total trouve: {total}")
    print("Ecoute en cours")

@bot.event
async def on_message(message):
    print(f"Message recu de {message.author}: {message.content}")  # DEBUG

    if has_sigle(message.content):
        print(f"Sigle trouve: {message.content}")

        sigles = extraire_sigles(message.content)
        for sigle in sigles:
            try:
                data = {
                    "courseId": sigle,
                    "author": str(message.author),
                    "message": message.content
                }

                response = requests.post(f"{API_URL}/comments", json=data)
                print(f"Envoye a API: {sigle} - Status: {response.status_code}")
            except Exception as e:
                print(f"Erreur API: {e}")

    await bot.process_commands(message)


bot.run(os.getenv("TOKEN"))
####