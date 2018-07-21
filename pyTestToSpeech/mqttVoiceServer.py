import paho.mqtt.client as mqtt
from google.cloud import texttospeech
from multiprocessing import Process
import subprocess


artifactNameList = ["Mona_Lisa","The_Birth_of_Venus","The_Starry_Night"]

def on_log(client, userdata, level, buf):
    print("log: ",buf)


def on_message(client, userdata, message):
    artifactName = message.payload.decode("utf-8")
    userName = message.topic[7:]
    print(userName)
    def run(name):
        subprocess.run(["afplay","%s.mp3"%name])
    if artifactName in artifactNameList:
        p = Process(target=run,args=(artifactName,))
        p.start()
    

brokerAddress = "192.168.1.8"
mqclient = mqtt.Client("museumServer")
mqclient.on_message=on_message
mqclient.on_log = on_log
mqclient.username_pw_set(username="admin",password="19930903")
mqclient.connect(brokerAddress,1883,60)
mqclient.subscribe("museum/#")
mqclient.loop_forever()


