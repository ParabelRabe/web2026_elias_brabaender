import os
script_dir = os.path.dirname(__file__)
file_path = os.path.join(script_dir, 'uebersetzungen.csv')
opdat = open(file_path, 'r')
allzeilen = opdat.readlines()
sprachen = allzeilen[0].split(';')
maxlen = len(sprachen)
n = 0
dateien = []
while n < maxlen:
    if n != 0:
        if n == maxlen-1:
            sprache = sprachen[n]
            saubersprache = sprache.replace('\n', '')
            datei = open(f"src/main/resources/messages_{saubersprache}.properties", "w")
        else: 
            datei = open(f"src/main/resources/messages_{sprachen[n]}.properties", "w")
        dateien.append(datei)
    n = n+1
dateimain = open(f"src/main/resources/messages.properties", "w")
dateien.append(dateimain)
m = 0
n = 0
for zeile in allzeilen:
    if m != 0:
        n = 0
        daten = zeile.split(';')
        for date in daten:
            if n != 0:
                if n == 1:
                    dateien[maxlen-1].write(f"{daten[0]}={date}\n")
                if n == maxlen-1:
                    sauberdate = date.replace('\n', '')
                    dateien[n-1].write(f"{daten[0]}={sauberdate}\n")
                else:
                    dateien[n-1].write(f"{daten[0]}={date}\n")
            n = n+1
    m = m +1

    


