import RPi.GPIO as GPIO
import time
from bluetooth import *


#####fonctions
def angleTOpercent (angle) :
    if angle > 180 or angle < 0 :
        return False

    start = 4
    end = 12.5
    ratio = (end - start)/180

    angle_as_percent = angle * ratio

    return start + angle_as_percent
#####






GPIO.setwarnings(False)

LED_PIN = 17

pinMoteur = 24
frequence = 50

GPIO.setmode(GPIO.BCM)
GPIO.setup(LED_PIN, GPIO.OUT)
GPIO.setup(pinMoteur, GPIO.OUT)
pwm = GPIO.PWM(pinMoteur, frequence)



server_sock=BluetoothSocket( RFCOMM )
server_sock.bind(("",PORT_ANY))
server_sock.listen(1)

port = server_sock.getsockname()[1]

uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"

advertise_service( server_sock, "SampleServer",
                   service_id = uuid,
                   service_classes = [ uuid, SERIAL_PORT_CLASS ],
                   profiles = [ SERIAL_PORT_PROFILE ], 
                    )

try:
    while True:
        print("Waiting for connection on RFCOMM channel %d" % port)

        client_sock, client_info = server_sock.accept()
        print("Accepted connection from ", client_info)

        try:
            while True:
                data = client_sock.recv(1024)
                if len(data) == 0: break
                print("received [%s]" % data)
                
                if data == 'led1':
                    GPIO.output(LED_PIN,True)
                    data ='led on'
                elif data == 'led0':
                    GPIO.output(LED_PIN,False)
                    data = 'led off'
                elif 'moteur' in data:
                    angle = data.split(' ')
                    print(angle[1])
                    pwm.start(angleTOpercent(int(angle[1])))
                    time.sleep(1)
                    #pwm.stop()
                elif data =='exit':
                    raise Exception('stop serveur')
                else:
                    data = 'Commande inconnue !' 
                
                client_sock.send(data)
                print ("sending [%s]" % data)
                
                
        except IOError:
            pass

        print("disconnected")    
        client_sock.close()
except Exception:
    print("Stop serveur Bluetooth !")
server_sock.close()
GPIO.cleanup()



